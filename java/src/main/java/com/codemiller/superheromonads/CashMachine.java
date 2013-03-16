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

    public static Option<Integer> unitsLeft(List<Tuple<Double, Integer>> currencySupply, Double value, Integer unitsWanted) {
        Option<Integer> none = none();
        return lookup(currencySupply, value)
                .flatMap(numUnits -> (unitsWanted < 0 || numUnits - unitsWanted < 0) ? none : some(numUnits - unitsWanted));
    }

    public static <A> List<String> listNotes(List<A> amounts, List<String> currencies) {
        return amounts.flatMap(amount -> (List<String>) currencies.flatMap(currency -> List.itemList(currency + amount.toString())));
    }

    public static Option<List<Integer>> checkAmountServiceable(List<Tuple<Double, Integer>> currencySupply, List<Tuple<Double, Integer>> combination) {
        List<Option<Integer>> emptyList = List.emptyList();
        return Option.sequence(combination.foldRight((tuple, acc) -> List.cons(unitsLeft(currencySupply, tuple.first(), tuple.second()), acc), emptyList));
    }

    public static List<Tuple<Double, Integer>> createValueUnitTuplesForValue(Double amount, Double currencyValue) {
        List<Tuple<Double, Integer>> result = List.emptyList();
        for (int i = new Double(Math.floor(amount / currencyValue)).intValue(); i >= 0; i--) {
            result = List.cons(Tuple.tuple(currencyValue, i), result);
        }
        return result;
    }

    public static List<List<Tuple<Double, Integer>>> findAllPossibleCombinationsForAmount(Double amount, List<Double> machineCurrencies) {
        List<List<Tuple<Double, Integer>>> emptyList = List.emptyList();
        return List.sequence(machineCurrencies.foldRight((value, acc) -> List.cons(createValueUnitTuplesForValue(amount, value), acc), emptyList));
    }
}
