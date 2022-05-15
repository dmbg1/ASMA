package Behaviour;

import Agents.TrafficLight;
import Agents.World;
import Utils.Intersection;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import static jade.lang.acl.MessageTemplate.MatchProtocol;

public class ListeningInform extends CyclicBehaviour {

    private Agent agent;

    MessageTemplate mt = MessageTemplate.and(MessageTemplate.not(MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM));

    public ListeningInform(Agent agent) {
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
                    case "Inform Lane State":
                        TrafficLight trafficLight = (TrafficLight) agent;
                        int numCars = Integer.parseInt(msg_map.get("numCars"));
                        int closestCarDistance = Integer.parseInt(msg_map.get("closestCarDistance"));
                        if(trafficLight.getIntersectionId() == 1 &&
                                (trafficLight.getOrientation() == 'W' || trafficLight.getOrientation() == 'E')) {
                            int parallelNumCars = Integer.parseInt(msg_map.get("parallelNumCars"));
                            trafficLight.setParallelNumCarsLane(parallelNumCars);
                            int parallelClosestCarDistance = Integer.parseInt(msg_map.get("parallelClosestCarDistance"));
                            trafficLight.setParallelClosestCarDistance(parallelClosestCarDistance);
                        }
                        trafficLight.setNumCarsLane(numCars);
                        trafficLight.setClosestCarDistance(closestCarDistance);
                        break;
                    case "Change Color": // For changing color in agent
                        TrafficLight TL = (TrafficLight) agent;
                        TL.addBehaviour(new ChangeTLColor(TL));
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
