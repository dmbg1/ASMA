package Utils;

public class TrafficLight {

    private static int current_id;
    private String nameId;
    private char color;
    private int duration;
    private char orientation;

    public TrafficLight(char color, int duration, char orientation) {
        this.color = color;
        this.duration = duration;
        this.orientation = orientation;
        current_id++;
        this.nameId = "tl" + current_id;

        //System.out.println("TL => " + this.nameId + " ori: " + this.orientation + " color: " + this.color);
    }

    public void alternateColor(int duration) {
        this.color = this.color == 'g' ? 'r' : 'g';
        this.duration = duration;
    }

    public String getNameId() {
        return nameId;
    }

    public char getColor() {
        return color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
