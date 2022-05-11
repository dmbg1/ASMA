package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Intersection {

    private HashMap<Character, Lane> lanes = new HashMap<>();
    private int id;

    public Intersection(int id) {
        this.id = id;
        addLanesToIntersection();
    }

    public void addLanesToIntersection() {
        Random r = new Random();

        char[] orientations = null;
        if (this.id == 1)
            orientations = new char[]{'N', 'E', 'W'};
        else if (this.id == 2)
            orientations = new char[]{'N', 'W'};

        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';
        // Initial duration between 10 e 20 seconds
        int initialTrafLightDur = r.nextInt(11) + 10;

        for (char orientation : orientations) {
            TrafficLight trafficLight;
            if(orientation == 'N' || orientation == 'S') {
                trafficLight = new TrafficLight(nsTrafficLightColor, initialTrafLightDur, orientation);
            }
            else
                trafficLight = new TrafficLight(weTrafficLightColor, initialTrafLightDur, orientation);

            Lane lane = new Lane(orientation, trafficLight, r.nextInt(100));
            lanes.put(orientation, lane);
        }
        logIntersection();
        System.out.println("==============================================================================");
    }

    public HashMap<Character, Lane> getLanes() {
        return lanes;
    }

    public void logIntersection() {
        ArrayList<Lane> lanes =  new ArrayList<>(this.lanes.values());
        System.out.println("Intersection " + id);
        for(Lane lane: lanes)
            System.out.println(lane.getTrafficLight().getColor() + ": " + lane.getLaneVehicles());
    }

    public int getId() {
        return id;
    }

    public void changeTrafficLightsColor(String nameId, int duration) {

        for(Lane lane: this.lanes.values()) {
            System.out.println("Alternate color " + nameId);
            if(Objects.equals(lane.getTrafficLight().getNameId(), nameId)) {
                lane.alternateColorTrafficLight(duration);
            }
        }
    }
}
