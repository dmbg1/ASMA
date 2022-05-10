package DataClasses;

public class TrafficLight {
    private char color;
    private int duration;
    private char orientation;

    public TrafficLight(char color, int duration, char orientation) {
        this.color = color;
        this.duration = duration;
        this.orientation = orientation;
    }

    public void alternateColor(int duration) {
        this.color = this.color == 'g' ? 'r' : 'g';
        this.duration = duration;
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
