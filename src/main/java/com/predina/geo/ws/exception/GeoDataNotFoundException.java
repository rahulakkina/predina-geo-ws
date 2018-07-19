package com.predina.geo.ws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeoDataNotFoundException extends RuntimeException{
    public GeoDataNotFoundException(String exception) {
        super(exception);
    }
}