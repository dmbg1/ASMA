/*
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Random r = new Random();
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        ContainerController container = rt.createMainContainer(p1);

        TrafficLight NTrafficLight = new TrafficLight();
        TrafficLight STrafficLight = new TrafficLight();
        TrafficLight ETrafficLight = new TrafficLight();
        TrafficLight WTrafficLight = new TrafficLight();

        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';

        NTrafficLight.setCurr_color(nsTrafficLightColor);
        STrafficLight.setCurr_color(nsTrafficLightColor);
        ETrafficLight.setCurr_color(weTrafficLightColor);
        WTrafficLight.setCurr_color(weTrafficLightColor);

        NTrafficLight.setOrientation('N');
        STrafficLight.setOrientation('S');
        ETrafficLight.setOrientation('E');
        WTrafficLight.setOrientation('W');

        try {
            AgentController TlN = container.acceptNewAgent("TrafficLightN", NTrafficLight);
            AgentController TlS = container.acceptNewAgent("TrafficLightS", STrafficLight);
            AgentController TlW = container.acceptNewAgent("TrafficLightW", ETrafficLight);
            AgentController TlE = container.acceptNewAgent("TrafficLightE", WTrafficLight);

            TlN.start();
            TlS.start();
            TlW.start();
            TlE.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        ArrayList<TrafficLight> trafficLights = new ArrayList<>();
        trafficLights.add(NTrafficLight);
        trafficLights.add(STrafficLight);
        trafficLights.add(ETrafficLight);
        trafficLights.add(WTrafficLight);

        World world = new World();
        Object[] agentArgs = new Object[4];
        int i=0;

        for(TrafficLight tl : trafficLights) {
            agentArgs[i] = tl;
            i++;
        }

        try {
            AgentController world1 = container.createNewAgent("Agents.World", "Agents.World", agentArgs);
            world1.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
*/