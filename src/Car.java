import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private double velocity; //TODO: Tlvs gerar velocidades diferentes para cada carro. Ter atencao ao comprimento da lane.

    private Car front_car;

    public Car(double acceleration, double deceleration, double position, double length, Car front_car, Lane lane) {

        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.position = position;
        this.length = length;
        this.front_car = front_car;
        this.velocity = 4.0;
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

    public double getPosition() {
        return position;
    }

    public boolean checkColisionFrontCar() {

        if (this.front_car != null) {
            return this.front_car.getPosition() <= (this.position -= this.velocity);
        }else {
          //Talvez para quando tivermos os semaforos a funcionar
        }

        return true;
    }

    public synchronized void update_position() {

        if(checkColisionFrontCar()) {
            this.position -= this.velocity;
        }
    }

    /*
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
        int value = 0;
        if(lane.getTrafficLight().getCurr_color() == 'g') {
            value = -99999999;
        }
        double front_car_position = front_car == null ? value : front_car.getPosition();
        // TODO solve race condition (solved?)
        return pos_after_stopping >= front_car_position &&
                pos_after_stopping <= front_car_position + max_distance;
    }
    */

    public void removeFromLane() {

        this.lane.getCars().remove(this);
    }

    @Override
    public void run() {

        while(this.position >= 0) {
            update_position();
            // TODO: Semaforo check
        }
        removeFromLane();
    }
}
