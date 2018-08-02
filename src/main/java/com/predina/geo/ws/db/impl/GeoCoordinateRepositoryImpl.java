package com.predina.geo.ws.db.impl;

import com.github.davidmoten.geo.mem.Info;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.predina.geo.ws.api.GeoUtil;
import com.predina.geo.ws.api.Geomem;
import com.predina.geo.ws.db.GeoCoordinateRepository;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import com.predina.geo.ws.model.GeoRiskScoreIndicator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.Map.Entry;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@Component
public class GeoCoordinateRepositoryImpl implements GeoCoordinateRepository {

    private static final long serialVersionUID = 2L;

    private static Logger logger = Logger.getLogger(GeoCoordinateRepositoryImpl.class);

    private final Map<Double, Map<Double, Integer>> databaseRepo;

    private final Geomem<Integer, GeoRiskScoreIndicator> geoMem;

    private final Random random;

    private final Long startTime;

    @Value("${geo.data.page.default-size}")
    private Integer clusterFuzzyLimit;

    @Value("${geo.data.page.default-min-size}")
    private Integer minFuzzyLimit;


    @Value("${geo.data.page.default-grid-size}")
    private Integer mGridSize;

    @Value("${geo.data.page.drill.min}")
    private Integer drillMin;

    @Value("${geo.data.page.drill.max}")
    private Integer drillMax;

    @Autowired
    public GeoCoordinateRepositoryImpl(){
        this.geoMem = new Geomem<Integer, GeoRiskScoreIndicator>();
        databaseRepo = new ConcurrentHashMap<>(2315754, 0.75f);
        random = new Random();
        this.startTime = System.currentTimeMillis();
    }


    /**
     *
     * @param geoCoordinate
     * @return
     */
    public GeoMapLocation updateWithoutRiskScore(final GeoCoordinate geoCoordinate){
        return updateWithRiskScore(geoCoordinate, getRandom(10));
    }


    /**
     *
     * @param geoCoordinate
     * @param riskScore
     * @return
     */
    public GeoMapLocation updateWithRiskScore(final GeoCoordinate geoCoordinate, final Integer riskScore){

        if(!databaseRepo.containsKey(geoCoordinate.getLat())){
            databaseRepo.put(geoCoordinate.getLat(), new ConcurrentHashMap<Double, Integer>(26, 0.75f));
        }

        databaseRepo.get(geoCoordinate.getLat()).put(geoCoordinate.getLng(),riskScore);

        final GeoMapLocation geoMapLocation = new GeoMapLocation(geoCoordinate, riskScore);

        geoMem.add(geoCoordinate.getLat(), geoCoordinate.getLng(), (System.currentTimeMillis() - startTime), riskScore, geoMapLocation.getGid());

        return geoMapLocation;
    }


    /**
     *
     * @param geoCoordinate
     * @return
     */
    public GeoMapLocation find(final GeoCoordinate geoCoordinate){
        return (databaseRepo.containsKey(geoCoordinate.getLat()) && databaseRepo.get(geoCoordinate.getLat()).containsKey(geoCoordinate.getLng())) ?
                new GeoMapLocation(geoCoordinate, databaseRepo.get(geoCoordinate.getLat()).get(geoCoordinate.getLng()))
                :  new GeoMapLocation(new GeoCoordinate(0.0, 0.0), 1);
    }


    /**
     *
     * @return
     */
    public List<GeoMapLocation> findAll(){
        final List<GeoMapLocation> resultSet = Lists.newArrayListWithCapacity(2952140);
        Integer subMapSize = 0;

        for(Entry<Double, Map<Double, Integer>> latEntry : databaseRepo.entrySet()){
            if(subMapSize < latEntry.getValue().size()){
                subMapSize = latEntry.getValue().size();
            }
            for(Entry<Double, Integer> longEntry: latEntry.getValue().entrySet()){
                resultSet.add(new GeoMapLocation(new GeoCoordinate(latEntry.getKey(), longEntry.getKey()), longEntry.getValue()));
            }
        }

        if(logger.isDebugEnabled()){
            logger.debug(String.format("Database Size : %d, Submap Size : %d",databaseRepo.size(), subMapSize));
        }

        return ImmutableList.copyOf(resultSet);
    }

    public List<GeoMapLocation> find(final GeoCoordinate topLeft, final GeoCoordinate bottomRight){
        final Iterable<Info<Integer, GeoRiskScoreIndicator>> iterable = geoMem.find(topLeft.getLat(), topLeft.getLng(),
                bottomRight.getLat(), bottomRight.getLng(),
                0, (System.currentTimeMillis() - startTime));

        final Iterable<GeoMapLocation> geoMapLocations = Iterables.transform(iterable, this::transform);
        final Integer gMapSize = Iterables.size(geoMapLocations);

        if(logger.isDebugEnabled()) {
            logger.debug(gMapSize + "," + clusterFuzzyLimit);
        }

        //Fuzzy Logic to limit the number of markers.
        if(gMapSize > clusterFuzzyLimit){
            for(int i = drillMin; i <= drillMax; i++){
                final Set<GeoMapLocation> clusters = GeoUtil.generateClusters(geoMapLocations, i, mGridSize);
                final Integer clusterSize = !CollectionUtils.isEmpty(clusters) ? clusters.size() : 0;

                if(logger.isDebugEnabled()) {
                    logger.debug(String.format("Drill : %d, Cluster Size : %d", i, clusterSize));
                }

                if(clusterSize <= clusterFuzzyLimit && clusterSize >= minFuzzyLimit){
                    return ImmutableList.copyOf(clusters);
                }
            }
        }else{
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("Clustering is not required : [%s, %s, %d]", topLeft, bottomRight, gMapSize));
            }
        }

        return ImmutableList.copyOf(geoMapLocations);
    }

    private GeoMapLocation transform(final Info<Integer, GeoRiskScoreIndicator> info){
        final Double lat = info.lat();
        final Double lng = info.lon();
        final Integer riskScore = info.value();
        return new GeoMapLocation(new GeoCoordinate(lat, lng), riskScore);
    }



}

