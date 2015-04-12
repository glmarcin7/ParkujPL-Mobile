package pl.parkujznami.parkujpl_mobile.models.city;

import com.google.gson.annotations.Expose;

public class Spots {

    @Expose
    private Integer min;
    @Expose
    private Integer max;

    /**
     * @return The min
     */
    public Integer getMin() {
        return min;
    }

    /**
     * @param min The min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     * @return The max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * @param max The max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

}