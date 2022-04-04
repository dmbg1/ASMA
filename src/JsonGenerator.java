import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class JsonGenerator {


    private String filename;
    private Intersection intersection;

    public JsonGenerator(String filename) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        Random r = new Random();

        // Velocidade máxima das lanes que entram na interceção
        float velMax = (float) 50 + r.nextFloat() * 10;

        // Interseção
        intersection = new Intersection(1, 5, velMax);

        // Gerar aleatoriamente (entre r/g) a cor do semáforo das lanes com orientações N/S e
        // associar a outra cor aos semáforos com orientações W/E
        char nsTrafficLightColor = r.nextBoolean() ? 'r' : 'g';
        char weTrafficLightColor = nsTrafficLightColor == 'r' ? 'g' : 'r';
        // Duração inicial dos semáforos entre 10 e 20 segundos
        float initialTrafLightDur = 10 + r.nextFloat() * (20 - 10);

        // Semáforos
        TrafficLight nsTrafficLight = new TrafficLight(nsTrafficLightColor, initialTrafLightDur);
        TrafficLight weTrafficLight = new TrafficLight(weTrafficLightColor, initialTrafLightDur);

        char[] orientations = {'N', 'S', 'E', 'W'};

        //Adiciona lanes a intersecao
        for (char orientation : orientations) {
            // TODO Depois e preciso definir outros valores de orientacao.
            Lane lane = new Lane(orientation,
                    orientation == 'W' || orientation == 'E' ? weTrafficLight : nsTrafficLight);

            int numCars = r.nextInt(8);
            //Aciciona carros a lane
            for (int j = 0; j < numCars; j++) {
                float acceleration = (float) (2.5 + r.nextFloat() * 2.5);
                float deceleration = acceleration + 2;
                float position = 10; // Nao sei como definir este valor
                Car car = new Car(acceleration, deceleration, position);
                lane.addCar(car);
            }
            intersection.addLane(lane);
        }

        writer.append(intersection.getJSONIntersection().toString(4));

        writer.close();
    }

}
