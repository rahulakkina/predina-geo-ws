package com.predina.geo.ws.exception;

public class GeoDataException extends Throwable {
    public GeoDataException(String s) {
        super(s);
    }

    public GeoDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    @Override
    public String getMessage() {
        return String.format("%s : %s",this.getClass().getName(),super.getMessage()) ;
    }
}
