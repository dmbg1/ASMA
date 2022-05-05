package DataClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Lane {
    // Size of array list equal to number of possible positions in lane
    // Array list that tells if position (index) is occupied by vehicle or not
    private ArrayList<Boolean> laneVehicles = new ArrayList<>();
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
            laneVehicles.add(i, r.nextBoolean());
    }

    // Adds car to final position in lane if not occupied
    public void addCarToLane() {
        if (!laneVehicles.get(7))
            laneVehicles.set(7, true);
    }

    public int getProbGenerateLane() {
        return probGenerateLane;
    }

    public char getOrientation() {
        return orientation;
    }

    public ArrayList<Boolean> getLaneVehicles() {
        return laneVehicles;
    }

    public void moveVehiclesForward() {
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                // TODO Verificar semaforo
            }
            else {
                if(!laneVehicles.get(i - 1) && laneVehicles.get(i)) {
                    laneVehicles.set(i - 1, true);
                    laneVehicles.set(i, false);
                }
            }
        }
    }
}
