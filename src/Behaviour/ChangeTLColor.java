package Behaviour;

import Agents.TrafficLight;
import Utils.Utils;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

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
        if(trafficLight.isInitiator()) {
            HashMap<String, String> message = new HashMap<>();
            message.put("MsgType", "Alternate color");
            message.put("InterId", String.valueOf(this.trafficLight.getIntersectionId()));
            this.trafficLight.send(Utils.getACLMessage(message,
                    trafficLight.getWorldAID(),
                    ACLMessage.INFORM));
        }
    }

    @Override
    public boolean done() {
        if(trafficLight.isInitiator())
            System.out.println("Changed traffic light colors on intersection " + trafficLight.getIntersectionId());
        return true;
    }
}
