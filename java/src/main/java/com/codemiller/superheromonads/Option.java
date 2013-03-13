package com.codemiller.superheromonads;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
 * @author Katie Miller (katie@codemiller.com)
 */
public abstract class Option<A> implements Iterable<A> {

    private Option() { }

    public abstract <B> Option<B> map(Function<? super A, ? extends B> mapper);

    public abstract <B> Option<B> flatMap(Function<? super A, ? extends Option<B>> mapper);

    public abstract Option<A> filter(Predicate<? super A> predicate);

    public abstract A getOrElse(A def);

    public abstract boolean isDefined();

    public static <A> Some<A> some(A value) {
        if (value == null) throw new NullPointerException();
        return new Some<A>(value);
    }

    public static <A> None<A> none() {
        return NONE;
    }

    public static <A> Option<A> option(A value) {
        if (value == null) return none(); else return some(value);
    }

    public static final None NONE = new None();

    public static final class None<A> extends Option<A> {

        @Override
        public <B> Option<B> map(Function<? super A, ? extends B> mapper) {
            return NONE;
        }

        @Override
        public <B> Option<B> flatMap(Function<? super A, ? extends Option<B>> mapper) {
            return NONE;
        }

        @Override
        public Option<A> filter(Predicate<? super A> predicate) {
            return NONE;
        }

        @Override
        public A getOrElse(A def) {
            return def;
        }

        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public Iterator<A> iterator() {
            return (Iterator<A>) NONE_ITERATOR;
        }

        @Override
        public String toString() {
            return "None";
        }

        private static final Iterator<?> NONE_ITERATOR = new NoneIterator();

        private static class NoneIterator<A> implements Iterator<A> {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public A next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static final class Some<A> extends Option<A> {

        private final A value;

        private Some(A value) {
            this.value = value;
        }

        @Override
        public <B> Option<B> map(Function<? super A, ? extends B> mapper) {
            return some(mapper.apply(value));
        }

        @Override
        public <B> Option<B> flatMap(Function<? super A, ? extends Option<B>> mapper) {
            return (Option<B>) mapper.apply(value);
        }

        @Override
        public Option<A> filter(Predicate<? super A> predicate) {
            if (predicate.test(value)) return this; else return None.NONE;
        }

        @Override
        public A getOrElse(A def) {
            return value;
        }

        @Override
        public boolean isDefined() {
            return true;
        }

        @Override
        public Iterator<A> iterator() {
            return new SomeIterator<A>(value);
        }

        @Override
        public String toString() {
            return "Some( " + value + ")";
        }

        private static class SomeIterator<A> implements Iterator<A> {

            private final A value;
            private boolean hasNext = true;

            private SomeIterator(A value) {
                this.value = value;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public A next() {
                if (!hasNext) throw new NoSuchElementException();
                hasNext = false;
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
