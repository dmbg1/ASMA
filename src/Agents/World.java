package Agents;

import Behaviour.UtilsToCalculateStatistics;
import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateWorld;
import Utils.Intersection;
import Utils.Lane;
import Utils.Utils;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;

import java.util.HashMap;
import java.util.Locale;

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
    private int timePassed = 0;

    public void setup() {
        Object[] agentArgs = getArguments();
        this.cc = (ContainerController) agentArgs[0];
        registerWorld();
        generateWorld();

        intersection1.logIntersection();
        intersection2.logIntersection();
        System.out.println("==============================================================================");
        addBehaviour(new UpdateWorld(this, 1000));
        addBehaviour(new GenerateVehicles(this, 3000));
        addBehaviour(new ListeningInform(this));
        addBehaviour(new UtilsToCalculateStatistics(this, 1000));
    }

    @Override
    protected void takeDown() {

        int totalCarsLeaving = 0;

        System.out.println("*********Intersection1*********");
        for(Lane lane: this.intersection1.getLanes().values()) {
            totalCarsLeaving += lane.getNumCarsLeaving();
            System.out.println("\t======" + lane.getTrafficLight().getNameId().toUpperCase(Locale.ROOT) + "======");
            System.out.println("\tTrafficLight Active Green Time: " + " " + lane.getGreenTime() + " s");
            System.out.println("\tTrafficLight Active Red Time: " + " " + lane.getRedTime() + " s");
            System.out.println("\t>Vehicles Average Waiting time: " + " " + lane.getRedTime()/lane.getNumTimesOnRed() + " s");
            System.out.println("\t>Flow of vehicles: " + lane.getTotalNumCarsPassedInLane() * 60 / (float)this.timePassed + " vehicles/min");
        }
        System.out.println("\t===============");
        System.out.println(">Flow in intersection: " + totalCarsLeaving * 60/ (float)this.timePassed + " vehicles/min\n");

        totalCarsLeaving = 0;
        System.out.println("************Intersection2************");
        for(Lane lane: this.intersection2.getLanes().values()) {
            totalCarsLeaving += lane.getNumCarsLeaving();
            System.out.println("\t======" + lane.getTrafficLight().getNameId().toUpperCase(Locale.ROOT) + "======");
            System.out.println("\tTrafficLight Active Green Time: " + lane.getTrafficLight().getNameId() + " " + lane.getGreenTime() + " s");
            System.out.println("\tTrafficLight Active Red Time: " + lane.getTrafficLight().getNameId() + " " + lane.getRedTime() + " s");
            System.out.println("\t>Vehicles Average Waiting time: " + lane.getTrafficLight().getNameId() + " " + lane.getRedTime()/lane.getNumTimesOnRed() + " s");
            System.out.println("\t>Flow of vehicles: " + lane.getTotalNumCarsPassedInLane() * 60 / (float)this.timePassed + " vehicles/min");
        }
        System.out.println(">Flow in intersection: " + totalCarsLeaving * 60/ (float)this.timePassed + " vehicles/min");

        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
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

    public void incrementTimePassed() {
        this.timePassed++;
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
