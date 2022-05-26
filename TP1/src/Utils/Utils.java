package Utils;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    public static String reset = "\u001b[0m";
    public static String green = "\u001b[32m";
    public static String red = "\u001b[31m";
    public static String yellow = "\u001B[33m";

    public static ACLMessage getACLMessage(HashMap<String, String> message, AID receiver, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg.addReceiver(receiver);
        return msg;
    }

    public static ACLMessage getACLMessage(HashMap<String, String> message, ArrayList<AID> receiverList, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (AID aid : receiverList)
            msg.addReceiver(aid);

        return msg;
    }

    public static ACLMessage getACLMessage(HashMap<String, String> message, String receiver, int performative) {

        ACLMessage msg = new ACLMessage(performative);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        return msg;
    }

}
