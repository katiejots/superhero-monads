package com.codemiller.superheromonads;

import org.junit.Test;

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
        List<Integer> result = list.filter(integer -> integer % 2 == 0);

        // Then results that don't meet that predicate are not present in the resulting list
        assert(result.equals(List.itemList(2)));
    }

    @Test
    public void testMap() {
        // Given a list
        List<Integer> list = List.itemList(1, 2, 3);

        // When an increment function is mapped across it
        List<Integer> result = list.map(integer -> integer + 1);

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
}
