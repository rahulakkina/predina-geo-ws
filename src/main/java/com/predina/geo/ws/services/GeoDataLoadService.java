package com.predina.geo.ws.services;


import com.predina.geo.ws.exception.GeoDataException;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;

import java.util.List;


/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
public interface GeoDataLoadService {

    public GeoMapLocation loadWithoutRiskScore(final GeoCoordinate geoCoordinate);

    public GeoMapLocation loadWithRiskScore(final GeoCoordinate geoCoordinate, final Integer riskScore);

    public List<GeoMapLocation> load() throws GeoDataException;

    public void store() throws GeoDataException;

    public void refresh() throws GeoDataException;

}
