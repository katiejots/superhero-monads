package com.codemiller.superheromonads;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public class Tuple<A, B> {

    private final A a;
    private final B b;

    private Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple<A, B> tuple(A a, B b) {
        return new Tuple<>(a, b);
    }

    public A first() {
        return a;
    }

    public B second() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a +
                "," + b +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        return a.equals(tuple.a) && b.equals(tuple.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }
}
