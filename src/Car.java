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

    public Car getFront_car() {
        return front_car;
    }

    public void setFront_car(Car front_car) {
        this.front_car = front_car;
    }

    public synchronized boolean checkColisionFrontCar() {

        if (this.front_car != null) {
            return this.front_car.getPosition() + 1 <= this.position;
        }else { // Somaforo
          return this.lane.getTrafficLight().getCurr_color() == 'g';
        }
    }

    public synchronized void update_position() {

        if(checkColisionFrontCar()) {
            this.position -= this.velocity / 60;
        }
    }

    public void removeFromLane() {

        this.lane.getCars().remove(this);
    }

    public void removeFrontCar() {

        if(this.lane.getCars().indexOf(this) == 0 && this.front_car != null) front_car = null;
    }

    @Override
    public void run() {

        while(this.position >= 0) {
            removeFrontCar();
            update_position();
            // TODO: Semaforo check
            try {
                sleep(1000 / 60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        removeFromLane();
    }
}
