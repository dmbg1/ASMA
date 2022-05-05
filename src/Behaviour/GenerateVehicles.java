package Behaviour;

import Agents.Intersection;
import DataClasses.Lane;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.Random;

public class GenerateVehicles extends TickerBehaviour {
    private Intersection intersection;
    public GenerateVehicles(Intersection intersection, long period) {
        super(intersection, period);
        this.intersection = intersection;
    }
    @Override
    protected void onTick() {
        ArrayList<Lane> lanes = intersection.getLanes();
        Random r = new Random();

        for (Lane lane: lanes) {
            if(r.nextInt(1, 101) <= lane.getProbGenerateLane())
                lane.addCarToLane();

            System.out.println(lane.getOrientation() + ": " + lane.getLaneVehicles());
        }
    }
}
