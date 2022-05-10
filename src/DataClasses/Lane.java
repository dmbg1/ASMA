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
    // Possible directions vehicle can take on intersection (either r for right or f for front)
    private char[] directions;

    public Lane(char orientation, TrafficLight trafficLight, int probGenerateLane, Intersection intersection) {
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

    public boolean can_turn_right() {
        for (char direction : directions) {
            if(direction == 'r')
                return true;
        }
        return false;
    }

    public void updateVehicles() {
        Random r = new Random();
        String
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                if (this.trafficLight.getColor() == 'g' && laneVehicles.get(i)) {
                    if(can_turn_right()) {
                        if () {
                            //TODO Comunicar com outra interseção para verificar
                        } else { // Em frente
                            laneVehicles.set(i, false);
                            System.out.println("Vehicle entered intersection");
                        }
                    }
                    else {
                        if () {// Verificar lane da frente
                            laneVehicles.set(i, false);
                            System.out.println("Vehicle entered intersection");
                        }
                    }
                }
            }
            else {
                if(!laneVehicles.get(i - 1) && laneVehicles.get(i)) {
                    laneVehicles.set(i - 1, true);
                    laneVehicles.set(i, false);
                }
            }
        }
    }

    public Lane getFrontLane() {
        if
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
