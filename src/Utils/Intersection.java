package Utils;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.*;

public class Intersection {

    private HashMap<Character, Lane> lanes = new HashMap<>();
    private int id;
    private int numVehiclesPassed = 0;

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
            lanes.put(orientation, lane);

            if(trafficLight.getColor() == 'g') {
                generateTrafficLightAgent(cc, trafficLight.getColor(), orientation,
                        false, lane.getTrafficLight().getNameId(),
                        lane.getNumCars(), lane.proximityToTheTrafficLight());
                continue;
            }

            generateTrafficLightAgent(cc, trafficLight.getColor(), orientation,
                    initiator, lane.getTrafficLight().getNameId(),
                    lane.getNumCars(), lane.proximityToTheTrafficLight());
            // Only one red traffic light can be initiator in one intersection
            if (initiator) initiator = false;
        }
    }

    public void generateTrafficLightAgent(ContainerController cc, char color, char ori, boolean initiator,
                                          String nameId, int numCars, int closestCar) {
        Object[] agentArgs = new Object[7];
        agentArgs[0] = color;
        agentArgs[1] = ori;
        agentArgs[2] = initiator;
        agentArgs[3] = id;
        agentArgs[4] = numCars;
        agentArgs[5] = closestCar;

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
            System.out.println((laneTrafficLight.getColor() != 'r' ? Utils.green : Utils.red) + "■ " + Utils.reset +
                    lane.getOrientation() +"[" + lane.getTrafficLight().getNameId().toUpperCase(Locale.ROOT) + "]: "
                    + lane.getLaneVehicles());
        }
    }

    public int getId() {
        return id;
    }

    public void changeTrafficLightsColor() {
        //System.out.println("Alternate intersection " + id + " traffic lights color");
        for(Lane lane: this.lanes.values())
            lane.alternateColorTrafficLight();
    }
}
