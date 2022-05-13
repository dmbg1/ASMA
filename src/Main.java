import Agents.World;
import Utils.Lane;
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
        try {
            Object[] agentArgs = new Object[1];
            agentArgs[0] = mainContainer;
            ac = mainContainer.createNewAgent("World", "Agents.World", agentArgs);
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
