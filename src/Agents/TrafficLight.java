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
