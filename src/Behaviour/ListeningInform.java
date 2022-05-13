package Behaviour;

import Agents.TrafficLight;
import Agents.World;
import Utils.Intersection;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

public class ListeningInform extends CyclicBehaviour {

    private Agent agent;

    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    public ListeningInform(Agent agent) { // TODO Maybe add performative
        this.agent = agent;
    }

    @Override
    public void action() {

        ACLMessage msg = this.getAgent().receive(mt);
        if(msg != null) {
            try {
                HashMap<String, String> msg_map = (HashMap<String, String>) msg.getContentObject();
                String msg_type = msg_map.get("MsgType");

                switch(msg_type) {
                    case "Alternate color":
                        int intersectionId = Integer.parseInt(msg_map.get("InterId"));
                        World world = (World) agent;

                        // duration + 1 because update behaviour makes it -1 immediately when color is alternated
                        world.changeColorTrafficLight(intersectionId);
                        break;
                    case "Inform num cars":
                        int numCars = Integer.parseInt(msg_map.get("numCars"));
                        TrafficLight trafficLight = (TrafficLight) agent;

                        trafficLight.setNumCarsLane(numCars);
                        break;
                    default:
                        block();
                        break;
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
