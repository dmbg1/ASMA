package Behaviour;

import Agents.Intersection;
import DataClasses.TrafficLight;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListeningInform extends CyclicBehaviour {

    private Intersection intersection;

    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    public ListeningInform(Intersection intersection) {
        this.intersection = intersection;
    }

    @Override
    public void action() {

        ACLMessage msg = this.getAgent().receive(mt);

        if(msg != null) {
            //TODO: Receber msg e alterar valores dos semaforos
            System.out.println(msg);
        }else {
            block();
        }
    }
}
