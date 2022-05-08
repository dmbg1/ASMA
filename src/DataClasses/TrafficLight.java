package DataClasses;

public class TrafficLight {
    private char color;
    private int duration;
    private char orientation;

    public TrafficLight(char orientation) {
        this.orientation = orientation;
    }

    public void alternateColor() {
        this.color = this.color == 'g' ? 'r' : 'g';
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
