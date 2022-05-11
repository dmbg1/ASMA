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


    // Randomly chooses lane to go to after intersection (f, l, r)
    public Lane chooseLaneToGoTo(ArrayList<Lane> laneWays) {
        Random r = new Random();
        Lane laneToGoTo = laneWays.get(r.nextInt(laneWays.size()));
        return laneToGoTo;
    }

    // Updates vehicles positions in a lane
    // Possible turns
    // If vehicle can go forward in position 0 it verifies if the lane it turns to is free
    public void updateVehiclesInLane(ArrayList<Lane> laneWays, int intersection_id) {
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                Lane laneToGoTo = chooseLaneToGoTo(laneWays);
                if (trafficLight.getColor() == 'g' && laneVehicles.get(i)) {
                    if (laneToGoTo == null) {
                        for (Lane lane : lanesInter1.values()) {
                            char laneOrientation = lane.getOrientation();
                            // Possible lane ways in intersection
                            ArrayList<Lane> laneWays = new ArrayList<>();

                            if (laneOrientation == 'N' || lane.getOrientation() == 'W') {
                                // Right on intersection when lane orientation is W and front when lane orientation is N
                                laneWays.add(lanesInter2.get('N'));
                                // Front on intersection when lane orientation is W and front when lane orientation is N
                                // null because there will always be available space in the lane
                                laneWays.add(null);
                            } else if (laneOrientation == 'E')
                                // Front on intersection when lane orientation is E
                                laneWays.add(null);

                            lane.updateVehiclesInLane(laneWays, world.getIntersection1().getId());
                        }
                        laneVehicles.set(i, false);
                        System.out.println("Vehicle entered intersection " + intersection_id + " from lane with " +
                                "orientation " + orientation);
                        continue;
                    }

                    if (!laneToGoTo.getLaneVehicles().get(7)) {
                        laneToGoTo.addCarToLane();
                        laneVehicles.set(i, false);
                        System.out.println("Vehicle entered intersection " + intersection_id + " from lane with " +
                                "orientation " + orientation);
                    }
                }
            } else {
                if (!laneVehicles.get(i - 1) && laneVehicles.get(i)) {
                    laneVehicles.set(i - 1, true);
                    laneVehicles.set(i, false);
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

}