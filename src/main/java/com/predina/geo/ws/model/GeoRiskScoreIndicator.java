package com.predina.geo.ws.model;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
public enum GeoRiskScoreIndicator {
    D(139, 0, 0),
    R(255, 0, 0),
    O(255,165,0),
    Y(255,255,0),
    G(0,128,0);

    private Integer red;
    private Integer green;
    private Integer blue;

    GeoRiskScoreIndicator(final Integer red, final Integer green, final Integer blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getColor(){
        return super.toString();
    }

    public Integer getRed() {
        return red;
    }

    public Integer getGreen() {
        return green;
    }

    public Integer getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "{" +
                "'color' : '" + getColor() +
                "' , 'red' : " + red +
                ", 'green' : " + green +
                ", 'blue' : " + blue +
                '}';
    }
}