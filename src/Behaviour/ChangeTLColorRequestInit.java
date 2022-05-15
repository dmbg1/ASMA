package Behaviour;


import Agents.TrafficLight;
import Utils.Utils;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.util.HashMap;

public class ChangeTLColorRequestInit extends AchieveREInitiator {

    private TrafficLight trafficLight;
    public ChangeTLColorRequestInit(TrafficLight trafficLight, ACLMessage msg) {
        super(trafficLight, msg);
        this.trafficLight = trafficLight;
    }

    protected void handleAgree(ACLMessage agree) {
        System.out.println(Utils.green + "\u2713" + Utils.reset + "Agent " + agree.getSender().getLocalName() + " accepted request to " +
                "change traffic light color");
    }

    protected void handleRefuse(ACLMessage refuse) {
        System.out.println(Utils.red + "\u2A2F" + Utils.reset + "Agent " + refuse.getSender().getLocalName() + " refused request to " +
                "change traffic light color");
        trafficLight.setInNegotiation(false);
    }

    protected void handleInform(ACLMessage inform) {
        try {
            Object[] oMsg= (Object[]) inform.getContentObject();
            String req = (String) oMsg[0];
            String ok = (String) oMsg[1];

            if (req.equals("REQ") && ok.equals("OK")) { // Traffic Light change made successfully on responder side
                //System.out.println("Agent " + inform.getSender().getLocalName() + " changed its traffic light color");

                char TLOrientation = trafficLight.getOrientation();
                if ((TLOrientation == 'W' || TLOrientation == 'E') && trafficLight.getIntersectionId() == 1) {
                    HashMap<String, String> msg_map = new HashMap<>();
                    msg_map.put("MsgType", "Change Color");

                    trafficLight.send(Utils.getACLMessage(
                            msg_map,
                            trafficLight.getTLAIDFromDF(trafficLight.getIntersectionId(),
                                    TLOrientation == 'W' ? 'E' : 'W')
                            , ACLMessage.INFORM
                        )
                    );
                }
                trafficLight.addBehaviour(new ChangeTLColor(trafficLight));
                trafficLight.setInitiator(false);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        trafficLight.setInNegotiation(false);
    }

    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            System.out.println("Responder does not exist");
        }
        else
            System.out.println("Agent " + failure.getSender().getLocalName() + " failed to change traffic light color");
        trafficLight.setInNegotiation(false);
    }

}
