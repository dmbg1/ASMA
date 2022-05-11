import Agents.World;
import Utils.Intersection;
import Utils.Lane;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
            Thread.sleep(3000);
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

                Lane curr_lane = _lanes.get(i);

                char color = curr_lane.getTrafficLight().getColor();
                int dur = curr_lane.getTrafficLight().getDuration();
                char orientation = curr_lane.getOrientation();

                Object[] agentArgs = new Object[3];
                agentArgs[0] = color;
                agentArgs[1] = dur;
                agentArgs[2] = orientation;

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
