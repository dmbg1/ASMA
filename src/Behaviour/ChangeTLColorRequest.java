package Behaviour;

import Agents.TrafficLight;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ChangeTLColorRequest extends SimpleBehaviour {
    private TrafficLight trafficLight;

    public ChangeTLColorRequest(TrafficLight trafficLight) {
        super(trafficLight);
        this.trafficLight = trafficLight;
    }

    @Override
    public void action() {
        if(trafficLight.getNumCarsLane() > 0 && trafficLight.getClosestCarDistance() == 0
                && trafficLight.isInitiator()) {
            System.out.println(132);
            if(trafficLight.getOrientation() == 'E' || trafficLight.getOrientation() == 'W') {
                ACLMessage msg = trafficLight.buildChangeTlColorReqMsg(
                        trafficLight.getTLAIDFromDF(
                                trafficLight.getIntersectionId(),
                                'N'
                        )
                );

                trafficLight.addBehaviour(new ChangeTLColorRequestInit(trafficLight, msg));
                trafficLight.addBehaviour(new ChangeTLColorRequestInit(trafficLight, msg));
            }
        }
    }

    @Override
    public boolean done() {
        return true;
    }
}
