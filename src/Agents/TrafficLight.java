package Agents;

import Behaviour.*;
import Utils.Utils;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

public class TrafficLight extends Agent {

    private AID worldAID;
    private AID parallelTLAID = null;

    private char color;
    private int elapsedTime = 0;
    private char orientation;
    private boolean initiator; // TODO Alternar após negociação aceite
    private int intersectionId;
    private int numCarsLane;
    private int closestCarDistance;
    private double parallelUtility = 0;

    @Override
    protected void setup() {

        Object[] agentArgs = getArguments();

        this.color = agentArgs[0].toString().charAt(0);
        this.orientation = agentArgs[1].toString().charAt(0);
        this.initiator = (boolean) agentArgs[2];
        this.intersectionId = (int) agentArgs[3];

        // Register traffic light in DF
        registerTrafficLight();
        // Get world AID from DF
        worldAID = getWorldAIDFromDF();
        // Get parallel traffic light AID from DF if any
        parallelTLAID = getParallelTLAIDFromDF();

        addBehaviour(new ListeningInform(this));
        addBehaviour(new UpdateTlElapsedTime(this, 1000));

        if(intersectionId == 1 && (orientation == 'E' || orientation == 'W')) {
            addBehaviour(new InformUtilityToParallelTL(this, 1000));
        }
        /*
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

        }*/
        //System.out.println("ATL => " + this.getLocalName() + " ori: " + this.orientation + " color: " + this.color);
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

    public void registerTrafficLight() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Traffic Light Intersection " + intersectionId + " Orientation " + orientation);
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println("Traffic Light " + this.getLocalName() + " registered in DF successfully");
        } catch(FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public AID getWorldAIDFromDF() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("World");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            return result[0].getName();
        } catch(FIPAException fe) {
            fe.printStackTrace();
        }
        return null;
    }

    // utility function to switch traffic light from red to green
    public double utilityFunction() {
        // TODO Adicionar quantidade de carros seguidos a partir do mais proximo

        if(orientation == 'E' || orientation == 'W')
            return (numCarsLane + (8 - closestCarDistance) + parallelUtility)/ 2;
        else
            return numCarsLane + (8 - closestCarDistance);
    }

    public AID getParallelTLAIDFromDF() {
        if(intersectionId == 1 && (orientation == 'E' || orientation == 'W')) {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Traffic Light Intersection " + intersectionId + " Orientation " +
                    (orientation == 'E' ? 'W' : 'E'));
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(this, template);
                return result[0].getName();
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }
        return null;
    }

    public AID getParallelTLAID() {
        return parallelTLAID;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public AID getWorldAID() {
        return worldAID;
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

    public void setParallelUtility(double parallelUtility) {
        System.out.println(parallelUtility);
        this.parallelUtility = parallelUtility;
    }

    public void changeColor() {
        this.color = this.color == 'r' ? 'g' : 'r';
    }
}
