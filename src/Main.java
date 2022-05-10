import Agents.World;
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
        World world = new World();
        try {
            ac = mainContainer.acceptNewAgent("World", world);
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

        // Wait until intersection agent is created
        /*
        ArrayList<Lane> lanes = world.getLanes();
        while(lanes.size() == 0) lanes = world.getLanes();

        for(int i = 0; i < 4; i++) {

            Lane curr_lane = lanes.get(i);
            char color = curr_lane.getTrafficLight().getColor();
            int dur = curr_lane.getTrafficLight().getDuration();
            char orientation = curr_lane.getOrientation();

            Object[] agentArgs = new Object[3];
            agentArgs[0] = color;
            agentArgs[1] = dur;
            agentArgs[2] = orientation;

            AgentController agentController;
            try {
                agentController = mainContainer.createNewAgent("tl" + (i + 1), "Agents.TrafficLight", agentArgs);
                agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }*/
    }
}
