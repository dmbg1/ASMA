import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String filename = "file2";

        try {
            JsonGenerator jsonGenerator = new JsonGenerator(filename + ".json");

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename + ".json"));


        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
}
