import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Lane extends Thread{

    private JSONObject JSONlane = new JSONObject();
    private ArrayList<Car> cars = new ArrayList<>();
    private JSONArray JSONCars = new JSONArray();
    private TrafficLight trafficLight;
    private char orientation;
    private int probGenerateCars;
    private Car frontCar;

    public Lane(char orientation, TrafficLight trafficLight, int probGenerateCars) {

        this.orientation = orientation;
        this.trafficLight = trafficLight;
        this.probGenerateCars = probGenerateCars;
        JSONlane.put("orientation", Character.toString(this.orientation));
        JSONlane.put("cars", this.JSONCars);
        JSONlane.put("traffic_light", this.trafficLight.getJSONTrafficLight());
        JSONlane.put("probGenerateCars", this.probGenerateCars);
    }

    public JSONObject getJSONLane() {
        return JSONlane;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public char getOrientation() {
        return orientation;
    }

    public void setFrontCar(Car frontCar) {
        this.frontCar = frontCar;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void addCar(Car car) {

        this.cars.add(car);
        this.JSONCars.put(car.getJSONCar());
    }

    private synchronized void generateCars(Lane lane) {

        Timer t = new Timer();

        t.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {

                        Random r = new Random();

                        if(r.nextInt(1, 101) <= lane.probGenerateCars) {
                            double acceleration = (2.5 + r.nextDouble() * 2.5);
                            double deceleration = acceleration + 2;
                            double length =  (4 + r.nextDouble() * 1);

                            Car front_car = lane.getCars().size() == 0 ? null : lane.getCars().get(lane.getCars().size() - 1);
                            Car car = new Car(acceleration, deceleration, 50, length, front_car, lane); //TODO: gerar posicao. provavelmente o tamanho da lane

                            lane.addCar(car);
                            car.start();
                        }
                    }
                }, 0,1000
        );
    }

    public synchronized void frontCarUpdate() {

        if(this.cars.size() > 0) {
            this.frontCar = cars.get(0);
        }
    }

    @Override
    public void run() { //TODO: Nao para por causa da lane

        // TODO: Usar esta thread para gerar carros na lane tlvz

        generateCars(this);

        while(true) {
            frontCarUpdate();
            if(this.getId() == 16 && this.getTrafficLight().getCurr_color() == 'g') {
                //System.out.println(this.frontCar.getId() + " " + this.frontCar.getPosition());
            }
        }
    }
}
