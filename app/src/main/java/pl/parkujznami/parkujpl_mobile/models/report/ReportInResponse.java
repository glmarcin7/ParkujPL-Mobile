package pl.parkujznami.parkujpl_mobile.models.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportInResponse {

    @Expose
    private Integer id;
    @SerializedName("parking_id")
    @Expose
    private Integer parkingId;
    @Expose
    private Integer availabilty;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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
     * @return The parkingId
     */
    public Integer getParkingId() {
        return parkingId;
    }

    /**
     * @param parkingId The parking_id
     */
    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

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

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
