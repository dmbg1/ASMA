package Agents;

import Behaviour.ChangeTrafficLightColor;
import Behaviour.GenerateVehicles;
import Behaviour.UpdateVehicles;
import jade.core.Agent;

public class TrafficLight extends Agent {

    private char color;
    private int duration;
    private char orientation;

    @Override
    protected void setup() {

        //TODO: É preciso adicionar os argumentos na criacao dos agentes
        Object[] agentArgs = getArguments();

        this.color = (char)agentArgs[0];
        this.duration = (int)agentArgs[1];
        this.orientation = (char)agentArgs[2];

        addBehaviour(new ChangeTrafficLightColor(this, 2000));
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
