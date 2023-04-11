package Utillity;

public class Pair<T,U> {
    private T first;
    private U second;

    public Pair(T t, U u){
        this.first = t;
        this.second = u;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(U second) {
        this.second = second;
    }

}
