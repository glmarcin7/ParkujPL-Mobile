package pl.parkujznami.parkujpl_mobile.models.parking;

import com.google.gson.annotations.Expose;

public class Availabilty {

    @Expose
    private Integer low;
    @Expose
    private Integer medium;
    @Expose
    private Integer high;

    /**
     * @return The low
     */
    public Integer getLow() {
        return low;
    }

    /**
     * @param low The low
     */
    public void setLow(Integer low) {
        this.low = low;
    }

    /**
     * @return The medium
     */
    public Integer getMedium() {
        return medium;
    }

    /**
     * @param medium The medium
     */
    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    /**
     * @return The high
     */
    public Integer getHigh() {
        return high;
    }

    /**
     * @param high The high
     */
    public void setHigh(Integer high) {
        this.high = high;
    }

}
