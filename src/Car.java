import org.json.JSONObject;

public class Car {

    private JSONObject JSONcar = new JSONObject();
    private float acceleration;
    private float deceleration;
    private float position;

    public Car(float acceleration, float deceleration, float position) {

        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.position = position;

        JSONcar.put("acceleration", acceleration);
        JSONcar.put("deceleration", deceleration);
        JSONcar.put("position", position);

    }

    public JSONObject getJSONcar() {
        return JSONcar;
    }
}
