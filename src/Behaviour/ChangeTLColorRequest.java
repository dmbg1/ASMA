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
        if(trafficLight.isInitiator() && !trafficLight.isInNegotiation()) {
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
}
