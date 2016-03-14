package pl.parkujznami.parkujpl_mobile.models.parkandride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Datum {

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("properties")
    @Expose
    private List<Property> properties = new ArrayList<Property>();

    /**
     * @return The geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry The geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return The properties
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * @param properties The properties
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
