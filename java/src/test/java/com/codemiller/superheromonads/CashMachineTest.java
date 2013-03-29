package com.codemiller.superheromonads;

import org.junit.Test;

import static com.codemiller.superheromonads.List.itemList;
import static com.codemiller.superheromonads.Maybe.just;
import static com.codemiller.superheromonads.Maybe.nothing;
import static com.codemiller.superheromonads.Tuple.tuple;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public class CashMachineTest {

    private List<Tuple<Integer, Integer>> currencySupply = itemList(tuple(20, 5), tuple(50, 10), tuple(100, 0));

    @Test
    public void testUnitsLeftAfterTakeThreeTwenties() {
        // Given a currency supply with 5 of the currency amount 20
        // And that we want three units

        // When unitsLeft is called
        Maybe<Integer> result = CashMachine.unitsLeft(currencySupply, 20, 3);

        // Then we find out this would leave Just(2) units left
        assert (result.equals(just(2)));
    }

    @Test
    public void testUnitsLeftAfterTakeOneHundred() {
        // Given a currency supply with 0 of the currency amount 100
        // And that we want one unit

        // When unitsLeft is called
        Maybe<Integer> result = CashMachine.unitsLeft(currencySupply, 100, 1);

        // Then we find out this would result in Nothing
        assert (result.equals(nothing()));
    }

    @Test
    public void testListMachineNotes() {
        // Given a currency supply
        // And two different currencies
        List<String> currencies = itemList("$AU", "$NZ");

        // When listNotes is called
        List<String> result = CashMachine.listNotes(itemList(20, 50, 100), currencies);

        // Then all the possible currency-value combinations are produced
        assert (result.equals(itemList("$AU20", "$NZ20", "$AU50", "$NZ50", "$AU100", "$NZ100")));
    }

    @Test
    public void testSeventyComboServiceable() {
        // Given a currency combination with a value of 70
        // And a currency supply
        List<Tuple<Integer, Integer>> combination = List.itemList(tuple(20, 1), tuple(50, 1));

        // When checkAmountServiceable is called
        Maybe<List<Integer>> result = CashMachine.checkAmountServiceable(currencySupply, combination);

        // Then a Just result showing the number of units of each currency that would remain is returned
        assert (result.equals(just(List.itemList(4, 9))));
    }

    @Test
    public void testOneHundredAndSeventyComboNotServiceable() {
        // Given a currency combination with a value of 170 that exceeds the number of units of a currency
        // And a currency supply
        List<Tuple<Integer, Integer>> combination = List.itemList(tuple(20, 6), tuple(50, 1));

        // When checkAmountServiceable is called
        Maybe<List<Integer>> result = CashMachine.checkAmountServiceable(currencySupply, combination);

        // Then a Nothing result is returned
        assert (result.equals(nothing()));
    }

    @Test
    public void testCreateValueUnitTuples() {
        // Given an amount and a currency value

        // When createValueUnitTuplesForValue is called
        List<Tuple<Integer, Integer>> result = CashMachine.createValueUnitTuplesForValue(100, 50);

        // Then all the possible pairings are returned
        assert (result.equals(List.itemList(tuple(50, 0), tuple(50, 1), tuple(50, 2))));
    }

    @Test
    public void testFindAllPossibleCombosForAmount() {
        // Given an amount and a list of currency values

        // When findCombinationSearchSpaceForAmount is called
        List<List<Tuple<Integer, Integer>>> result = CashMachine.findCombinationSearchSpaceForAmount(50, itemList(20, 50));

        // Then all possible combinations that could be picked, given that amount is the ceiling for any individual currency, are returned
        assert (result.equals(itemList(itemList(tuple(20, 0), tuple(50, 0)),
                itemList(tuple(20, 0), tuple(50, 1)),
                itemList(tuple(20, 1), tuple(50, 0)),
                itemList(tuple(20, 1), tuple(50, 1)),
                itemList(tuple(20, 2), tuple(50, 0)),
                itemList(tuple(20, 2), tuple(50, 1)))));
    }

    @Test
    public void testFindCombosForAmount() {
        // Given an amount and a list of currency values

        // When findCombinationsForAmount is called
        List<List<Tuple<Integer, Integer>>> result = CashMachine.findCombinationsForAmount(100, itemList(20, 50, 100));

        // Then all combinations that equal that amount are returned
        assert (result.equals(itemList(itemList(tuple(20, 0), tuple(50, 0), tuple(100, 1)),
                itemList(tuple(20, 0), tuple(50, 2), tuple(100, 0)),
                itemList(tuple(20, 5), tuple(50, 0), tuple(100, 0)))));
    }

    @Test
    public void testFindServiceableCombos() {
        // Given an amount and a machine currency supply

        // When findServiceableCombinations is called
        List<List<Tuple<Integer, Integer>>> result = CashMachine.findServiceableCombinations(100, currencySupply);

        // Then all combinations that are serviceable for that amount are returned
        assert (result.equals(itemList(itemList(tuple(20, 0), tuple(50, 2), tuple(100, 0)),
                itemList(tuple(20, 5), tuple(50, 0), tuple(100, 0)))));
    }
}
