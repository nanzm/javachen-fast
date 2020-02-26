package com.javachen.common.utils;

/**
 * This Tuple class can be used when you want to return two elements from a function in a type safe way.
 *
 * @author Andre Azzolini (apazzolini)
 */
public class Tuple<A, B> {

    protected final A one;
    protected final B two;

    public Tuple(A one, B two) {
        this.one = one;
        this.two = two;
    }

    public A getFirst() {
        return one;
    }

    public B getSecond() {
        return two;
    }

}
