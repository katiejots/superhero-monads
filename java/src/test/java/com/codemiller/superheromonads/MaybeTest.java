package com.codemiller.superheromonads;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.codemiller.superheromonads.List.itemList;
import static com.codemiller.superheromonads.Maybe.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
 * @author Katie Miller (katie@codemiller.com)
 */
public class MaybeTest {

    @Test
    public void testMap() {
        Map<String, String> param = new HashMap<>();
        param.put("a", "5");
        param.put("b", "true");
        param.put("c", "-3");

        assertEquals(5, readPositiveIntParam(param, "a"));
        assertEquals(0, readPositiveIntParam(param, "b"));
        assertEquals(0, readPositiveIntParam(param, "c"));
        assertEquals(0, readPositiveIntParam(param, "d"));
    }

    public int readPositiveIntParam(Map<String, String> params, String name) {
        return fromNullable(params.get(name)).bind(MaybeTest::stringToInt).filter(i -> i > 0).getOrElse(0);
    }

    public static Maybe<Integer> stringToInt(String s) {
        try {
            return just(Integer.parseInt(s));
        } catch (NumberFormatException nfe) {
            return nothing();
        }
    }

    @Test
    public void testLift() {
        // Given a regular function that takes two arguments, and two maybe types containing those type of values
        BiFunction<Integer, Integer, Integer> function = (a, b) -> a + b;
        Maybe<Integer> maybe = just(1);
        Maybe<Integer> maybe2 = just(2);

        // When lift is called
        Maybe<Integer> result = maybe.lift(function, maybe2);

        // Then the function is lifted into the maybe context and applied to the arguments
        assert (result.equals(just(3)));
    }

    @Test
    public void testSequence() {
        // Given a list of maybes
        List<Maybe<Integer>> list = itemList(fromNullable(1), fromNullable(2), fromNullable(3));

        // When sequence is called with that list
        Maybe<List<Integer>> result = Maybe.sequence(list);

        // Then the result is a maybe type containing a list of the maybe type contents
        assert (result.equals(just(itemList(1, 2, 3))));
    }
}
