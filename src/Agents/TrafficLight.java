package Agents;

import Behaviour.ChangeTrafficLightsColor;
import Behaviour.ListeningInform;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;

public class TrafficLight extends Agent {

    private char color;
    private int elapsedTime = 0;
    private char orientation;
    private boolean initiator;
    private int intersectionId;
    private int numCarsLane;

    @Override
    protected void setup() {

        Object[] agentArgs = getArguments();

        this.color = agentArgs[0].toString().charAt(0);
        this.orientation = agentArgs[1].toString().charAt(0);
        this.initiator = (boolean) agentArgs[2];
        this.intersectionId = (int) agentArgs[3];

        //TODO: Temporario, o semaforo vai mudar apenas durante a negociação
        if(initiator)
            addBehaviour(new ChangeTrafficLightsColor(this, 3000));

        addBehaviour(new ListeningInform(this));
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                elapsedTime++;
                System.out.println("====> " + getLocalName() + " " + elapsedTime);
            }
        });

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

    public void setNumCarsLane(int numCarsLane) {
        this.numCarsLane = numCarsLane;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void changeColor() {
        this.color = this.color == 'r' ? 'g' : 'r';
    }
}
