package com.github.transitbot.api.models;

/**
 * Class represents Coordinates of location.
 */
public class Coordinates {

    /**
     * Longitude.
     */
    private String longitude;

    /**
     * Latitude.
     */
    private String latitude;

    /**
     * Constructor.
     * @param longitude longitude
     * @param latitude latitude
     */
    public Coordinates(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * getter.
     * @return longitude as string.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * setter.
     * @param longitude string
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * getter.
     * @return latitude as string.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * setter.
     * @param latitude as string.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * to string method.
     * @return string
     */
    @Override
    public String toString(){
        return "Lat: "+this.latitude+" Lon: "+this.longitude;
    }
}
