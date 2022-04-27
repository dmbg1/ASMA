import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class JsonGenerator {


    private Intersection intersection;

    public JsonGenerator(String filename) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        intersection = generateIntersection();

        writer.append(intersection.getJSONIntersection().toString(4));

        writer.close();
    }

    public Intersection generateIntersection() {
        Random r = new Random();

        // Velocidade máxima das lanes que entram na interceção
        double velMax = 50 + r.nextDouble() * 10;

        // Interseção
        intersection = new Intersection(1, 5, velMax);

        addLanesToIntersection(intersection, r);

        return intersection;
    }

    public void addLanesToIntersection(Intersection intersection, Random r) {
        HashMap<String, TrafficLight> trafficLights = generateTrafficLights(r);
        char[] orientations = {'N', 'S', 'E', 'W'};

        //Adiciona lanes a intersecao
        for (char orientation : orientations) {
            // TODO Depois e preciso definir outros valores de orientacao.
            int probGenerateCars = r.nextInt(100);
            Lane lane = new Lane(orientation, orientation == 'W' || orientation == 'E' ?
                    trafficLights.get("WETrafficLight") : trafficLights.get("NSTrafficLight"), probGenerateCars);

            addCarsToLane(lane, r);

            intersection.addLane(lane);
        }
    }

    //Adiciona número aleatório de carros (max 8) a uma lane
    public void addCarsToLane(Lane lane, Random r) {
        int numCars = r.nextInt(8);
        double acceleration, deceleration, length;
        double position = 0;
        double security_distance = 1; // representa o espaço inicial entre dois carros ou entre o carro e o semaforo

        for (int j = 0; j < numCars; j++) {
            acceleration = (2.5 + r.nextDouble() * 2.5);
            deceleration = acceleration + 2;
            length = (4 + r.nextDouble() * 1);
            position += length + security_distance;

            Car front_car = lane.getCars().size() == 0 ? null : lane.getCars().get(lane.getCars().size() - 1);

            Car car = new Car(acceleration, deceleration, position, length, front_car, lane);
            lane.addCar(car);
        }
    }

    public HashMap<String, TrafficLight> generateTrafficLights(Random r) {
        HashMap<String, TrafficLight> trafficLights = new HashMap<String, TrafficLight>();
        // Gerar aleatoriamente (entre r/g) a cor do semáforo das lanes com orientações N/S e
        // associar a outra cor aos semáforos com orientações W/E
        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';

        // Duração inicial dos semáforos entre 10 e 20 segundos
        int initialTrafLightDur = r.nextInt(11) + 10;
        trafficLights.put("NSTrafficLight", new TrafficLight(nsTrafficLightColor, initialTrafLightDur));
        trafficLights.put("WETrafficLight", new TrafficLight(weTrafficLightColor, initialTrafLightDur));

        return trafficLights;
    }

    public Intersection getIntersection() {
        return intersection;
    }
}
