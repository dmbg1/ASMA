package Agents;

import Behaviour.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

public class TrafficLight extends Agent {

    private char color;
    private int elapsedTime = 0;
    private char orientation;
    private boolean initiator; // TODO Alternar após negociação aceite
    private int intersectionId;
    private int numCarsLane;
    private int closestCarDistance;

    @Override
    protected void setup() {

        Object[] agentArgs = getArguments();

        this.color = agentArgs[0].toString().charAt(0);
        this.orientation = agentArgs[1].toString().charAt(0);
        this.initiator = (boolean) agentArgs[2];
        this.intersectionId = (int) agentArgs[3];

        addBehaviour(new ListeningInform(this));
        addBehaviour(new UpdateTlElapsedTime(this, 1000));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // TODO Verificar quais semáforos serão initiators e como fazer no caso dos 3 semaforos
        // TODO Quando fazer a negociação?
        if(intersectionId == 2) {
            if(getLocalName().equals("tl4")){
                ACLMessage msg = buildChangeTlColorReqMsg(new AID("tl5", AID.ISLOCALNAME));
                addBehaviour(new FIPARequestTlInitiator(this, msg));
            }
            else {
                MessageTemplate template = MessageTemplate.and(
                        MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
                addBehaviour(new FIPARequestTlResponder(this, template));
            }

        }
        //System.out.println("ATL => " + this.getLocalName() + " ori: " + this.orientation + " color: " + this.color);
    }

    public void sendMessage(HashMap<String, String> message, String receiver, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        send(msg);
    }

    private ACLMessage buildChangeTlColorReqMsg(AID trafficLightAID) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(trafficLightAID);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        HashMap<String, String> msg_map = new HashMap<>();
        msg_map.put("Utility", String.valueOf(utilityFunction()));
        try {
            msg.setContentObject(msg_map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    // utility function to switch traffic light from red to green
    public double utilityFunction() {
        // tl1 <-> tl2 (se cor de tl1 for vermelha e diferente de tl2)
        // nr carros (+)
        // proximidade ao semaforo (mais proximo +)
        // TODO Adicionar quantidade de carros seguidos a partir do mais proximo
        return (numCarsLane * 0.4 + (8 - closestCarDistance) * 0.6) / 8;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public char getOrientation() {
        return orientation;
    }

    public char getColor() {
        return color;
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public boolean isInitiator() {
        return initiator;
    }

    public void incrementElapsedTime() {
        elapsedTime++;
    }

    public void setNumCarsLane(int numCarsLane) {
        this.numCarsLane = numCarsLane;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setClosestCarDistance(int closestCarDistance) {
        this.closestCarDistance = closestCarDistance;
    }

    public void changeColor() {
        this.color = this.color == 'r' ? 'g' : 'r';
    }
}
