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

    @XmlElement(name = "riskScore")
    private final Integer riskScore;

    @XmlElement(name = "geoRiskScoreIndicator")
    private final GeoRiskScoreIndicator geoRiskScoreIndicator;

    public GeoMapLocation(){
        coords = new GeoCoordinate();
        riskScore = 1;
        geoRiskScoreIndicator = GeoRiskScoreIndicator.GREEN;
    }

    public GeoMapLocation(final GeoCoordinate coords, final Integer riskScore) {
        this.coords = coords;
        this.riskScore = riskScore;
        this.geoRiskScoreIndicator = (riskScore >= 1 && riskScore <= 3) ? GeoRiskScoreIndicator.GREEN :
                (riskScore >= 4 && riskScore <= 5) ? GeoRiskScoreIndicator.YELLOW :
                        (riskScore >= 6 && riskScore <= 7) ? GeoRiskScoreIndicator.ORANGE :
                                (riskScore == 8) ? GeoRiskScoreIndicator.RED : GeoRiskScoreIndicator.DARK_RED;
    }

    public GeoCoordinate getCoords() {
        return coords;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public GeoRiskScoreIndicator getGeoRiskScoreIndicator() {
        return geoRiskScoreIndicator;
    }

    @Override
    public String toString() {
        return "{" +
                "'coords' : " + coords +
                ", 'riskScore' " + riskScore +
                ", 'geoRiskScoreIndicator' : " + geoRiskScoreIndicator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoMapLocation)) return false;

        final GeoMapLocation that = (GeoMapLocation) o;

        if (getCoords() != null ? !getCoords().equals(that.getCoords()) : that.getCoords() != null) return false;
        if (getRiskScore() != null ? !getRiskScore().equals(that.getRiskScore()) : that.getRiskScore() != null)
            return false;
        return getGeoRiskScoreIndicator() == that.getGeoRiskScoreIndicator();
    }

    @Override
    public int hashCode() {
        int result = getCoords() != null ? getCoords().hashCode() : 0;
        result = 31 * result + (getRiskScore() != null ? getRiskScore().hashCode() : 0);
        result = 31 * result + (getGeoRiskScoreIndicator() != null ? getGeoRiskScoreIndicator().hashCode() : 0);
        return result;
    }
}