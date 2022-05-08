package Agents;

import Behaviour.ChangeTrafficLightColor;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class TrafficLight extends Agent {

    private char color;
    private int duration;
    private char orientation;

    @Override
    protected void setup() {

        //É preciso adicionar os argumentos na criacao dos agentes

        Object[] agentArgs = getArguments();

        this.color = ((String)agentArgs[0]).charAt(1);
        this.duration = Integer.parseInt((String)agentArgs[1]);
        this.orientation = ((String)agentArgs[2]).charAt(1);

        String message = "color: " + this.color + " duration: " + this.duration + " orientation: " + this.orientation;

        sendMessage(message, "Intersection", ACLMessage.INFORM);

        addBehaviour(new ChangeTrafficLightColor(this, 2000));
    }

    public void sendMessage(String message, String receiver, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        msg.setContent(message);
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

    public void changeColor() {

        if(color == 'r') this.color = 'g';
        else this.color = 'r';
    }
}
