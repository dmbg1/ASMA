package Agents;

import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateWorld;
import Utils.Intersection;
import Utils.Lane;
import Utils.Utils;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;

import java.io.IOException;
import java.util.HashMap;

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
        // Guarantee world generation finished
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        intersection1.logIntersection();
        intersection2.logIntersection();
        System.out.println("==============================================================================");
        addBehaviour(new UpdateWorld(this, 1000));
        addBehaviour(new GenerateVehicles(this, 5000));
        addBehaviour(new ListeningInform(this));
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
