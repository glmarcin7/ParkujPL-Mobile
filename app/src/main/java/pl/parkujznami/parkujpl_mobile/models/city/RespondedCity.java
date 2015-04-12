package pl.parkujznami.parkujpl_mobile.models.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pl.parkujznami.parkujpl_mobile.models.shared.Coords;

public class RespondedCity {

    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private Coords coords;
    @Expose
    private Price price;
    @Expose
    private Spots spots;
    @SerializedName("max_parking_time")
    @Expose
    private MaxParkingTime maxParkingTime;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The coords
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * @param coords The coords
     */
    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    /**
     * @return The price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * @return The spots
     */
    public Spots getSpots() {
        return spots;
    }

    /**
     * @param spots The spots
     */
    public void setSpots(Spots spots) {
        this.spots = spots;
    }

    /**
     * @return The maxParkingTime
     */
    public MaxParkingTime getMaxParkingTime() {
        return maxParkingTime;
    }

    /**
     * @param maxParkingTime The max_parking_time
     */
    public void setMaxParkingTime(MaxParkingTime maxParkingTime) {
        this.maxParkingTime = maxParkingTime;
    }

}