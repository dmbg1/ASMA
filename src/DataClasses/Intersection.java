package DataClasses;

import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateVehicles;
import DataClasses.Lane;
import DataClasses.TrafficLight;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Intersection {

    private ArrayList<Lane> lanes = new ArrayList<>();
    private int id;

    protected void Intersection(int id, HashMap<Character, char[]> orientations) {
        this.id = id;
        addLanesToIntersection(orientations);
    }

    public void addLanesToIntersection(HashMap<Character, char[]> orientations) {
        Random r = new Random();
        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';
        // Initial duration between 10 e 20 seconds
        int initialTrafLightDur = r.nextInt(11) + 10;

        // Add lanes to intersection
        // TODO Depois e preciso definir outros valores de orientacao.
        //TODO: Esta parte vai ter de ser feita na criacao dos agentes semaforos

        for (char orientation : orientations.keySet()) {

            TrafficLight trafficLight;
            if(orientation == 'N' || orientation == 'S')
                trafficLight = new TrafficLight(nsTrafficLightColor, initialTrafLightDur, orientation);
            else
                trafficLight = new TrafficLight(weTrafficLightColor, initialTrafLightDur, orientation);

            Lane lane = new Lane(orientation, trafficLight, r.nextInt(100));
            System.out.println(orientation + ": " + lane.getLaneVehicles());
            lanes.add(lane);
        }
        System.out.println("==============================================================================");

    }

    public void alternateTrafficLights(int duration) {
        for(Lane lane: lanes) {
            lane.getTrafficLight().alternateColor(duration);
        }
    }
    public void takeDown() {
        System.out.println(getLocalName() + ": done working.");
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }
}
