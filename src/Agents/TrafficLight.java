package Agents;

import Behaviour.ChangeTrafficLightColor;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TrafficLight extends Agent {

    private char color;
    private int duration;
    private char orientation;

    @Override
    protected void setup() {

        //É preciso adicionar os argumentos na criacao dos agentes

        Object[] agentArgs = getArguments();

        this.color = agentArgs[0].toString().charAt(0);
        this.duration =  (int) agentArgs[1];
        this.orientation = agentArgs[2].toString().charAt(0);

        HashMap<String, String> message = new HashMap<>();
        message.put("MsgType", "Alternate color");
        message.put("NameId", this.getLocalName());
        message.put("Duration", String.valueOf(this.duration));

        sendMessage(message, "World", ACLMessage.INFORM);

        addBehaviour(new ChangeTrafficLightColor(this, this.duration));

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

    public void changeColor() {

        if(color == 'r') this.color = 'g';
        else this.color = 'r';
    }
}
