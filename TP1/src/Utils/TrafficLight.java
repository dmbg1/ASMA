package Utils;

public class TrafficLight {

    private static int current_id;
    private final String nameId;
    private char color;


    public TrafficLight(char color) {
        this.color = color;
        current_id++;
        this.nameId = "tl" + current_id;
    }

    public void alternateColor() {
        this.color = this.color == 'g' ? 'r' : 'g';
    }

    public String getNameId() {
        return nameId;
    }

    public char getColor() {
        return color;
    }
}
