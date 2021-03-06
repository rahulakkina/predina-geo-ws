package com.predina.geo.ws.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import com.predina.geo.ws.services.GeoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@RestController
public class GeoDataController {


    private GeoDataService geoDataService;

    @Autowired
    public GeoDataController(final GeoDataService geoDataService) {
        this.geoDataService = geoDataService;
    }

    public GeoDataService getGeoDataService() {
        return geoDataService;
    }

    /**
     *
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    public ResponseEntity<List<List<Serializable>>> all() {
        return new ResponseEntity<List<List<Serializable>>>(
                Lists.transform(geoDataService.findAll(),this::transformList), HttpStatus.OK);
    }


    /**
     *
     * @param pageId
     * @param pageSize
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/page/{pageId}/{pageSize}")
    public ResponseEntity<Map<String, Serializable>> page(@PathVariable("pageId") final Integer pageId, @PathVariable("pageSize") final Integer pageSize){
        final Map<String, Serializable> result = Maps.newHashMapWithExpectedSize(3);
        final List<List<GeoMapLocation>> pages = Lists.partition(geoDataService.findAll(), pageSize);
        if(!CollectionUtils.isEmpty(pages)) {
            final ImmutableList<List<Serializable>> data = ImmutableList.copyOf((pageId != null && pageId <= pages.size()) ?
                              Lists.transform(pages.get(pageId - 1),this::transformList) : Lists.transform(pages.get(0),this::transformList));
            result.put("d", data);
            result.put( "p", (pageId != null && pageId <= pages.size()) ? pageId : 1);
            result.put("m", pages.size());
        }else{
            result.put("d", Lists.newArrayList());
            result.put( "p",1);
            result.put("m", pages.size());
        }
        return new ResponseEntity<Map<String, Serializable>>(result, HttpStatus.OK);
    }

    /**
     *
     * @param lat
     * @param lng
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/lookup/{lat}/{lng}")
    public ResponseEntity<List<Serializable>> lookup(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng) {
        return new ResponseEntity<List<Serializable>>(transformList(getGeoDataService().lookup(new GeoCoordinate(lat, lng))), HttpStatus.OK);
    }

    /**
     *
     * @param lat
     * @param lng
     * @param riskScore
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/update/{lat}/{lng}/{riskScore}")
    @SendTo("/topic/geoDataUpdates")
    public ResponseEntity<List<Serializable>> update(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng, @PathVariable("riskScore") final Integer riskScore) {
        return new ResponseEntity<List<Serializable>>(transformList(getGeoDataService().update(new GeoCoordinate(lat, lng), riskScore)), HttpStatus.OK);
    }

    /**
     *
     * @param topLeftLat
     * @param topLeftLng
     * @param bottomRightLat
     * @param bottomRightLng
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/find/{topLeftLat}/{topLeftLng}/{bottomRightLat}/{bottomRightLng}")
    public ResponseEntity<Map<String, Serializable>> find(@PathVariable("topLeftLat") final Double topLeftLat, @PathVariable("topLeftLng") final Double topLeftLng,
                  @PathVariable("bottomRightLat") final Double bottomRightLat, @PathVariable("bottomRightLng") final Double bottomRightLng){
        final List<List<Serializable>> data = Lists.transform(getGeoDataService().find(getCoord(topLeftLat, topLeftLng), getCoord(bottomRightLat, bottomRightLng)), this::transformList);
        return new ResponseEntity<Map<String, Serializable>>(ImmutableMap.of(
                "s", (data != null ? data.size() : 0),
                "d", ImmutableList.copyOf(data),
                "z", (!CollectionUtils.isEmpty(data) ? data.get(0).get(3) : false)
        ), HttpStatus.OK);
    }


    /**
     *
     * @param lat
     * @param lng
     * @return
     */
    protected GeoCoordinate getCoord(final Double lat, final Double lng){
        return new GeoCoordinate(lat, lng);
    }

    /**
     *
     * @param geoMapLocation
     * @return
     */
    protected ImmutableList<Serializable> transformList(final GeoMapLocation geoMapLocation){
        return ImmutableList.of(geoMapLocation.getCoords().getLat(), geoMapLocation.getCoords().getLng(), geoMapLocation.getGid(), geoMapLocation.getZ(), geoMapLocation.getRs());
    }

}
