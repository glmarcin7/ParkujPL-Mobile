package pl.parkujznami.parkujpl_mobile.models.parking;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import pl.parkujznami.parkujpl_mobile.models.shared.Coords;

public class Parking {

    @Expose
    private Integer id;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @Expose
    private String name;
    @Expose
    private String price;
    @Expose
    private Integer spots;
    @Expose
    private String address;
    @Expose
    private Coords coords;
    @SerializedName("max_parking_time")
    @Expose
    private Integer maxParkingTime;
    @Expose
    private String distance;
    @Expose
    private Availabilty availabilty;

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
     * @return The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return The spots
     */
    public Integer getSpots() {
        return spots;
    }

    /**
     * @param spots The spots
     */
    public void setSpots(Integer spots) {
        this.spots = spots;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return The maxParkingTime
     */
    public Integer getMaxParkingTime() {
        return maxParkingTime;
    }

    /**
     * @param maxParkingTime The max_parking_time
     */
    public void setMaxParkingTime(Integer maxParkingTime) {
        this.maxParkingTime = maxParkingTime;
    }

    /**
     * @return The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return The availabilty
     */
    public Availabilty getAvailabilty() {
        return availabilty;
    }

    /**
     * @param availabilty The availabilty
     */
    public void setAvailabilty(Availabilty availabilty) {
        this.availabilty = availabilty;
    }

    /**
     * Comparator for sorting the list by costs
     */
    public static Comparator<Parking> costsComparator = new Comparator<Parking>() {

        public int compare(Parking p1, Parking p2) {
            Double p1Costs = Double.parseDouble(p1.getPrice());
            Double p2Costs = Double.parseDouble(p2.getPrice());

            //ascending order
            if (p2Costs <= p1Costs) {
                return 1;
            }
            return -1;
        }
    };

    /**
     * Comparator for sorting the list by distance
     */
    public static Comparator<Parking> distanceComparator = new Comparator<Parking>() {

        public int compare(Parking p1, Parking p2) {
            String p1DistanceString = p1.getDistance();
            String p2DistanceString = p2.getDistance();
            Double p1Distance = Double.parseDouble(p1DistanceString.substring(0, p1DistanceString.length() - 1));
            Double p2Distance = Double.parseDouble(p2DistanceString.substring(0, p2DistanceString.length() - 1));

            //ascending order
            if (p2Distance <= p1Distance) {
                return 1;
            }
            return -1;
        }
    };

    /**
     * Comparator for sorting the list by spots availability
     */
    public static Comparator<Parking> spotsAvailabilityComparator = new Comparator<Parking>() {

        public int compare(Parking p1, Parking p2) {
            Double p1AvailabilityFactor = p1.getAvailabilty().getNumberOfFreeSpotsFactor();
            Double p2AvailabilityFactor = p2.getAvailabilty().getNumberOfFreeSpotsFactor();

            //descending order
            if (p2AvailabilityFactor >= p1AvailabilityFactor) {
                return 1;
            }
            return -1;
        }
    };

    public String toString(Context context) {
        return "Parking{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", spots=" + spots +
                ", address='" + address + '\'' +
                ", coords=" + coords +
                ", maxParkingTime=" + maxParkingTime +
                ", distance='" + distance + '\'' +
                ", availabilty=" + availabilty.toString(context) +
                '}';
    }
}