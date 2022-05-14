package Behaviour;

import Agents.TrafficLight;
import Utils.Utils;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class InformUtilityToParallelTL extends TickerBehaviour {

    private TrafficLight trafficLight;
    public InformUtilityToParallelTL(TrafficLight trafficLight, long period) {
        super(trafficLight, period);
        this.trafficLight = trafficLight;
    }

    @Override
    protected void onTick() {
        HashMap<String, String> message = new HashMap<>();
        message.put("MsgType", "Parallel Utility");
        message.put("Utility", String.valueOf(trafficLight.utilityFunction()));
        this.trafficLight.send(Utils.getACLMessage(message,
                String.valueOf(trafficLight.getParallelTLAID()),
                ACLMessage.INFORM));
    }
}
