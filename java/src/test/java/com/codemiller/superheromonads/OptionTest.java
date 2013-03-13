package com.codemiller.superheromonads;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.codemiller.superheromonads.Option.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mario Fusco (see http://github.com/mariofusco/javaz)
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
}
