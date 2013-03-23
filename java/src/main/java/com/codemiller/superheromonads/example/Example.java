package com.codemiller.superheromonads.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Simple example showing lambda expressions and method reference syntax.
 *
 * @author Katie Miller (katie@codemiller.com)
 */
public class Example {

    public static void main(String[] args) {
        System.out.println(map(Arrays.asList(1, 2, 3), i -> i + 1));
        System.out.println(map(Arrays.asList(-1, 2, -3), Math::abs));
    }

    public static List<Integer> map(List<Integer> list, Function<Integer,Integer> func) {
        List<Integer> result = new ArrayList<>();
        for (Integer num : list) {
            result.add(func.apply(num));
        }
        return result;
    }
}
