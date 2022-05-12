package Agents;

import Behaviour.CountdownWorldTrafficLights;
import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateVehicles;
import Utils.Intersection;
import Utils.Lane;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
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

    private Intersection intersection1;
    private Intersection intersection2;

    public void setup() {
        generateWorld();
        addBehaviour(new GenerateVehicles(this, 5000));
        addBehaviour(new UpdateVehicles(this, 1000));
        addBehaviour(new ListeningInform(this));
        addBehaviour(new CountdownWorldTrafficLights(this, 1000));
    }

    private void generateWorld() {
        intersection1 = new Intersection(1);
        intersection2 = new Intersection(2);
    }

    public Intersection getIntersection1() {
        return intersection1;
    }

    public Intersection getIntersection2() {
        return intersection2;
    }

    public void changeColorTrafficLight(int duration, int intersectionId) {

        if(intersectionId == 1)
            intersection1.changeTrafficLightsColor(duration);
        else
            intersection2.changeTrafficLightsColor(duration);
    }

    public void informTLNumCars() {

        for(Lane lane: this.intersection1.getLanes().values()) {
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Inform num cars");
            message.put("numCars", String.valueOf(lane.getNumCars()));
            sendMessage(message, lane.getTrafficLight().getNameId(), ACLMessage.INFORM);
        }

        for(Lane lane: this.intersection2.getLanes().values()) {
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Inform num cars");
            message.put("numCars", String.valueOf(lane.getNumCars()));
            sendMessage(message, lane.getTrafficLight().getNameId(), ACLMessage.INFORM);
        }
    }

    public void sendMessage(HashMap<String, String> message, String receiver, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        send(msg);
    }
}
