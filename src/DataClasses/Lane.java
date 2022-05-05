package DataClasses;

import java.util.HashMap;
import java.util.Random;

public class Lane {
    // Size of hashmap equal to number of possible positions in lane
    // Hash map that tells if position is occupied by vehicle or not
    private HashMap<Integer, Boolean> laneVehicles = new HashMap<>();
    private char orientation;
    private TrafficLight trafficLight;
    private int probGenerateLane;

    public Lane(char orientation, TrafficLight trafficLight, int probGenerateLane) {
        generateLaneVehicles();
        this.orientation = orientation;
        this.trafficLight = trafficLight;
        this.probGenerateLane = probGenerateLane;
    }

    // Randomly generates lane vehicles
    public void generateLaneVehicles() {
        Random r = new Random();
        // Each lane will have 8 different positions
        for (int i = 0; i < 8; i++)
            laneVehicles.put(i, r.nextBoolean());
    }

    // Adds car to final position in lane if not occupied
    public void addCarToLane() {
        if (!laneVehicles.get(7))
            laneVehicles.replace(7, true);
    }

    public int getProbGenerateLane() {
        return probGenerateLane;
    }

    public char getOrientation() {
        return orientation;
    }

    public HashMap<Integer, Boolean> getLaneVehicles() {
        return laneVehicles;
    }
}
