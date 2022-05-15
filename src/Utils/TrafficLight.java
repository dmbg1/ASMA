package Utils;

public class TrafficLight {

    private static int current_id;
    private String nameId;
    private char color;
    private char orientation;


    public TrafficLight(char color, char orientation) {
        this.color = color;
        this.orientation = orientation;
        current_id++;
        this.nameId = "tl" + current_id;

        //System.out.println("TL => " + this.nameId + " ori: " + this.orientation + " color: " + this.color);
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

    public void setColor(char color) {
        this.color = color;
    }
}
