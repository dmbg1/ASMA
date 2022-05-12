package Behaviour;

import Agents.World;
import Utils.Lane;
import Utils.TrafficLight;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;

public class CountdownWorldTrafficLights extends TickerBehaviour {

    World world;

    public CountdownWorldTrafficLights(World world, long period) {
        super(world, period);
        this.world = world;
    }

    @Override
    protected void onTick() {
        ArrayList<Lane> lanesInter1 = new ArrayList<>(world.getIntersection1().getLanes().values());
        ArrayList<Lane> lanesInter2 = new ArrayList<>(world.getIntersection2().getLanes().values());
        ArrayList<ArrayList<Lane>> lanes = new ArrayList<>();
        lanes.add(lanesInter1);
        lanes.add(lanesInter2);

        for(ArrayList<Lane> _lanes: lanes) {
            for (Lane lane: _lanes) {
                TrafficLight laneTrafficLight = lane.getTrafficLight();
                laneTrafficLight.setDuration(laneTrafficLight.getDuration() - 1);
            }
        }
    }
}
