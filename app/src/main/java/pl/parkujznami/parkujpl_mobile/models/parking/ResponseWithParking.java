package pl.parkujznami.parkujpl_mobile.models.parking;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import pl.parkujznami.parkujpl_mobile.models.shared.Coords;

public class ResponseWithParking {

    @Expose
    private Coords coords;
    @Expose
    private List<Parking> parkings = new ArrayList<Parking>();

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
     * @return The parkings
     */
    public List<Parking> getParkings() {
        return parkings;
    }

    /**
     * @param parkings The parkings
     */
    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }

}
