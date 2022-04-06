import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Lane {

    private JSONObject JSONlane = new JSONObject();
    private ArrayList<Car> cars = new ArrayList<>();
    private JSONArray JSONCars = new JSONArray();
    private TrafficLight trafficLight;
    private char orientation;

    public Lane(char orientation, TrafficLight trafficLight) {

        this.orientation = orientation;
        this.trafficLight = trafficLight;
        JSONlane.put("orientation", Character.toString(this.orientation));
        JSONlane.put("cars", this.JSONCars);
        JSONlane.put("traffic_light", this.trafficLight.getJSONTrafficLight());
    }

    public JSONObject getJSONLane() {
        return JSONlane;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {

        this.cars.add(car);
        this.JSONCars.put(car.getJSONCar());
    }
}
