package Behaviour;

import Agents.TrafficLight;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class ChangeTrafficLightsColor extends WakerBehaviour {

    private TrafficLight trafficLight;

    public ChangeTrafficLightsColor(TrafficLight trafficLight, long timeout) {
        super(trafficLight, timeout);

        this.trafficLight = trafficLight;
    }

    @Override
    protected void onWake() {

        HashMap<String, String> message = new HashMap<>();
        message.put("MsgType", "Alternate color");
        message.put("InterId", String.valueOf(this.trafficLight.getIntersectionId()));

        this.trafficLight.setElapsedTime(0);
        this.trafficLight.changeColor();
        this.trafficLight.sendMessage(message, "World", ACLMessage.INFORM);
        this.trafficLight.addBehaviour(new ChangeTrafficLightsColor(trafficLight, 3000));
    }
}
