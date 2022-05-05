package DataClasses;

public class TrafficLight {
    private char color;
    private int duration;

    public TrafficLight(char color, int duration) {
        this.color = color;
        this.duration = duration;
    }

    public char getColor() {
        return color;
    }

    public int getDuration() {
        return duration;
    }
}
