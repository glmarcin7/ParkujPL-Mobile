package pl.parkujznami.parkujpl_mobile.models.report;

import com.google.gson.annotations.Expose;

public class ReportInRequest {

    @Expose
    private Integer availabilty;

    /**
     * @return The availabilty
     */
    public Integer getAvailabilty() {
        return availabilty;
    }

    /**
     * @param availabilty The availabilty
     */
    public void setAvailabilty(Integer availabilty) {
        this.availabilty = availabilty;
    }

}