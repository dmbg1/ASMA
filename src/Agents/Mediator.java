package Agents;

import Behaviour.ListeningInform;
import jade.core.Agent;

public class Mediator extends Agent {

    public void setup() {
        addBehaviour(new ListeningInform(this));

    }
}
