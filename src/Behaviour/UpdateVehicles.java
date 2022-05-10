package Behaviour;

import Agents.World;
import Utils.Lane;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateVehicles extends TickerBehaviour {

    private final World world;

    public UpdateVehicles(World world, long period) {
        super(world, period);
        this.world = world;
    }

    @Override
    protected void onTick() {
        HashMap<Character, Lane> lanesInter1 = world.getIntersection1().getLanes();
        HashMap<Character, Lane> lanesInter2 = world.getIntersection2().getLanes();

        for (Lane lane : lanesInter2.values()) {
            char laneOrientation = lane.getOrientation();
            // Possible lane ways in intersection
            ArrayList<Lane> laneWays = new ArrayList<>();

            if (laneOrientation == 'N' || lane.getOrientation() == 'W') {
                // Left on intersection when lane orientation is N and front when lane orientation is W
                // null because there will always be available space in the lane
                laneWays.add(null);
            }
            lane.updateVehiclesInLane(laneWays, world.getIntersection2().getId());
        }

        for (Lane lane : lanesInter1.values()) {
            char laneOrientation = lane.getOrientation();
            // Possible lane ways in intersection
            ArrayList<Lane> laneWays = new ArrayList<>();

            if (laneOrientation == 'N' || lane.getOrientation() == 'W') {
                // Right on intersection when lane orientation is W and front when lane orientation is N
                laneWays.add(lanesInter2.get('N'));
                // Front on intersection when lane orientation is W and front when lane orientation is N
                // null because there will always be available space in the lane
                laneWays.add(null);
            } else if (laneOrientation == 'E')
                // Front on intersection when lane orientation is E
                laneWays.add(null);

            lane.updateVehiclesInLane(laneWays, world.getIntersection1().getId());
        }

        this.world.getIntersection1().logIntersection();
        this.world.getIntersection2().logIntersection();
        System.out.println("==============================================================================");
    }
}
