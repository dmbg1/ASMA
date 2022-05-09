import Agents.Intersection;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();

        Profile p = new ProfileImpl();
        ContainerController mainContainer = rt.createMainContainer(p);

        AgentController ac;
        Intersection intersection = new Intersection();
        try {
            ac = mainContainer.acceptNewAgent("intersection", intersection);
            ac.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        AgentController ac1;
        try {
            ac1 = mainContainer.acceptNewAgent("myRMA", new jade.tools.rma.rma());
            ac1.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
