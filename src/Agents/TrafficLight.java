package Agents;

import Behaviour.ChangeTrafficLightsColor;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;

public class TrafficLight extends Agent {

    private char color;
    private int duration;
    private char orientation;
    private boolean initiator;
    private int intersectionId;

    @Override
    protected void setup() {

        //É preciso adicionar os argumentos na criacao dos agentes

        Object[] agentArgs = getArguments();

        this.color = agentArgs[0].toString().charAt(0);
        this.duration =  (int) agentArgs[1];
        this.orientation = agentArgs[2].toString().charAt(0);
        this.initiator = (boolean) agentArgs[3];
        this.intersectionId = (int) agentArgs[4];

        if(initiator)
            addBehaviour(new ChangeTrafficLightsColor(this, this.duration * 1000));

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

    public int getDuration() {
        return duration;
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

    public void changeColor() {
        this.color = this.color == 'r' ? 'g' : 'r';
    }
}
