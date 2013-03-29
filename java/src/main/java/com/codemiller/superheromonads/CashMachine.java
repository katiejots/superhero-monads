package com.codemiller.superheromonads;

import static com.codemiller.superheromonads.Maybe.nothing;
import static com.codemiller.superheromonads.Maybe.just;
import static com.codemiller.superheromonads.Maybe.sequence;

/**
 * Example usages of bind and sequence for List and Option.
 *
 * @author Katie Miller (katie@codemiller.com)
 */
public class CashMachine {

    public static <K, V> Maybe<V> lookup(List<Tuple<K, V>> tupleList, K key) {
        return tupleList.foldRight((tuple, acc) -> tuple.first().equals(key) ? Maybe.just(tuple.second()) : acc, (Maybe<V>) Maybe.<V>nothing());
    }

    public static Maybe<Integer> unitsLeft(List<Tuple<Integer, Integer>> currencySupply, Integer value, Integer unitsWanted) {
        return lookup(currencySupply, value)
                .bind(numUnits -> (unitsWanted < 0 || numUnits - unitsWanted < 0) ? Maybe.<Integer>nothing() : just(numUnits - unitsWanted));
    }

    public static <A> List<String> listNotes(List<A> amounts, List<String> currencies) {
        return amounts.bind(amount -> (List<String>) currencies.bind(currency -> List.itemList(currency + amount.toString())));
    }

    public static Maybe<List<Integer>> checkAmountServiceable(List<Tuple<Integer, Integer>> currencySupply, List<Tuple<Integer, Integer>> combination) {
        return sequence(combination.foldRight((tuple, acc) -> List.cons(unitsLeft(currencySupply, tuple.first(), tuple.second()), acc),
                (List<Maybe<Integer>>) List.<Maybe<Integer>>emptyList()));
    }

    public static List<Tuple<Integer, Integer>> createValueUnitTuplesForValue(Integer amount, Integer currencyValue) {
        List<Tuple<Integer, Integer>> result = List.emptyList();
        for (int i = amount/currencyValue; i >= 0; i--) {
            result = List.cons(Tuple.tuple(currencyValue, i), result);
        }
        return result;
    }

    public static List<List<Tuple<Integer, Integer>>> findCombinationSearchSpaceForAmount(Integer amount, List<Integer> machineCurrencies) {
        return List.sequence(machineCurrencies.foldRight((value, acc) -> List.cons(createValueUnitTuplesForValue(amount, value), acc),
                (List<List<Tuple<Integer, Integer>>>) List.<List<Tuple<Integer, Integer>>>emptyList()));
    }

    public static List<List<Tuple<Integer, Integer>>> findCombinationsForAmount(Integer amount, List<Integer> machineCurrencies) {
        return findCombinationSearchSpaceForAmount(amount, machineCurrencies).filter(list ->
                (list.foldRight((tuple, acc) -> acc + (tuple.first() * tuple.second()), 0)).equals(amount));
    }

    public static List<List<Tuple<Integer, Integer>>> findServiceableCombinations(Integer amount, List<Tuple<Integer, Integer>> machineSupply) {
        List<Integer> machineCurrencies = machineSupply.foldRight((tuple, acc) -> List.cons(tuple.first(), acc), (List<Integer>) List.<Integer>emptyList());
        return findCombinationsForAmount(amount, machineCurrencies).filter(combo -> checkAmountServiceable(machineSupply, combo).isDefined());
    }
}
