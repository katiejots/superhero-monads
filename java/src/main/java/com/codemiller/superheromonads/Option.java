package com.codemiller.superheromonads;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.codemiller.superheromonads.List.emptyList;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
 * @author Katie Miller (katie@codemiller.com)
 */
public abstract class Option<A> {

    private Option() {
    }

    public abstract <B> Option<B> map(Function<? super A, ? extends B> mapper);

    public abstract <B> Option<B> flatMap(Function<? super A, ? extends Option<B>> mapper);

    public abstract Option<A> filter(Predicate<? super A> predicate);

    public abstract A getOrElse(A def);

    public abstract boolean isDefined();

    public abstract <B, C> Option<C> lift(BiFunction<? super A, ? super B, ? super C> function, Option<B> option);

    public static <A> Some<A> some(A value) {
        if (value == null) throw new NullPointerException();
        return new Some<A>(value);
    }

    public static <A> None<A> none() {
        return NONE;
    }

    public static <A> Option<A> option(A value) {
        if (value == null) return none();
        else return some(value);
    }

    public static <A> Option<List<A>> sequence(List<Option<A>> list) {
        return list.foldRight((o, a) -> o.lift(List::cons, a), (Option<List<A>>) some((List<A>) emptyList()));
    }

    public static final None NONE = new None();

    private static final class None<A> extends Option<A> {

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
        public <B, C> Option<C> lift(BiFunction<? super A, ? super B, ? super C> function, Option<B> option) {
            return NONE;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof None && obj == NONE;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "None";
        }
    }

    private static final class Some<A> extends Option<A> {

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
            if (predicate.test(value)) return this;
            else return None.NONE;
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
        public <B, C> Option<C> lift(BiFunction<? super A, ? super B, ? super C> function, Option<B> option) {
            return this.flatMap(a -> (Option<C>) option.flatMap(b -> Option.some(function.apply(a, b))));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Some some = (Some) o;

            if (!value.equals(some.value)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "Some(" + value + ")";
        }
    }
}
