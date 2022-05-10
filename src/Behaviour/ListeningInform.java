package Behaviour;

import DataClasses.Intersection;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

public class ListeningInform extends CyclicBehaviour {

    private Agent agent;

    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    public ListeningInform(Agent agent) { // TODO Maybe add performative
        this.agent = agent;
    }

    @Override
    public void action() {

        ACLMessage msg = this.getAgent().receive(mt);
        // TODO ['Alternate traffic light', 'duration', duration]
        // TODO ['Alternate traffic light', 'duration:', duration, 'orientation:', orientation]
        // TODO ['Init', 'color', 'duration', 'orientation'] maybe
        if(msg != null) {
            try {
                ArrayList<String> msg_array = (ArrayList) msg.getContentObject();
                String msg_type = msg_array.get(0);
                int durationNS = Integer.parseInt(msg_array.get(3));
                int durationWE = Integer.parseInt(msg_array.get(5));

                switch(msg_type) {
                    case "Alternate traffic light":
                        Intersection intersection = (Intersection) agent;
                        intersection.alternateTrafficLights(d);
                }

            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            //TODO: Receber msg e alterar valores dos semaforos
        }else {
            block();
        }
    }
}
