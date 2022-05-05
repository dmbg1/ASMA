import jade.core.Agent;
import org.json.JSONObject;

public class TrafficLight extends Agent {

    private JSONObject JSONTrafficLight = new JSONObject();
    private char curr_color;
    private int curr_duration;
    private int orientation;

    public void setup() {

        JSONTrafficLight.put("color", Character.toString(this.curr_color));
        JSONTrafficLight.put("default_duration", curr_duration);

        System.out.println("Traffic Light Setup!");

        // TODO Negotiate green light duration (20 seg total soma do tempo em que os semaforos estao verdes) with other traffic light
        // according to amount (and flow maybe) of cars in lane

        // TODO Delaying sequential actions for traffic light color alternation (after each negotiation?)
    }

    public void setCurr_color(char curr_color) {
        this.curr_color = curr_color;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public JSONObject getJSONTrafficLight() {
        return JSONTrafficLight;
    }

    public double getCurr_duration() {
        return curr_duration;
    }

    public char getCurr_color() {
        return curr_color;
    }

    public int getOrientation() {
        return orientation;
    }

    public void change_color() {
        curr_color = curr_color == 'g' ? 'r' : 'g';
    }
}
