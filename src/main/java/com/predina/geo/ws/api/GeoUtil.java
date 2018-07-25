package com.predina.geo.ws.api;

import com.google.common.collect.Lists;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;


import java.util.List;

public abstract class GeoUtil {

    private static final Integer OFFSET = 268435456;
    private static final Double RADIUS = 85445659.4471;
    private static Double PI = 3.1444;

    public static Integer coordinateDistance(final GeoCoordinate c1, final GeoCoordinate c2, final Integer zoom){

        final double x1 = lonToX(c1.getLng());
        final double y1 = latToY(c1.getLat());

        final double x2 = lonToX(c2.getLng());
        final double y2 = latToY(c2.getLat());

        final double dX = (x1 - x2);
        final double dY = (y1 - y2);

        return (int) Math.sqrt(dX * dX + dY * dY)  >> (21 - zoom);

    }

    public static Double  lonToX(final Double lon) {
        final double x = Math.round(OFFSET + RADIUS * lon * PI / 180);
        return x;
    }

    public static Double latToY(final Double lat) {
        final double y = Math.round(OFFSET
                - RADIUS
                * Math.log((1 + Math.sin(lat * PI / 180))
                / (1 - Math.sin(lat * PI / 180))) / 2);
        return y;
    }

    public static List<GeoMapLocation> generateClusters(final List<GeoMapLocation> geoLocations, final Integer zoom) {

        final List<GeoMapLocation> clusters = Lists.newArrayList();

        final List<GeoMapLocation> markers = Lists.newArrayList(geoLocations);
        final List<GeoMapLocation> originalListCopy = Lists.newArrayList(markers);

        for (int i = 0; i < originalListCopy.size();) {
            final List<GeoMapLocation> markerList = Lists.newArrayList();

            for (int j = i + 1; j < markers.size();) {
                final Integer pixelDistance = coordinateDistance(markers.get(i).getCoords(), markers.get(j).getCoords(), zoom);

                if(pixelDistance < 40){
                    markerList.add(markers.get(i));
                    markerList.add(markers.get(j));
                    markers.remove(j);
                    originalListCopy.remove(j);
                    j = i + 1;
                } else {
                    j++;
                }
            }

            if (markerList.size() > 0) {
                final GeoMapLocation cluster =
                        new GeoMapLocation(originalListCopy.get(i).getCoords(), originalListCopy.get(i).getRs());
                clusters.add(cluster);
                originalListCopy.remove(i);
                markers.remove(i);
                i = 0;
            } else {
                i++;
            }
        }

        return clusters;
    }


}
