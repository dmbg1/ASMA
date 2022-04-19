import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String filename = "intersection";
        Intersection intersection;

        try {
            JsonGenerator jsonGenerator = new JsonGenerator(filename + ".json");
            intersection = jsonGenerator.getIntersection();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (Lane lane : intersection.getLanes()) {
            for (Car car : lane.getCars()) {
                car.start();
            }
        }
    }
}
