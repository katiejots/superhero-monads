package com.codemiller.superheromonads;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
 * @author Katie Miller (katie@codemiller.com)
 */
public abstract class Maybe<A> {

    private Maybe() {
    }

    public abstract <B> Maybe<B> map(Function<A, B> func);

    public abstract <B> Maybe<B> bind(Function<A, Maybe<B>> func);

    public abstract Maybe<A> filter(Predicate<A> predicate);

    public abstract A getOrElse(A def);

    public abstract boolean isDefined();

    public abstract <B, C> Maybe<C> lift2(BiFunction<A, B, C> func, Maybe<B> maybe);

    public static <A> Just<A> just(A value) {
        return new Just<>(value);
    }

    public static <A> Nothing<A> nothing() {
        return new Nothing<>();
    }

    public static <A> Maybe<A> fromNullable(A value) {
        if (value == null) return nothing();
        else return just(value);
    }

    public static <A> Maybe<List<A>> sequence(List<Maybe<A>> list) {
        return list.foldRight((o, a) -> o.lift2(List::cons, a), (Maybe<List<A>>) Maybe.<List<A>>just(List.<A>emptyList()));
    }

    private static final class Nothing<A> extends Maybe<A> {

        @Override
        public <B> Maybe<B> map(Function<A, B> func) {
            return nothing();
        }

        @Override
        public <B> Maybe<B> bind(Function<A, Maybe<B>> func) {
            return nothing();
        }

        @Override
        public Maybe<A> filter(Predicate<A> predicate) {
            return nothing();
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
        public <B, C> Maybe<C> lift2(BiFunction<A, B, C> func, Maybe<B> maybe) {
            return nothing();
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass());
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    private static final class Just<A> extends Maybe<A> {

        private final A value;

        private Just(A value) {
            this.value = value;
        }

        @Override
        public <B> Maybe<B> map(Function<A, B> func) {
            return just(func.apply(value));
        }

        @Override
        public <B> Maybe<B> bind(Function<A, Maybe<B>> func) {
            return func.apply(value);
        }

        @Override
        public Maybe<A> filter(Predicate<A> predicate) {
            if (predicate.test(value)) return this;
            else return nothing();
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
        public <B, C> Maybe<C> lift2(BiFunction<A, B, C> func, Maybe<B> maybe) {
            return this.bind(a -> (Maybe<C>) maybe.bind(b -> just(func.apply(a, b))));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Just just = (Just) o;

            return value.equals(just.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "Just(" + value + ")";
        }
    }
}
