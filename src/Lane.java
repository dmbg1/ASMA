import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Lane {

    private JSONObject JSONlane = new JSONObject();
    private ArrayList<Car> cars = new ArrayList<>();
    private JSONArray JSONCars = new JSONArray();
    private String orientation;
    private float velMax;

    public Lane(String orientation, float velMax) {

        this.orientation = orientation;
        this.velMax = velMax;
        JSONlane.put("maxVelocity", velMax);
        JSONlane.put("orientation", this.orientation);
        JSONlane.put("cars", this.JSONCars);
    }

    public JSONObject getJSONlane() {
        return JSONlane;
    }

    public void addCar(Car car) {

        this.cars.add(car);
        this.JSONCars.put(car.getJSONcar());
    }
}
