
package pl.parkujznami.parkujpl_mobile.models.parkandride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pl.parkujznami.parkujpl_mobile.models.shared.Coords;

public class Geometry {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private Coords coordinates;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The coordinates
     */
    public Coords getCoordinates() {
        return coordinates;
    }

    /**
     * 
     * @param coordinates
     *     The coordinates
     */
    public void setCoordinates(Coords coordinates) {
        this.coordinates = coordinates;
    }

}
