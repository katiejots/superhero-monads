package com.codemiller.superheromonads;

import java.util.Map;

import static com.codemiller.superheromonads.Option.*;

/**
 * Example usages of flatMap and sequence for List and Option.
 *
 * @author kamiller@redhat.com (Katie Miller)
 */
public class CashMachine {

    public static <K, V> Option<V> doLookup(Map<K, V> map, K key) {
        return option(map.get(key));
    }

    public static Option<Integer> unitsLeft(Map<Integer, Integer> currencySupply, Integer value, Integer unitsWanted) {
        Option<Integer> none = none();
        return doLookup(currencySupply, value)
                .flatMap(numUnits -> (unitsWanted < 0 || numUnits - unitsWanted < 0) ? none : some(numUnits - unitsWanted));
    }

    public static <A> List<String> listNotes(List<A> amounts, List<String> currencies) {
        return amounts.flatMap(amount -> (List<String>) currencies.flatMap(currency -> List.itemList(currency + amount.toString())));
    }
}
