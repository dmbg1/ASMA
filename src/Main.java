import Agents.World;
import Utils.Lane;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Lane> lanes1 = new ArrayList<>(world.getIntersection1().getLanes().values());
        ArrayList<Lane> lanes2 = new ArrayList<>(world.getIntersection2().getLanes().values());

        ArrayList<ArrayList<Lane>> lanesInter = new ArrayList<>();
        lanesInter.add(lanes1);
        lanesInter.add(lanes2);

        for(ArrayList<Lane> _lanes : lanesInter) {
            for(int i = 0; i < _lanes.size(); i++) {
                int intersectionId = _lanes.equals(lanes1) ? 1 : 2;

                // Traffic light responsible for communicating with world and initiating negotiations
                boolean initiator_tl = i == 0;

                Lane curr_lane = _lanes.get(i);

                char color = curr_lane.getTrafficLight().getColor();
                int dur = curr_lane.getTrafficLight().getDuration() - 1; // -1 because of 1 second sleep
                char orientation = curr_lane.getOrientation();

                Object[] agentArgs = new Object[5];
                agentArgs[0] = color;
                agentArgs[1] = dur;
                agentArgs[2] = orientation;
                agentArgs[3] = initiator_tl;
                agentArgs[4] = intersectionId;

                AgentController agentController;
                try {
                    agentController = mainContainer.createNewAgent(curr_lane.getTrafficLight().getNameId(), "Agents.TrafficLight", agentArgs);
                    agentController.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
