package pl.parkujznami.parkujpl_mobile.models.shared;

import com.google.gson.annotations.Expose;

public class Coords {

    @Expose
    private String latitude;
    @Expose
    private String longitude;

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
