/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.predina.geo.ws.api;

import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;

public class SphericalMercatorProjection {

    private final Double mWorldWidth;

    public SphericalMercatorProjection(final Double worldWidth) {
        mWorldWidth = worldWidth;
    }


    public GeoMapLocation toPoint(final GeoMapLocation latLng) {
        final Double x = latLng.getCoords().getLng() / 360 + .5;
        final Double sinY = Math.sin(Math.toRadians(latLng.getCoords().getLat()));
        final Double y = 0.5 * Math.log((1 + sinY) / (1 - sinY)) / -(2 * Math.PI) + .5;

        return new GeoMapLocation(new GeoCoordinate(x * mWorldWidth, y * mWorldWidth), latLng.getRs(), true);
    }

    public GeoMapLocation toLatLng(final GeoMapLocation location) {
        final GeoCoordinate point = location.getCoords();
        final Double x = point.getLat() / mWorldWidth - 0.5;
        final Double lng = x * 360;

        double y = .5 - (point.getLng() / mWorldWidth);
        final double lat = 90 - Math.toDegrees(Math.atan(Math.exp(-y * 2 * Math.PI)) * 2);

        return new GeoMapLocation(new GeoCoordinate(lat, lng), location.getRs(), true);
    }
}
