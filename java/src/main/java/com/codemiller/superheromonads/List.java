package com.codemiller.superheromonads;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public abstract class List<A> {
    private List() {
    }

    public abstract <B> List<B> map(Function<A, B> func);

    public abstract <B> List<B> bind(Function<A, List<B>> func);

    public abstract List<A> filter(Predicate<A> predicate);

    public abstract boolean isEmpty();

    public abstract <B> B foldRight(BiFunction<A, B, B> function, B accumulator);

    public abstract List<A> append(List<A> list);

    public abstract <B, C> List<C> lift(BiFunction<A, B, C> function, List<B> list);

    public abstract <B> List<B> apply(List<Function<A, B>> functionList);

    public static <A> EmptyList<A> emptyList() {
        return EMPTY_LIST;
    }

    @SafeVarargs
    public static <A> List<A> itemList(A... values) {
        if (values.length == 0) {
            return EMPTY_LIST;
        }
        List<A> list = EMPTY_LIST;
        for (int i = values.length - 1; i >= 0; i--) {
            list = cons(values[i], list);
        }
        return list;
    }

    public static <A> List<A> cons(A value, List<A> list) {
        return new ItemList<A>(value, list);
    }

    public static <A> List<List<A>> sequence(List<List<A>> list) {
        return list.foldRight((l, a) -> l.lift(List::cons, a), itemList((List<A>) EMPTY_LIST));
    }

    public static final EmptyList EMPTY_LIST = new EmptyList();

    private static class EmptyList<A> extends List<A> {
        @Override
        public <B> List<B> map(Function<A, B> func) {
            return EMPTY_LIST;
        }

        @Override
        public <B> List<B> bind(Function<A, List<B>> func) {
            return EMPTY_LIST;
        }

        @Override
        public List<A> filter(Predicate<A> predicate) {
            return EMPTY_LIST;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public <B> B foldRight(BiFunction<A, B, B> func, B accumulator) {
            return accumulator;
        }

        @Override
        public List<A> append(List<A> list) {
            return list;
        }

        @Override
        public <B, C> List<C> lift(BiFunction<A, B, C> func, List<B> list) {
            return EMPTY_LIST;
        }

        @Override
        public <B> List<B> apply(List<Function<A, B>> functionList) {
            return EMPTY_LIST;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof EmptyList && obj == EMPTY_LIST;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "EmptyList";
        }
    }

    private static class ItemList<A> extends List<A> {

        private final A value;
        private final List<A> next;

        private ItemList(A value, List<A> next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public <B> List<B> map(Function<A, B> func) {
            return new ItemList<B>(func.apply(value), next.map(func));
        }

        @Override
        public <B> List<B> bind(Function<A, List<B>> func) {
            return foldRight((value, accumulator) -> (func.apply(value)).append(accumulator), (List<B>) EMPTY_LIST);
        }

        @Override
        public List<A> filter(Predicate<A> predicate) {
            return (predicate.test(value))
                    ? new ItemList<>(value, next.filter(predicate))
                    : next.filter(predicate);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public <B> B foldRight(BiFunction<A, B, B> func, B accumulator) {
            return func.apply(value, next.foldRight(func, accumulator));
        }

        @Override
        public List<A> append(List<A> list) {
            return new ItemList<>(value, next.foldRight(List::cons, list));
        }

        @Override
        public <B, C> List<C> lift(BiFunction<A, B, C> function, List<B> list) {
            return this.bind(a -> (List<C>) list.bind(b -> List.itemList(function.apply(a, b))));
        }

        @Override
        public <B> List<B> apply(List<Function<A, B>> functionList) {
            return functionList.bind(this::map);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemList itemList = (ItemList) o;

            if (next != null ? !next.equals(itemList.next) : itemList.next != null) return false;
            if (value != null ? !value.equals(itemList.value) : itemList.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = value != null ? value.hashCode() : 0;
            result = 31 * result + (next != null ? next.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "[" + value + "]:" + next;
        }
    }
}
