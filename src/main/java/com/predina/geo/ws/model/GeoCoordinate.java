package com.predina.geo.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoCoordinate implements Serializable{

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "lat")
    private final Double lat;

    @XmlElement(name = "lng")
    private final Double lng;

    public GeoCoordinate(){
        this.lat = 0.0;
        this.lng = 0.0;
    }

    public GeoCoordinate(final Double lat, final Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoCoordinate)) return false;

        final GeoCoordinate that = (GeoCoordinate) o;

        if (getLat() != null ? !getLat().equals(that.getLat()) : that.getLat() != null) return false;
        return getLng() != null ? getLng().equals(that.getLng()) : that.getLng() == null;
    }

    @Override
    public int hashCode() {
        int result = getLat() != null ? getLat().hashCode() : 0;
        result = 31 * result + (getLng() != null ? getLng().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "'lat' : " + lat +
                ", 'lng' : " + lng +
                '}';
    }
}
