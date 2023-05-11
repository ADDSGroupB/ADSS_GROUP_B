package Utillity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return pair.first.equals(first) && pair.second.equals(second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
