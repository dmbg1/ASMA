import org.json.JSONObject;

public class TrafficLight {
    private JSONObject JSONTrafficLight = new JSONObject();
    private char curr_color;
    private int curr_duration;

    TrafficLight(char color, int curr_duration) {
        this.curr_color = color;
        this.curr_duration = curr_duration;

        JSONTrafficLight.put("color", Character.toString(color));
        JSONTrafficLight.put("default_duration", curr_duration);
    }

    public JSONObject getJSONTrafficLight() {
        return JSONTrafficLight;
    }

    public void change_color() {
        curr_color = curr_color == 'g' ? 'r' : 'g';
    }
    public double getCurr_duration() {
        return curr_duration;
    }

    public char getCurr_color() {
        return curr_color;
    }
}
