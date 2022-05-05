package Behaviour;

import Agents.Intersection;
import DataClasses.Lane;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;

public class UpdateVehicles extends TickerBehaviour {

    private Intersection intersection;

    public UpdateVehicles(Intersection intersection, long period) {
        super(intersection, period);
        this.intersection = intersection;
    }

    @Override
    protected void onTick() {
        ArrayList<Lane> lanes = intersection.getLanes();
        for(Lane lane: lanes) {
            lane.moveVehiclesForward();
            System.out.println(lane.getOrientation() + ":" + lane.getLaneVehicles());
        }
        System.out.println("======================================================");
    }
}
