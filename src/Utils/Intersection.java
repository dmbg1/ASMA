package Utils;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Intersection {

    private HashMap<Character, Lane> lanes = new HashMap<>();
    private int id;

    public Intersection(int id) {
        this.id = id;
    }

    public void addLanesToIntersection(char[] laneOrientations, ContainerController cc) {
        Random r = new Random();

        // Choose initial traffic light attributes randomly (color, duration)
        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';
        //int initialTrafLightElapsedTime = r.nextInt(11) + 10;

        // Variable tells if traffic light agent is responsible for communicating with world when alternating color
        boolean initiator = true;

        for (char orientation : laneOrientations) {
            TrafficLight trafficLight;
            if(orientation == 'N' || orientation == 'S') {
                trafficLight = new TrafficLight(nsTrafficLightColor, orientation);
            }
            else
                trafficLight = new TrafficLight(weTrafficLightColor, orientation);

            Lane lane = new Lane(orientation, trafficLight, r.nextInt(100));

            generateTrafficLightAgent(cc, trafficLight.getColor(), orientation,
                    initiator, lane.getTrafficLight().getNameId());
            // Only one traffic light can be initiator in one intersection
            if (initiator) initiator = false;

            lanes.put(orientation, lane);
        }
        logIntersection();
        System.out.println("==============================================================================");
    }

    public void generateTrafficLightAgent(ContainerController cc, char color, char ori, boolean initiator, String nameId) {
        Object[] agentArgs = new Object[5];
        agentArgs[0] = color;
        agentArgs[1] = ori;
        agentArgs[2] = initiator;
        agentArgs[3] = id;

        AgentController agentController;
        try {
            agentController = cc.createNewAgent(nameId, "Agents.TrafficLight", agentArgs);
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Character, Lane> getLanes() {
        return lanes;
    }

    public void logIntersection() {
        ArrayList<Lane> lanes =  new ArrayList<>(this.lanes.values());
        System.out.println("Intersection " + id);
        for(Lane lane: lanes) {
            TrafficLight laneTrafficLight = lane.getTrafficLight();
            System.out.println(laneTrafficLight.getColor() + "[" + "]: " +
                    lane.getLaneVehicles());
        }
    }

    public int getId() {
        return id;
    }

    public void changeTrafficLightsColor() {
        System.out.println("Alternate intersection " + id + " traffic lights color");
        for(Lane lane: this.lanes.values())
            lane.alternateColorTrafficLight();
    }
}
