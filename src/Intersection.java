import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Intersection {

    private JSONObject JSONintersection = new JSONObject();
    private int id;
    private float width;
    private ArrayList<Lane> lanes = new ArrayList<>();
    private JSONArray JSONlanes = new JSONArray();

    public Intersection(int id, float width) {

        this.id = id;
        this.width = width;

        JSONintersection.put("id", id);
        JSONintersection.put("width", width);
        JSONintersection.put("lanes", JSONlanes);

    }

    public void addLane(Lane lane) {

        this.lanes.add(lane);
        this.JSONlanes.put(lane.getJSONlane());
    }

    public JSONObject getJSONintersection() {
        return JSONintersection;
    }
}
