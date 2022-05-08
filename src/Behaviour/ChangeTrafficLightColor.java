package Behaviour;

import Agents.TrafficLight;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class ChangeTrafficLightColor extends WakerBehaviour {

    private TrafficLight trafficLight;

    public ChangeTrafficLightColor(TrafficLight trafficLight, long timeout) {
        super(trafficLight, timeout);
    }

    @Override
    protected void onWake() {
        trafficLight.changeColor();

        trafficLight.addBehaviour(new ChangeTrafficLightColor(trafficLight, trafficLight.getDuration()));
    }
}
