package com.predina.geo.ws.services.impl;


import com.predina.geo.ws.db.GeoCoordinateRepository;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import com.predina.geo.ws.exception.GeoDataException;
import com.predina.geo.ws.services.GeoDataLoadService;
import com.predina.geo.ws.services.GeoDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@Component
public class GeoDataServiceImpl implements GeoDataService {

    private static Logger logger = Logger.getLogger(GeoDataServiceImpl.class);

    private final GeoCoordinateRepository geoCoordinateRepository;

    private final GeoDataLoadService geoDataLoadService;

    @Autowired
    public GeoDataServiceImpl(final GeoCoordinateRepository geoCoordinateRepository, final GeoDataLoadService geoDataLoadService) {
        this.geoCoordinateRepository = geoCoordinateRepository;
        this.geoDataLoadService = geoDataLoadService;
    }

    @Override
    public GeoMapLocation lookup(final GeoCoordinate geoCoordinate) {
        return geoCoordinateRepository.find(geoCoordinate);
    }

    @Override
    public GeoMapLocation update(final GeoCoordinate geoCoordinate, final Integer riskScore) {
        return geoCoordinateRepository.updateWithRiskScore(geoCoordinate, riskScore);
    }

    @Override
    public List<GeoMapLocation> findAll() {
        return geoCoordinateRepository.findAll();
    }

    @Override
    public void store() throws GeoDataException {
         geoDataLoadService.store();
    }

}
