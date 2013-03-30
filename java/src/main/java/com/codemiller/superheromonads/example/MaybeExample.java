package com.codemiller.superheromonads.example;

import com.codemiller.superheromonads.Maybe;

import static com.codemiller.superheromonads.Maybe.just;
import static com.codemiller.superheromonads.Maybe.nothing;

/**
 * Simple example contrasting non-monadic approach using null, with use of Maybe and bind.
 *
 * @author Katie Miller (katie@codemiller.com)
 */
public class MaybeExample {
    public static void main(String[] args) {

        assert(incrementAndMultiplyInteger(7).equals(40));
        assert(incrementAndMultiplyInteger(null) == null);

        if (incrementAndMultiplyInteger(7) != null) {
            System.out.println("We got: " + incrementAndMultiplyInteger(7));
        }

        assert(incrementAndMultiplyIntegerWithMaybeMonad(just(7)).equals(just(40)));
        assert(incrementAndMultiplyIntegerWithMaybeMonad(Maybe.<Integer>nothing()).equals(nothing()));

        if (incrementAndMultiplyIntegerWithMaybeMonad(just(7)).isDefined()) {
            System.out.println("We got: " + incrementAndMultiplyIntegerWithMaybeMonad(just(7)));
        }
    }

    public static Integer incrementAndMultiplyInteger(Integer num) {
        if (num == null) return null;
        // Or perhaps throw new IllegalArgumentException("Num cannot be null!");

        Integer incrementedNum = increment(num);

        // Of perhaps this would be in a try-catch block
        if (incrementedNum == null) return null;

        Integer multipliedNum = multiplyByFive(incrementedNum);

        // Of perhaps this would be in a try-catch block
        if (multipliedNum == null) return null;
        return multipliedNum;
    }

    public static Integer increment(Integer num) {
        return (num == null) ? null : num + 1;
    }

    public static Integer multiplyByFive(Integer num) {
        return (num == null) ? null : num * 5;
    }

    public static Maybe<Integer> incrementAndMultiplyIntegerWithMaybeMonad(Maybe<Integer> maybeNum) {
        return maybeNum.bind(x -> just(x + 1)).bind(y -> just(y * 5));
    }
}
