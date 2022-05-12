package Utils;

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
    private int numCars = 0;

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
        for (int i = 0; i < 8; i++) {
            if (r.nextBoolean()) {
                this.numCars++;
                laneVehicles.add(i, true);
            }else {
                laneVehicles.add(i, false);
            }
        }
    }

    // Adds car to final position in lane if not occupied
    public void addCarToLane() {
        if (!laneVehicles.get(7)) {
            this.numCars++;
            laneVehicles.set(7, true);
        }
    }

    // Randomly chooses lane to go to after intersection (f, l, r)
    public Lane chooseLaneToGoTo(ArrayList<Lane> laneWays) {
        Random r = new Random();
        Lane laneToGoTo = laneWays.get(r.nextInt(laneWays.size()));
        return laneToGoTo;
    }

    // Updates vehicles positions in a lane
    // Possible turns
    // If vehicle can go forward in position 0 it verifies if the lane it turns to is free
    public void updateVehiclesInLane(ArrayList<Lane> laneWays, int intersectionId) {
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                Lane laneToGoTo = chooseLaneToGoTo(laneWays);
                if (trafficLight.getColor() == 'g' && laneVehicles.get(i)) {
                    if(laneToGoTo == null) {
                        laneVehicles.set(i, false);
                        this.numCars--;
                        System.out.println("Vehicle entered intersection " + intersectionId + " from lane with " +
                                "orientation " + orientation);
                        continue;
                    }
                    if (!laneToGoTo.getLaneVehicles().get(7)) {
                        laneToGoTo.addCarToLane();
                        laneVehicles.set(i, false);
                        this.numCars--;
                        System.out.println("Vehicle entered intersection " + intersectionId + " from lane with " +
                                "orientation " + orientation);
                    }
                }
            } else {
                if (!laneVehicles.get(i - 1) && laneVehicles.get(i)) {
                    laneVehicles.set(i - 1, true);
                    laneVehicles.set(i, false);
                    this.numCars--;
                }
            }
        }
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

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public int getNumCars() {
        return numCars;
    }

    public void alternateColorTrafficLight(int duration) {
        this.trafficLight.alternateColor(duration);
    }

}
