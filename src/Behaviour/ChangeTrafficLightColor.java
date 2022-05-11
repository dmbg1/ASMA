package Behaviour;

import Agents.TrafficLight;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class ChangeTrafficLightColor extends WakerBehaviour {

    private TrafficLight trafficLight;

    public ChangeTrafficLightColor(TrafficLight trafficLight, long timeout) {
        super(trafficLight, timeout);

        this.trafficLight = trafficLight;
    }

    @Override
    protected void onWake() {

        HashMap<String, String> message = new HashMap<>();
        message.put("MsgType", "Alternate color");
        message.put("NameId", this.trafficLight.getLocalName());
        message.put("Duration", String.valueOf(this.trafficLight.getDuration()));


        this.trafficLight.changeColor();
        this.trafficLight.sendMessage(message, "", ACLMessage.INFORM);
        this.trafficLight.addBehaviour(new ChangeTrafficLightColor(trafficLight, trafficLight.getDuration()));
    }
}
