package Behaviour;

import Agents.TrafficLight;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class ChangeTrafficLightColor extends WakerBehaviour {

    private TrafficLight trafficLight;

    public ChangeTrafficLightColor(TrafficLight trafficLight, long timeout) {
        super(trafficLight, timeout);

        this.trafficLight = trafficLight;
    }

    @Override
    protected void onWake() {

        String message = "color: " + this.trafficLight.getColor() + " duration: " + this.trafficLight.getDuration();

        trafficLight.changeColor();
        this.trafficLight.sendMessage(message, "Intersection", ACLMessage.INFORM);
        trafficLight.addBehaviour(new ChangeTrafficLightColor(trafficLight, trafficLight.getDuration()));
    }
}
