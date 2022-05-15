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

    public ChangeTLColor(TrafficLight trafficLight) {
        super(trafficLight);

        this.trafficLight = trafficLight;
    }

    @Override
    public void action() {
        this.trafficLight.setElapsedTime(0);
        this.trafficLight.changeColor();
        if (trafficLight.isInitiator()) {
            ArrayList<AID> aux_list = new ArrayList<>();
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Alternate color");
            message.put("InterId", String.valueOf(this.trafficLight.getIntersectionId()));
            aux_list.add(trafficLight.getWorldAID());
            char TLOrientation = trafficLight.getOrientation();
            if ((TLOrientation == 'W' || TLOrientation == 'E') && trafficLight.getIntersectionId() == 1)
                aux_list.add(trafficLight.getTLAIDFromDF(1, TLOrientation == 'W' ? 'E': 'W'));

            this.trafficLight.send(Utils.getACLMessage(message,
                    aux_list,
                    ACLMessage.INFORM));
        }
        else if (trafficLight.getColor() == 'g') {
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
