package com.codemiller.superheromonads;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.codemiller.superheromonads.List.itemList;
import static com.codemiller.superheromonads.Option.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
 * @author Katie Miller (katie@codemiller.com)
 */
public class OptionTest {

    @Test
    public void testMap() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("a", "5");
        param.put("b", "true");
        param.put("c", "-3");

        assertEquals(5, readPositiveIntParam(param, "a"));
        assertEquals(0, readPositiveIntParam(param, "b"));
        assertEquals(0, readPositiveIntParam(param, "c"));
        assertEquals(0, readPositiveIntParam(param, "d"));
    }

    public int readPositiveIntParam(Map<String, String> params, String name) {
        return option(params.get(name)).flatMap(OptionTest::stringToInt).filter(i -> i > 0).getOrElse(0);
    }

    public static Option<Integer> stringToInt(String s) {
        try {
            return some(Integer.parseInt(s));
        } catch (NumberFormatException nfe) {
            return none();
        }
    }

    @Test
    public void testLift() {
        // Given a regular function that takes two arguments, and two options containing those type of values
        BiFunction<Integer, Integer, Integer> function = (a, b) -> a + b;
        Option<Integer> option = some(1);
        Option<Integer> option2 = some(2);

        // When lift is called
        Option<Integer> result = option.lift(function, option2);

        // Then the function is lifted into the option context and applied to the arguments
        assert (result.equals(some(3)));
    }

    @Test
    public void testSequence() {
        // Given a list of options
        List<Option<Integer>> list = itemList(option(1), option(2), option(3));

        // When sequence is called with that list
        Option<List<Integer>> result = Option.sequence(list);

        // Then the result is an option containing a list of the option contents
        assert (result.equals(some(itemList(1, 2, 3))));
    }
}
