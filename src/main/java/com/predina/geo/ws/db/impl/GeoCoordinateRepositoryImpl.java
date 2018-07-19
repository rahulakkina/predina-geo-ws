package com.predina.geo.ws.db.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.predina.geo.ws.db.GeoCoordinateRepository;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.Random;
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

    private final Random random;

    public GeoCoordinateRepositoryImpl(){
        databaseRepo = new ConcurrentHashMap<>(2315754, 0.75f);
        random = new Random();
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

        return new GeoMapLocation(geoCoordinate, riskScore);
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



}

