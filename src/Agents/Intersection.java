package Agents;

import Behaviour.GenerateVehicles;
import Behaviour.UpdateVehicles;
import DataClasses.Lane;
import DataClasses.TrafficLight;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Random;

public class Intersection extends Agent {

    ArrayList<Lane> lanes = new ArrayList<>();

    @Override
    protected void setup() {
        addLanesToIntersection();
        addBehaviour(new GenerateVehicles(this, 5000));
        addBehaviour(new UpdateVehicles(this, 1000));
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void addLanesToIntersection() {
        Random r = new Random();
        char[] orientations = {'N', 'S', 'E', 'W'};
        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';

        // Initial duration between 10 e 20 seconds
        int initialTrafLightDur = r.nextInt(11) + 10;

        // Add lanes to intersection
        // TODO Depois e preciso definir outros valores de orientacao.
        for (char orientation : orientations) {
            TrafficLight trafficLight;
            if(orientation == 'N' || orientation == 'S')
                trafficLight = new TrafficLight(nsTrafficLightColor, initialTrafLightDur);
            else
                trafficLight = new TrafficLight(weTrafficLightColor, initialTrafLightDur);

            Lane lane = new Lane(orientation, trafficLight, r.nextInt(100));
            System.out.println(orientation + ": " + lane.getLaneVehicles());
            lanes.add(lane);
        }
        System.out.println("==============================================================================");

    }

    public void alternateTrafficLights() {
        for(Lane lane: lanes) {
            lane.getTrafficLight().alternateColor();
        }
    }
    public void takeDown() {
        System.out.println(getLocalName() + ": done working.");
    }

}
