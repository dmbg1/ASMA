package Behaviour;

import Agents.TrafficLight;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class UpdateTlElapsedTime extends TickerBehaviour {

    private TrafficLight trafficLight;

    public UpdateTlElapsedTime(TrafficLight trafficLight, long period) {
        super(trafficLight, period);
        this.trafficLight = trafficLight;
    }

    @Override
    protected void onTick() {
        trafficLight.incrementElapsedTime();
        if(trafficLight.getElapsedTime() >= 100)
            trafficLight.addBehaviour(new ChangeTrafficLightsColor(trafficLight));

        System.out.println("====> " + trafficLight.getLocalName() + " " + trafficLight.getElapsedTime() + " u" + trafficLight.utilityFunction());
    }
}
