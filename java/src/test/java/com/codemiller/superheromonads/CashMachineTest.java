package com.codemiller.superheromonads;

import org.junit.Test;

import static com.codemiller.superheromonads.List.itemList;
import static com.codemiller.superheromonads.Tuple.tuple;

/**
 * @author Katie Miller (katie@codemiller.com)
 */
public class CashMachineTest {

    private List<Tuple<Double, Integer>> currencySupply = itemList(tuple(20.0, 5), tuple(50.0, 10), tuple(100.0, 0));

    @Test
    public void testUnitsLeftAfterTakeThreeTwenties() {
        // Given a currency supply with 5 of the currency amount 20.0
        // And that we want three units

        // When unitsLeft is called
        Option<Integer> result = CashMachine.unitsLeft(currencySupply, 20.0, 3);

        // Then we find out this would leave Some(2) units left
        assert (result.equals(Option.some(2)));
    }

    @Test
    public void testUnitsLeftAfterTakeOneHundred() {
        // Given a currency supply with 0 of the currency amount 100.0
        // And that we want one unit

        // When unitsLeft is called
        Option<Integer> result = CashMachine.unitsLeft(currencySupply, 100.0, 1);

        // Then we find out this would result in None
        assert (result.equals(Option.none()));
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
        // Given a currency combination with a value of 70.0
        // And a currency supply
        List<Tuple<Double, Integer>> combination = List.itemList(tuple(20.0, 1), tuple(50.0, 1));

        // When checkAmountServiceable is called
        Option<List<Integer>> result = CashMachine.checkAmountServiceable(currencySupply, combination);

        // Then a Some result showing the number of units of each currency that would remain is returned
        assert (result.equals(Option.some(List.itemList(4, 9))));
    }

    @Test
    public void testOneHundredAndSeventyComboNotServiceable() {
        // Given a currency combination with a value of 170.0 that exceeds the number of units of a currency
        // And a currency supply
        List<Tuple<Double, Integer>> combination = List.itemList(tuple(20.0, 6), tuple(50.0, 1));

        // When checkAmountServiceable is called
        Option<List<Integer>> result = CashMachine.checkAmountServiceable(currencySupply, combination);

        // Then a None result is returned
        assert (result.equals(Option.none()));
    }

    @Test
    public void testCreateValueUnitTuples() {
        // Given an amount and a currency value

        // When createValueUnitTuplesForValue is called
        List<Tuple<Double, Integer>> result = CashMachine.createValueUnitTuplesForValue(100.0, 50.0);

        // Then all the possible pairings are returned
        assert(result.equals(List.itemList(tuple(50.0,0), tuple(50.0,1), tuple(50.0, 2))));
    }

    @Test
    public void testFindAllPossibleCombosForAmount() {
        // Given an amount and a list of currency values

        // When findAllPossibleCombinationsForAmount is called
        List<List<Tuple<Double, Integer>>> result = CashMachine.findAllPossibleCombinationsForAmount(50.0, itemList(20.0, 50.0));

        // Then all possible combinations that could be picked given that amount is the ceiling for any individual currency are returned
        assert(result.equals(itemList(itemList(tuple(20.0, 0), tuple(50.0, 0)),
                                      itemList(tuple(20.0, 0), tuple(50.0, 1)),
                                      itemList(tuple(20.0, 1), tuple(50.0, 0)),
                                      itemList(tuple(20.0, 1), tuple(50.0, 1)),
                                      itemList(tuple(20.0, 2), tuple(50.0, 0)),
                                      itemList(tuple(20.0, 2), tuple(50.0, 1)))));
    }
}
