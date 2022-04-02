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
        intersection = new Intersection(1, 5);
        Random r = new Random();

        ArrayList<String> orientations = new ArrayList<>();
        orientations.add("N");
        orientations.add("S");
        orientations.add("E");
        orientations.add("W");


        //Adiciona lanes a intersecao
        for(String orientation : orientations) {
            float velMax = (float) 50 + r.nextFloat() * 10; // Ou cada lane tem a sua velocidade ou por a velocidade na intersecao.
            Lane lane = new Lane(orientation, velMax); //Depois e preciso definir outros valores de orientacao.

            int numCars = r.nextInt(8);
            //Aciciona carros a lane
            for (int j = 0; j < numCars; j++) {
                float aceleration = (float)(2.5 + r.nextFloat() * 2.5);
                float deceleration = aceleration + 2;
                float position = 10; // Nao sei como definir este valor
                Car car = new Car(aceleration, deceleration, position);
                lane.addCar(car);
            }
            intersection.addLane(lane);
        }

        //System.out.println(intersection.getJSONintersection().toString(4));

        writer.append(intersection.getJSONintersection().toString(4));

        writer.close();
    }
}
