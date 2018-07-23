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

    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Serializable>>> all() {
        return new ResponseEntity<List<Map<String, Serializable>>>(
                Lists.transform(geoDataService.findAll(),this::transform), HttpStatus.OK);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/page/{pageId}")
    public ResponseEntity<Map<String, Serializable>> page(@PathVariable("pageId") final Integer pageId){
        final Map<String, Serializable> result = Maps.newHashMapWithExpectedSize(3);
        final List<List<GeoMapLocation>> pages = Lists.partition(geoDataService.findAll(), 22850);
        if(!CollectionUtils.isEmpty(pages)) {
            final ImmutableList<Map<String, Serializable>> data = ImmutableList.copyOf((pageId != null && pageId <= pages.size()) ?
                              Lists.transform(pages.get(pageId - 1),this::transform) : Lists.transform(pages.get(0),this::transform));
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

    @CrossOrigin(origins = "*")
    @GetMapping("/lookup/{lat}/{lng}")
    public ResponseEntity<Map<String, Serializable>> lookup(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng) {
        return new ResponseEntity<Map<String, Serializable>>(transform(getGeoDataService().lookup(new GeoCoordinate(lat, lng))), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/update/{lat}/{lng}/{riskScore}")
    @SendTo("/topic/geoDataUpdates")
    public ResponseEntity<Map<String, Serializable>> update(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng, @PathVariable("riskScore") final Integer riskScore) {
        return new ResponseEntity<Map<String, Serializable>>(transform(getGeoDataService().update(new GeoCoordinate(lat, lng), riskScore)), HttpStatus.OK);
    }


    protected Map<String, Serializable> transform(final GeoMapLocation geoMapLocation){
        return ImmutableMap.of("lat", geoMapLocation.getCoords().getLat(),
                "lng",geoMapLocation.getCoords().getLng(),
                "rs", geoMapLocation.getRs(),
                "cin",geoMapLocation.getGid());
    }



}
