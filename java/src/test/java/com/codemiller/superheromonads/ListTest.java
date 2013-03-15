package com.codemiller.superheromonads;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.codemiller.superheromonads.List.cons;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public class ListTest {

    @Test
    public void testAppend() {
        // Given two lists
        List<Integer> list = List.itemList(1, 2, 3);
        List<Integer> list2 = List.itemList(4, 5);

        // When the second list is appended to the first
        List<Integer> result = list.append(list2);

        // Then the result is equal to the two lists joined together
        assert(result.equals(List.itemList(1, 2, 3, 4, 5)));
    }

    @Test
    public void testFoldRight() {
        // Given a list
        List<Integer> list = List.itemList(1, 2, 3);

        // When an increment function is folded across it, building up the result using cons
        List<Integer> acc = List.emptyList();
        List<Integer> result = list.foldRight((integer, accumulator) -> cons(integer + 1, accumulator), acc);

        // Then each of the elements is incremented
        assert(result.equals(List.itemList(2, 3, 4)));
    }

    @Test
    public void testFilter() {
        // Given a list
        List<Integer> list = List.itemList(1, 2, 3);

        // When it is filtered with a predicate
        List<Integer> result = list.filter(i -> i % 2 == 0);

        // Then results that don't meet that predicate are not present in the resulting list
        assert(result.equals(List.itemList(2)));
    }

    @Test
    public void testMap() {
        // Given a list
        List<Integer> list = List.itemList(1, 2, 3);

        // When an increment function is mapped across it
        List<Integer> result = list.map(i -> i + 1);

        // Then each of the elements is incremented
        assert(result.equals(List.itemList(2, 3, 4)));
    }

    @Test
    public void testFlatMap() {
        // Given a list
        List<Integer> list = List.itemList(1, 2, 3);

        // When a function is flatmapped over it
        List<Integer> result = list.flatMap(integer -> List.itemList(integer * 2));

        // Then the result is as expected
        assert(result.equals(List.itemList(2, 4, 6)));
    }

    @Test
    public void testApply() {
        // Given a list with some values in it, and a list with a function in it that requires one value
        List<Integer> list = List.itemList(1, 2, 3);
        List<Function<Integer, Integer>> functionList = List.itemList(i -> i + 1);

        // When apply is called on that value list
        List<Integer> result = list.apply(functionList);

        // Then the function is applied to the value and the wrapped in a list
        assert(result.equals(List.itemList(2, 3, 4)));
    }

    @Test
    public void testLift() {
        // Given a regular function that takes two arguments, and two lists of those values
        BiFunction<Integer, Integer, Integer> function = (a, b) -> a + b;
        List<Integer> list = List.itemList(1, 2, 3);
        List<Integer> list2 = List.itemList(4, 5, 6);

        // When lift is called
        List<Integer> result = list.lift(function, list2);

        // Then the function is lifted into the list context and applied to the arguments
        assert(result.equals(List.itemList(5, 6, 7, 6, 7, 8, 7, 8, 9)));
    }
}
