package Utils;

public class Pair<T, G> {

    private T num1;
    private G num2;

    public Pair(T num1, G num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public G getNum2() {
        return num2;
    }

    public T getNum1() {
        return num1;
    }
}
