package Behaviour;

import Agents.TrafficLight;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ChangeTLColorRequest extends TickerBehaviour {
    private TrafficLight trafficLight;

    public ChangeTLColorRequest(TrafficLight trafficLight, long period) {
        super(trafficLight, period);
        this.trafficLight = trafficLight;
    }

    @Override
    protected void onTick() {
        trafficLight.incrementElapsedTime();
        // if traffic light surpasses the 12 seconds elapsed mark change color

        if(trafficLight.getElapsedTime() > 12)
            trafficLight.addBehaviour(new ChangeTLColor(trafficLight));

        // Send request every 4 seconds elapsed in the color the traffic light is

        if(trafficLight.isInitiator() && !trafficLight.isInNegotiation()
                && trafficLight.getElapsedTime() % 2 == 0 && trafficLight.getElapsedTime() > 0) {
            sendColorChangeRequest();
        }
    }

    public void sendColorChangeRequest() {
        ACLMessage msg = null;
        // For the world simulated the following conditions on the message building is enough
        if (trafficLight.getOrientation() == 'E' || trafficLight.getOrientation() == 'W')
            msg = trafficLight.buildChangeTlColorReqMsg(
                    trafficLight.getTLAIDFromDF(
                            trafficLight.getIntersectionId(),
                            'N')
            );
        else if (trafficLight.getOrientation() == 'N')
            msg = trafficLight.buildChangeTlColorReqMsg(
                    trafficLight.getTLAIDFromDF(
                            trafficLight.getIntersectionId(),
                            'W'
                    )
            );

        trafficLight.setInNegotiation(true);
        trafficLight.addBehaviour(new ChangeTLColorRequestInit(trafficLight, msg));
    }
}
