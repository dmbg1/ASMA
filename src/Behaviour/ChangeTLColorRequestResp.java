package Behaviour;

import Agents.TrafficLight;
import Utils.Utils;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;
import java.util.HashMap;

public class ChangeTLColorRequestResp extends AchieveREResponder {

    private TrafficLight trafficLight;

    public ChangeTLColorRequestResp(TrafficLight trafficLight, MessageTemplate mt) {
        super(trafficLight, mt);
        this.trafficLight = trafficLight;
    }

    protected ACLMessage handleRequest(ACLMessage request) throws RefuseException {
        trafficLight.setInNegotiation(true);
        ACLMessage reply = request.createReply();
        if (checkUtilities(request))
            reply.setPerformative(ACLMessage.AGREE);
        else {
            trafficLight.setInNegotiation(false);
            throw new RefuseException("check-failed");
        }
        return reply;
    }

    protected boolean checkUtilities(ACLMessage request) {
        try {
            HashMap<String, String> msg_map = (HashMap<String, String>) request.getContentObject();
            double initiator_utility = Double.parseDouble(msg_map.get("Utility"));
            return initiator_utility > trafficLight.utilityFunction();
        } catch (UnreadableException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean performAction() {
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
        trafficLight.setInitiator(true);
        return true;
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        ACLMessage result = request.createReply();
        if (performAction()) {

            Object[] oMsg = new Object[2];
            oMsg[0] = "REQ";
            oMsg[1] = "OK";

            try {
                result.setContentObject(oMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            result.setPerformative(ACLMessage.INFORM);
        } else {
            trafficLight.setInNegotiation(false);
            throw new FailureException("unexpected failure");
        }

        trafficLight.setInNegotiation(false);
        return result;
    }

}