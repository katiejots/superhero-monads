package com.codemiller.superheromonads;

import static com.codemiller.superheromonads.Option.none;
import static com.codemiller.superheromonads.Option.some;

/**
 * Example usages of flatMap and sequence for List and Option.
 *
 * @author Katie Miller (katie@codemiller.com)
 */
public class CashMachine {

    public static <K, V> Option<V> lookup(List<Tuple<K, V>> tupleList, K key) {
        return tupleList.foldRight((tuple, acc) -> tuple.first().equals(key) ? Option.some(tuple.second()) : acc, (Option<V>) Option.none());
    }

    public static Option<Integer> unitsLeft(List<Tuple<Integer, Integer>> currencySupply, Integer value, Integer unitsWanted) {
        Option<Integer> none = none();
        return lookup(currencySupply, value)
                .flatMap(numUnits -> (unitsWanted < 0 || numUnits - unitsWanted < 0) ? none : some(numUnits - unitsWanted));
    }

    public static <A> List<String> listNotes(List<A> amounts, List<String> currencies) {
        return amounts.flatMap(amount -> (List<String>) currencies.flatMap(currency -> List.itemList(currency + amount.toString())));
    }

    public static Option<List<Integer>> checkAmountServiceable(List<Tuple<Integer, Integer>> currencySupply, List<Tuple<Integer, Integer>> combination) {
        List<Option<Integer>> emptyList = List.emptyList();
        return Option.sequence(combination.foldRight((tuple, acc) -> List.cons(unitsLeft(currencySupply, tuple.first(), tuple.second()), acc), emptyList));
    }

    public static List<Tuple<Integer, Integer>> createValueUnitTuplesForValue(Integer amount, Integer currencyValue) {
        List<Tuple<Integer, Integer>> result = List.emptyList();
        for (int i = amount/currencyValue; i >= 0; i--) {
            result = List.cons(Tuple.tuple(currencyValue, i), result);
        }
        return result;
    }

    public static List<List<Tuple<Integer, Integer>>> findCombinationSearchSpaceForAmount(Integer amount, List<Integer> machineCurrencies) {
        List<List<Tuple<Integer, Integer>>> emptyList = List.emptyList();
        return List.sequence(machineCurrencies.foldRight((value, acc) -> List.cons(createValueUnitTuplesForValue(amount, value), acc), emptyList));
    }

    public static List<List<Tuple<Integer, Integer>>> findCombinationsForAmount(Integer amount, List<Integer> machineCurrencies) {
        return findCombinationSearchSpaceForAmount(amount, machineCurrencies).filter(list ->
                (list.foldRight((tuple, acc) -> acc + (tuple.first() * tuple.second()), 0)).equals(amount));
    }

    public static List<List<Tuple<Integer, Integer>>> findServiceableCombinations(Integer amount, List<Tuple<Integer, Integer>> machineSupply) {
        List<Integer> accumulator = List.emptyList();
        List<Integer> machineCurrencies = machineSupply.foldRight((tuple, acc) -> List.cons(tuple.first(), acc), accumulator);
        return findCombinationsForAmount(amount, machineCurrencies).filter(combo -> checkAmountServiceable(machineSupply, combo).isDefined());
    }
}
