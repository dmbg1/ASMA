package Utils;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;

public class Utils {

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

    public static double vehiclesAverageWaitingTime() {


        return 0.0;
    }

}
