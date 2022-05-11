package Agents;

import Behaviour.GenerateVehicles;
import Behaviour.ListeningInform;
import Behaviour.UpdateVehicles;
import Utils.Intersection;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashMap;

public class World extends Agent {
    /*
   World drawing
   * * * * * * * * I2 * * * * * * * * <-- Possible ways: front in intersection
                   *
                   * <-- Possible ways: left in intersection
                   *
                   *
                   *
                   *
                   *    Ʌ
                   *  <-|
   * * * * * * * * I1 * * * * * * * *
              -->  * Ʌ
                   * |->
                   *
                   *
                   *
                   *
                   *
                   *
   Where
   * -> Position vehicle can occupy
   I1 -> Intersection 1
   I2 -> Intersection 2
   */

    private Intersection intersection1;
    private Intersection intersection2;

    public void setup() {
        generateWorld();
        addBehaviour(new GenerateVehicles(this, 5000));
        addBehaviour(new UpdateVehicles(this, 1000));
        addBehaviour(new ListeningInform(this));

    }

    private void generateWorld() {
        intersection1 = new Intersection(1);
        intersection2 = new Intersection(2);
    }

    public Intersection getIntersection1() {
        return intersection1;
    }

    public Intersection getIntersection2() {
        return intersection2;
    }

    public void changeColorTrafficLight(String nameId, int duration) {

        intersection1.changeTrafficLightsColor(nameId, duration);
        intersection2.changeTrafficLightsColor(nameId, duration);
    }
}
