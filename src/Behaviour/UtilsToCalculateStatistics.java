package Behaviour;

import Agents.World;
import Utils.Lane;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

public class UtilsToCalculateStatistics extends TickerBehaviour {

    private World world;

    public UtilsToCalculateStatistics(World world, long period) {
        super(world, period);

        this.world = world;
    }

    @Override
    protected void onTick() {

        this.incrementLanesLightTime();
    }

    public void incrementLanesLightTime() {

        for(Lane lane: this.world.getIntersection1().getLanes().values()) {
            if(lane.getTrafficLight().getColor() == 'r') {
                lane.incrementRedTime();
            }else {
                lane.incrementGreenTime();
            }
        }

        for(Lane lane: this.world.getIntersection2().getLanes().values()) {
            if(lane.getTrafficLight().getColor() == 'r') {
                lane.incrementRedTime();
            }else {
                lane.incrementGreenTime();
            }
        }
    }
}
