package Agents;

import Behaviour.UtilsToCalculateStatistics;
import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateWorld;
import Utils.Intersection;
import Utils.Lane;
import Utils.Utils;
import Utils.Pair;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class World extends Agent {
    /*
   World drawing
   * * * * * * * * I2 * * * * * * * * <-- Possible ways: front in intersection
                   *
                   * <-- Possible ways: left in intersection
                   *
                   *
                   *
                   *
                   *    Ʌ
                   *  <-|
   * * * * * * * * I1 * * * * * * * *
              -->  * Ʌ
                   * |->
                   *
                   *
                   *
                   *
                   *
                   *
   Where
   * -> Position vehicle can occupy
   I1 -> Intersection 1
   I2 -> Intersection 2
   */

    private ContainerController cc;
    private Intersection intersection1;
    private Intersection intersection2;

    public void setup() {
        Object[] agentArgs = getArguments();
        this.cc = (ContainerController) agentArgs[0];
        registerWorld();
        generateWorld();

        intersection1.logIntersection();
        intersection2.logIntersection();
        System.out.println("==============================================================================");
        addBehaviour(new UpdateWorld(this, 1000));
        addBehaviour(new GenerateVehicles(this, 5000));
        addBehaviour(new ListeningInform(this));
        addBehaviour(new UtilsToCalculateStatistics(this, 1000));
    }

    @Override
    protected void takeDown() {

        ArrayList<Pair<Double, Integer>> times = totalRedAndGreenTime();
        double averageWaitingTime = times.get(0).getNum1() / times.get(0).getNum2();
        double totalRedWaitingTime = times.get(0).getNum1();
        double totalGreenWaitingTime = times.get(1).getNum1();

        System.out.println("TtafficLight Active Green Time: " + totalRedWaitingTime);
        System.out.println("TtafficLight Active Red Time: " + totalGreenWaitingTime);
        System.out.println("Vehicles Average Waiting time: " + averageWaitingTime);

        System.out.println("Flow of vehicles: ");
        System.out.println("Density of vehicles: ");

        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /*
    public double calculateAverageWaitingTime() {

        double sum = 0.0;
        int numTimesOnRed = 0;

        for(Lane lane: this.intersection1.getLanes().values()) {
            sum += lane.getRedTime();
            numTimesOnRed += lane.getNumTimesOnRed();
        }

        for(Lane lane: this.intersection2.getLanes().values()) {
            sum += lane.getRedTime();
            numTimesOnRed += lane.getNumTimesOnRed();
        }

        return sum/numTimesOnRed;
    }
    */

    public ArrayList<Pair<Double, Integer>> totalRedAndGreenTime() {

        double redTime = 0.0;
        int numRed = 0;
        double greenTime = 0.0;
        int numGreen = 0;
        ArrayList<Pair<Double, Integer>> times = new ArrayList<>();

        for(Lane lane: this.intersection1.getLanes().values()) {
            redTime += lane.getRedTime();
            numRed += lane.getNumTimesOnRed();
        }

        for(Lane lane: this.intersection2.getLanes().values()) {
            redTime += lane.getRedTime();
            numRed += lane.getNumTimesOnRed();
        }

        Pair<Double, Integer> p1 = new Pair<>(redTime, numRed);

        for(Lane lane: this.intersection1.getLanes().values()) {
            greenTime += lane.getGreenTime();
            numGreen += lane.getNumTimesOnGreen();
        }

        for(Lane lane: this.intersection2.getLanes().values()) {
            greenTime += lane.getGreenTime();
            numGreen += lane.getNumTimesOnGreen();
        }

        Pair<Double, Integer> p2 = new Pair<>(greenTime, numGreen);
        times.add(p1);
        times.add(p2);

        return times;
    }

    private void generateWorld() {
        intersection1 = new Intersection(1);
        intersection1.addLanesToIntersection(new char[]{'N', 'E', 'W'}, cc);
        intersection2 = new Intersection(2);
        intersection2.addLanesToIntersection(new char[]{'N', 'W'}, cc);
    }

    public Intersection getIntersection1() {
        return intersection1;
    }

    public Intersection getIntersection2() {
        return intersection2;
    }

    public void changeColorTrafficLight(int intersectionId) {

        if(intersectionId == 1)
            intersection1.changeTrafficLightsColor();
        else
            intersection2.changeTrafficLightsColor();
    }

    public void informTLNumCars() {

        for(Lane lane: this.intersection1.getLanes().values()) {
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Inform Lane State");
            message.put("numCars", String.valueOf(lane.getNumCars()));
            message.put("closestCarDistance", String.valueOf(lane.proximityToTheTrafficLight()));
            if(lane.getOrientation() == 'W') {
                Lane parallelLane = intersection1.getLanes().get('E');
                message.put("parallelNumCars", String.valueOf(parallelLane.getNumCars()));
                message.put("parallelClosestCarDistance", String.valueOf(parallelLane.proximityToTheTrafficLight()));
            }
            else if(lane.getOrientation() == 'E') {
                Lane parallelLane = intersection1.getLanes().get('W');
                message.put("parallelNumCars", String.valueOf(parallelLane.getNumCars()));
                message.put("parallelClosestCarDistance", String.valueOf(parallelLane.proximityToTheTrafficLight()));
            }

            send(Utils.getACLMessage(message, lane.getTrafficLight().getNameId(), ACLMessage.INFORM));
        }

        for(Lane lane: this.intersection2.getLanes().values()) {
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Inform Lane State");
            message.put("numCars", String.valueOf(lane.getNumCars()));
            message.put("closestCarDistance", String.valueOf(lane.proximityToTheTrafficLight()));
            send(Utils.getACLMessage(message, lane.getTrafficLight().getNameId(), ACLMessage.INFORM));
        }
    }

    public void registerWorld() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("World");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println("World registered in DF successfully");
        } catch(FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
