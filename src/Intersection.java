import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Intersection {

    private JSONObject JSONIntersection = new JSONObject();
    private int id;
    private float width;
    private float vMax;
    private ArrayList<Lane> lanes = new ArrayList<>();
    private JSONArray JSONLanes = new JSONArray();

    public Intersection(int id, float width, float vMax) {
        this.vMax = vMax;
        this.id = id;
        this.width = width;
        JSONIntersection.put("id", id);
        JSONIntersection.put("max_vel", vMax);
        JSONIntersection.put("width", width);
        JSONIntersection.put("lanes", JSONLanes);
    }

    public void addLane(Lane lane) {
        this.lanes.add(lane);
        this.JSONLanes.put(lane.getJSONlane());
    }

    public JSONObject getJSONIntersection() {
        return JSONIntersection;
    }
}
