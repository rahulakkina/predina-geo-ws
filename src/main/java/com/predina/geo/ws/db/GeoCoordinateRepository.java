package com.predina.geo.ws.db;


import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;

import java.io.Serializable;
import java.util.List;


/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
public interface GeoCoordinateRepository extends Serializable {

    default Integer getRandom(Integer max){
        Integer randomValue = (int) (Math.random()*max);
        return randomValue == 0 ? 1 : randomValue;
    }

    public GeoMapLocation updateWithoutRiskScore(final GeoCoordinate geoCoordinate);

    public GeoMapLocation updateWithRiskScore(final GeoCoordinate geoCoordinate, final Integer riskScore);

    public GeoMapLocation find(final GeoCoordinate geoCoordinate);

    public List<GeoMapLocation> findAll();

    public List<GeoMapLocation> find(final GeoCoordinate topLeft, final GeoCoordinate bottomRight);
}