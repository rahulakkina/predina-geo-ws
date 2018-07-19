package com.predina.geo.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import static com.predina.geo.ws.model.GeoRiskScoreIndicator.*;

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

    @XmlElement(name = "gid")
    private final GeoRiskScoreIndicator gid;

    public GeoMapLocation(){
        this.coords = new GeoCoordinate();
        this.rs = 1;
        this.gid = G;
    }

    public GeoMapLocation(final GeoCoordinate coords, final Integer rs) {
        this.coords = coords;
        this.rs = rs;
        this.gid = (rs >= 1 && rs <= 3) ? G:
                (rs >= 4 && rs <= 5) ? Y:
                        (rs >= 6 && rs <= 7) ? O :
                                (rs == 8) ? R : D;
    }

    public GeoCoordinate getCoords() {
        return coords;
    }

    public Integer getRs() {
        return rs;
    }

    public GeoRiskScoreIndicator getGid() {
        return gid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoMapLocation)) return false;

        GeoMapLocation that = (GeoMapLocation) o;

        if (getCoords() != null ? !getCoords().equals(that.getCoords()) : that.getCoords() != null) return false;
        if (getRs() != null ? !getRs().equals(that.getRs()) : that.getRs() != null) return false;
        return getGid() == that.getGid();
    }

    @Override
    public int hashCode() {
        int result = getCoords() != null ? getCoords().hashCode() : 0;
        result = 31 * result + (getRs() != null ? getRs().hashCode() : 0);
        result = 31 * result + (getGid() != null ? getGid().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "'coords' : " + coords +
                ", 'rs' : " + rs +
                ", 'gid' : " + gid +
                '}';
    }
}