package Behaviour;

import Agents.TrafficLight;
import Utils.Utils;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;

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
        if (trafficLight.getElapsedTime() > 12)
            trafficLight.addBehaviour(new ChangeTLColor(trafficLight, false));

        // Send request every 4 seconds elapsed in the color the traffic light is
        if (trafficLight.isInitiator() && !trafficLight.isInNegotiation()
                && trafficLight.getElapsedTime() % 4 == 0 && trafficLight.getElapsedTime() > 0) {
            sendColorChangeRequest();
            System.out.println(Utils.yellow + "\uE319 " + Utils.reset + "Agent " + trafficLight.getLocalName() +
                    " sent request to change traffic light color");
        }
    }

    public void sendColorChangeRequest() {
        ACLMessage msg = null;
        // For the world simulated the following conditions on the message building is enough
        if (trafficLight.getOrientation() == 'E' || trafficLight.getOrientation() == 'W')
            msg = buildChangeTlColorReqMsg(trafficLight.getTLAIDFromDF(trafficLight.getIntersectionId(), 'N'));
        else if (trafficLight.getOrientation() == 'N')
            msg = buildChangeTlColorReqMsg(trafficLight.getTLAIDFromDF(trafficLight.getIntersectionId(), 'W'));

        trafficLight.setInNegotiation(true);
        trafficLight.addBehaviour(new ChangeTLColorRequestInit(trafficLight, msg));
    }

    public ACLMessage buildChangeTlColorReqMsg(AID trafficLightAID) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(trafficLightAID);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        HashMap<String, String> msg_map = new HashMap<>();
        msg_map.put("Utility", String.valueOf(trafficLight.utilityFunction()));
        try {
            msg.setContentObject(msg_map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
