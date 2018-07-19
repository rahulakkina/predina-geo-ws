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
public class GeoMapLocation implements Serializable {

    private static final long serialVersionUID = -1L;

    @XmlElement(name = "coords")
    private final GeoCoordinate coords;

    @XmlElement(name = "rs")
    private final Integer rs;

    @XmlElement(name = "geoRsInd")
    private final GeoRiskScoreIndicator geoRsInd;

    public GeoMapLocation(){
        this.coords = new GeoCoordinate();
        this.rs = 1;
        this.geoRsInd = GeoRiskScoreIndicator.GREEN;
    }

    public GeoMapLocation(final GeoCoordinate coords, final Integer rs) {
        this.coords = coords;
        this.rs = rs;
        this.geoRsInd = (rs >= 1 && rs <= 3) ? GeoRiskScoreIndicator.GREEN :
                (rs >= 4 && rs <= 5) ? GeoRiskScoreIndicator.YELLOW :
                        (rs >= 6 && rs <= 7) ? GeoRiskScoreIndicator.ORANGE :
                                (rs == 8) ? GeoRiskScoreIndicator.RED : GeoRiskScoreIndicator.DARK_RED;
    }

    public GeoCoordinate getCoords() {
        return coords;
    }

    public Integer getRs() {
        return rs;
    }

    public GeoRiskScoreIndicator getGeoRsInd() {
        return geoRsInd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoMapLocation that = (GeoMapLocation) o;

        if (coords != null ? !coords.equals(that.coords) : that.coords != null) return false;
        if (rs != null ? !rs.equals(that.rs) : that.rs != null) return false;
        return geoRsInd == that.geoRsInd;
    }

    @Override
    public int hashCode() {
        int result = coords != null ? coords.hashCode() : 0;
        result = 31 * result + (rs != null ? rs.hashCode() : 0);
        result = 31 * result + (geoRsInd != null ? geoRsInd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "'coords' : " + coords +
                ", 'rs' :" + rs +
                ", 'geoRsInd' : " + geoRsInd +
                '}';
    }
}