package pl.parkujznami.parkujpl_mobile.models.city;

import com.google.gson.annotations.Expose;

public class Price {

    @Expose
    private String min;
    @Expose
    private String max;

    /**
     * @return The min
     */
    public String getMin() {
        return min;
    }

    /**
     * @param min The min
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * @return The max
     */
    public String getMax() {
        return max;
    }

    /**
     * @param max The max
     */
    public void setMax(String max) {
        this.max = max;
    }

}