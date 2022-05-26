package Behaviour;

import Agents.TrafficLight;
import Utils.Utils;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeTLColor extends SimpleBehaviour {

    private TrafficLight trafficLight;
    // true if this behaviour is applied after an inform message processed in ListeningInform behaviour
    private boolean receiver;

    public ChangeTLColor(TrafficLight trafficLight, boolean receiver) {
        super(trafficLight);

        this.trafficLight = trafficLight;
        this.receiver = receiver;
    }

    @Override
    public void action() {
        // Reset elapsed time
        trafficLight.setElapsedTime(0);
        // Change color attribute in trafficLight
        trafficLight.changeColor();

        if (trafficLight.isInitiator()) { // Send message to world
            ArrayList<AID> receiver_list = new ArrayList<>();

            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Alternate color");
            message.put("InterId", String.valueOf(this.trafficLight.getIntersectionId()));
            receiver_list.add(trafficLight.getWorldAID());

            // Check if there is any parallel traffic light and if any add the parallel traffic light to receiver list
            char TLOrientation = trafficLight.getOrientation();
            if ((TLOrientation == 'W' || TLOrientation == 'E') && trafficLight.getIntersectionId() == 1)
                receiver_list.add(trafficLight.getTLAIDFromDF(1, TLOrientation == 'W' ? 'E' : 'W'));

            this.trafficLight.send(Utils.getACLMessage(message,
                    receiver_list,
                    ACLMessage.INFORM));
        } else if (trafficLight.getColor() == 'g' && !receiver) {
            char TLOrientation = trafficLight.getOrientation();
            if ((TLOrientation == 'W' || TLOrientation == 'E') && trafficLight.getIntersectionId() == 1) {
                HashMap<String, String> message = new HashMap<>();
                message.put("MsgType", "Alternate color");
                message.put("InterId", String.valueOf(this.trafficLight.getIntersectionId()));
                trafficLight.send(Utils.getACLMessage(message,
                        trafficLight.getTLAIDFromDF(1, TLOrientation == 'W' ? 'E' : 'W'),
                        ACLMessage.INFORM));
            }
        }
    }

    @Override
    public boolean done() {
        return true;
    }
}
