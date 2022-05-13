package Behaviour;

import Agents.World;
import Utils.Lane;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GenerateVehicles extends TickerBehaviour {
    private World world;
    public GenerateVehicles(World world, long period) {
        super(world, period);
        this.world = world;
    }
    @Override
    protected void onTick() {
        ArrayList<Lane> lanes = new ArrayList<>(world.getIntersection1().getLanes().values());

        // Not considering north lane of intersection 2 because it is fed by the other intersection
        lanes.add(world.getIntersection2().getLanes().get('W'));

        Random r = new Random();

        for (Lane lane: lanes) {
            if(r.nextInt(1, 101) <= lane.getProbGenerateLane())
                lane.addCarToLaneQueue();
        }

        this.world.informTLNumCars();
    }
}
