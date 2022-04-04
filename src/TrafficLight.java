import org.json.JSONObject;

public class TrafficLight {
    private JSONObject JSONTrafficLight = new JSONObject();
    private char curr_color;
    private double curr_duration;

    TrafficLight(char color, double curr_duration) {
        this.curr_color = color;
        this.curr_duration = curr_duration;

        JSONTrafficLight.put("color", Character.toString(color));
        JSONTrafficLight.put("default_duration", curr_duration);
    }

    public JSONObject getJSONTrafficLight() {
        return JSONTrafficLight;
    }
}
