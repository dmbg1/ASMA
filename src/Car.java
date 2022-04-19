import org.json.JSONObject;

public class Car extends Thread {

    private boolean forward;
    private JSONObject JSONcar = new JSONObject();
    private double acceleration;
    private double deceleration;
    private double position;
    private double length;
    private static int count = 0;
    private int id;
    private Lane lane;

    private double velocity;

    private Car front_car;

    public Car(double acceleration, double deceleration, double position, double length, Car front_car, Lane lane) {

        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.position = position;
        this.length = length;
        this.front_car = front_car;
        this.velocity = 0;
        this.id = ++count;
        this.lane = lane;

        JSONcar.put("acceleration", acceleration);
        JSONcar.put("deceleration", deceleration);
        JSONcar.put("position", position);
        JSONcar.put("length", length);

    }

    public JSONObject getJSONCar() {
        return JSONcar;
    }

    public void update_position() {
        if(this.id == 1)
            System.out.println(this.velocity);
        if(forward)
            this.velocity += this.acceleration;
        else
            this.velocity = this.velocity <= 0 ? 0 : this.velocity - this.deceleration;

        this.position -= this.velocity;
    }

    public double stopping_distance() {
        double distance = 0;
        double aux_velocity = this.velocity;

        while(aux_velocity > 0) {
            aux_velocity -= this.deceleration;
            // velocidade não pode ser inferior a zero
            if(aux_velocity <= 0 ) aux_velocity = 0;
            distance += this.velocity;
        }

        return distance;
    }


    public synchronized boolean check_security_position(double max_distance) { //detetar colisao entre carros na mesma lane.
        double pos_after_stopping = this.position - this.length - stopping_distance();
        // TODO when green the position in front should be ~~ -inf and 0 if red so the car stops (maybe introduce yellow light)
        double front_car_position = front_car == null ? -9999 : front_car.getPosition();
        // TODO solve race condition (solved?)
        return pos_after_stopping >= front_car_position &&
                pos_after_stopping <= front_car_position + max_distance;
    }

    public double getPosition() {
        return position;
    }

    @Override
    public void run() {
        while(this.position >= 0) {
            if(check_security_position(1.5))
                forward = false;
            else
                if (forward == false) forward = true;
            if(this.id == 1)
                System.out.println(this.id + " "+ this.lane.getId()+" car position: " + this.position + "\n" + "car forwardstate: " + this.forward);
            update_position();
            // TODO Semaforo check

        }

    }
}
