package pl.parkujznami.parkujpl_mobile.models.parking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestForParking {

    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @Expose
    private String destination;
    @SerializedName("max_parking_time_max")
    @Expose
    private Integer maxParkingTimeMax;
    @SerializedName("min_parking_time_min")
    @Expose
    private Integer minParkingTimeMin;
    @SerializedName("price_min")
    @Expose
    private Integer priceMin;
    @SerializedName("price_max")
    @Expose
    private Integer priceMax;
    @Expose
    private Integer radius;
    @SerializedName("spots_min")
    @Expose
    private Integer spotsMin;
    @SerializedName("spots_max")
    @Expose
    private Integer spotsMax;

    /**
     * @return The cityId
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * @param cityId The city_id
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * @return The destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination The destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return The maxParkingTimeMax
     */
    public Integer getMaxParkingTimeMax() {
        return maxParkingTimeMax;
    }

    /**
     * @param maxParkingTimeMax The max_parking_time_max
     */
    public void setMaxParkingTimeMax(Integer maxParkingTimeMax) {
        this.maxParkingTimeMax = maxParkingTimeMax;
    }

    /**
     * @return The minParkingTimeMin
     */
    public Integer getMinParkingTimeMin() {
        return minParkingTimeMin;
    }

    /**
     * @param minParkingTimeMin The min_parking_time_min
     */
    public void setMinParkingTimeMin(Integer minParkingTimeMin) {
        this.minParkingTimeMin = minParkingTimeMin;
    }

    /**
     * @return The priceMin
     */
    public Integer getPriceMin() {
        return priceMin;
    }

    /**
     * @param priceMin The price_min
     */
    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    /**
     * @return The priceMax
     */
    public Integer getPriceMax() {
        return priceMax;
    }

    /**
     * @param priceMax The price_max
     */
    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }

    /**
     * @return The radius
     */
    public Integer getRadius() {
        return radius;
    }

    /**
     * @param radius The radius
     */
    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    /**
     * @return The spotsMin
     */
    public Integer getSpotsMin() {
        return spotsMin;
    }

    /**
     * @param spotsMin The spots_min
     */
    public void setSpotsMin(Integer spotsMin) {
        this.spotsMin = spotsMin;
    }

    /**
     * @return The spotsMax
     */
    public Integer getSpotsMax() {
        return spotsMax;
    }

    /**
     * @param spotsMax The spots_max
     */
    public void setSpotsMax(Integer spotsMax) {
        this.spotsMax = spotsMax;
    }

}