package Behaviour;


import Agents.TrafficLight;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class FIPARequestTlInitiator extends AchieveREInitiator {

    private TrafficLight trafficLight;
    public FIPARequestTlInitiator(TrafficLight trafficLight, ACLMessage msg) {
        super(trafficLight, msg);
        this.trafficLight = trafficLight;
    }

    /*
    protected Vector<ACLMessage> prepareRequests(ACLMessage msg) {
        // add behaviours
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        Vector<ACLMessage> v = new Vector<ACLMessage>();
        // ...
        return v;
    }*/

    protected void handleAgree(ACLMessage agree) {
        System.out.println("Agent " + agree.getSender().getLocalName() + " accepted request to " +
                "change traffic light color");
    }

    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Agent " + refuse.getSender().getLocalName() + " refused request to " +
                "change traffic light color");
    }

    protected void handleInform(ACLMessage inform) {
        try {
            Object[] oMsg= (Object[]) inform.getContentObject();
            String req = (String) oMsg[0];
            String ok = (String) oMsg[1];
            if (req.equals("REQ") && ok.equals("OK")) { // Traffic Light change made successfully on responder side
                System.out.println("Agent " + inform.getSender().getLocalName() + " changed its traffic light color");
                trafficLight.addBehaviour(new ChangeTrafficLightsColor(trafficLight));
            }
        } catch (UnreadableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            System.out.println("Responder does not exist");
        }
        else
            System.out.println("Agent " + failure.getSender().getLocalName() + " failed to change traffic light color");
    }

}