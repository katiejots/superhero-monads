package com.codemiller.superheromonads;

import org.junit.Test;

import java.util.function.BiFunction;

import static com.codemiller.superheromonads.List.cons;
import static com.codemiller.superheromonads.List.itemList;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public class ListTest {

    @Test
    public void testAppend() {
        // Given two lists
        List<Integer> list = itemList(1, 2, 3);
        List<Integer> list2 = itemList(4, 5);

        // When the second list is appended to the first
        List<Integer> result = list.append(list2);

        // Then the result is equal to the two lists joined together
        assert(result.equals(itemList(1, 2, 3, 4, 5)));
    }

    @Test
    public void testFoldRight() {
        // Given a list
        List<Integer> list = itemList(1, 2, 3);

        // When an increment function is folded across it, building up the result using cons
        List<Integer> acc = List.emptyList();
        List<Integer> result = list.foldRight((integer, accumulator) -> cons(integer + 1, accumulator), acc);

        // Then each of the elements is incremented
        assert(result.equals(itemList(2, 3, 4)));
    }

    @Test
    public void testFilter() {
        // Given a list
        List<Integer> list = itemList(1, 2, 3);

        // When it is filtered with a predicate
        List<Integer> result = list.filter(i -> i % 2 == 0);

        // Then results that don't meet that predicate are not present in the resulting list
        assert(result.equals(itemList(2)));
    }

    @Test
    public void testMap() {
        // Given a list
        List<Integer> list = itemList(1, 2, 3);

        // When an increment function is mapped across it
        List<Integer> result = list.map(i -> i + 1);

        // Then each of the elements is incremented
        assert(result.equals(itemList(2, 3, 4)));
    }

    @Test
    public void testBind() {
        // Given a list
        List<Integer> list = itemList(1, 2, 3);

        // When a function is bound over it
        List<Integer> result = list.bind(integer -> itemList(integer * 2));

        // Then the result is as expected
        assert(result.equals(itemList(2, 4, 6)));
    }

    @Test
    public void testLift2() {
        // Given a regular function that takes two arguments, and two lists of those values
        BiFunction<Integer, Integer, Integer> function = (a, b) -> a + b;
        List<Integer> list = itemList(1, 2, 3);
        List<Integer> list2 = itemList(4, 5, 6);

        // When lift2 is called
        List<Integer> result = list.lift2(function, list2);

        // Then the function is lifted into the list context and applied to the arguments
        assert(result.equals(itemList(5, 6, 7, 6, 7, 8, 7, 8, 9)));
    }

    @Test
    public void testSequence() {
        // Given a list containing two lists of values
        List<List<Integer>> list = itemList(itemList(1, 2), itemList(3, 4));

        // When sequence is called with that list
        List<List<Integer>> result = List.sequence(list);

        // Then the result is a list containing all the pairings of elements from different sublists
        assert(result.equals(itemList(itemList(1, 3), itemList(1, 4), itemList(2, 3), itemList(2, 4))));
    }
}
