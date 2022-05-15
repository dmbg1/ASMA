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
        ACLMessage reply = request.createReply();
        if (checkUtilities(request) && !trafficLight.isInNegotiation()){
            trafficLight.setInNegotiation(true);
            reply.setPerformative(ACLMessage.AGREE);
        }
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
        trafficLight.addBehaviour(new ChangeTLColor(trafficLight));
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
            trafficLight.setInitiator(true);
        } else {
            trafficLight.setInNegotiation(false);
            throw new FailureException("unexpected failure");
        }
        trafficLight.setInNegotiation(false);
        return result;
    }

}
