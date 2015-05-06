package pl.parkujznami.parkujpl_mobile.models.parking;

import android.content.Context;

import com.google.gson.annotations.Expose;

import pl.parkujznami.parkujpl_mobile.R;

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

    public Double getNumberOfFreeSpotsFactor() {
        int sum = low + medium + high;
        return ((double) (low + medium * 3 + high * 5)) / (sum != 0 ? sum : 1);
    }

    public String toString(Context context) {
        final Double numberOfFreeSpotsFactor = getNumberOfFreeSpotsFactor();

        if (numberOfFreeSpotsFactor < 1.5) return context.getString(R.string.s_really_little_space);
        if (numberOfFreeSpotsFactor < 2.5) return context.getString(R.string.s_little_space);
        if (numberOfFreeSpotsFactor < 3.5)
            return context.getString(R.string.s_the_average_amount_of_space);
        if (numberOfFreeSpotsFactor < 4.5) return context.getString(R.string.s_a_lot_of_space);
        return context.getString(R.string.s_really_a_lot_of_space);
    }
}
