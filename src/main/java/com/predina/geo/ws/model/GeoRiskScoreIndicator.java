package com.predina.geo.ws.model;

/**
 * @author Rahul Anand Akkina
 * @version v1.0
 */
public enum GeoRiskScoreIndicator {
    DARK_RED(139, 0, 0),
    RED(255, 0, 0),
    ORANGE(255,165,0),
    YELLOW(255,255,0),
    GREEN(0,128,0);

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