package com.predina.geo.ws.services;



import com.predina.geo.ws.exception.GeoDataException;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;

import java.util.List;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
public interface GeoDataService {

    public GeoMapLocation lookup(final GeoCoordinate geoCoordinate);

    public GeoMapLocation update(final GeoCoordinate geoCoordinate, final Integer riskScore);

    public List<GeoMapLocation> findAll();

    public List<GeoMapLocation> find(final GeoCoordinate topLeft, final GeoCoordinate bottomRight);

    public void store() throws GeoDataException;

}
