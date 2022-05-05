import jade.core.Agent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class World extends Agent {

    String filename = "intersection";
    Intersection intersection;

    public void setup() {

        ArrayList<TrafficLight> trafficLights = new ArrayList<>();
        Object[] agentArgs = getArguments();

        for (Object agentArg : agentArgs) {
            trafficLights.add((TrafficLight) agentArg);
        }

        try {
            JsonGenerator jsonGenerator = new JsonGenerator(trafficLights, filename + ".json");
            intersection = jsonGenerator.getIntersection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Lane lane : intersection.getLanes()) {
            lane.start();
            for (Car car : lane.getCars()) {
                car.start();
            }
        }

        // TODO Comunicar com traffic lights para dar o numero de carros numa lane
        System.out.println("Agents.World setup done.");
    }
}
