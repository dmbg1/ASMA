import org.json.JSONObject;

public class Car {

    private JSONObject JSONcar = new JSONObject();
    private double acceleration;
    private double deceleration;
    private double position;
    private double length;

    private double velocity;

    public Car(double acceleration, double deceleration, double position, double length) {

        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.position = position;
        this.length = length;
        this.velocity = 0;

        JSONcar.put("acceleration", acceleration);
        JSONcar.put("deceleration", deceleration);
        JSONcar.put("position", position);
        JSONcar.put("length", length);

    }

    public JSONObject getJSONCar() {
        return JSONcar;
    }

    public void goForward() {

        this.velocity += this.acceleration;
        this.position -= this.velocity;
    }

    public void stopping() {

    }

    public boolean collision(Car car) { //detetar colisao entre carros na mesma lane.

        return false;
    }
}
