package com.predina.geo.ws.controller;

import com.predina.geo.ws.model.GeoCoordinate;
import com.predina.geo.ws.model.GeoMapLocation;
import com.predina.geo.ws.services.GeoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@RestController
public class GeoDataController {


    private GeoDataService geoDataService;

    @Autowired
    public GeoDataController(final GeoDataService geoDataService){
        this.geoDataService = geoDataService;
    }

    public GeoDataService getGeoDataService() {
        return geoDataService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GeoMapLocation>> all(){
        return new ResponseEntity<List<GeoMapLocation>>(geoDataService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/lookup/{lat}/{lng}")
    public ResponseEntity<GeoMapLocation> lookup(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng){
        return new ResponseEntity<GeoMapLocation>(getGeoDataService().lookup(new GeoCoordinate(lat, lng)), HttpStatus.OK);
    }

    @GetMapping("/update/{lat}/{lng}/{riskScore}")
    @SendTo("/topic/geoDataUpdates")
    public ResponseEntity<GeoMapLocation> update(@PathVariable("lat") final Double lat, @PathVariable("lng") final Double lng, @PathVariable("riskScore") final Integer riskScore){
        return new ResponseEntity<GeoMapLocation>(getGeoDataService().update(new GeoCoordinate(lat, lng), riskScore), HttpStatus.OK);
    }

}
