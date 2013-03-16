package com.codemiller.superheromonads;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
public class CashMachineTest {

    private ImmutableMap<Integer, Integer> currencySupply = ImmutableMap.of(20, 5, 50, 10, 100, 0);

    @Test
    public void testUnitsLeftAfterTakeThreeTwenties() {
        // Given a currency supply with 5 of the currency amount 20
        // And that we want three units

        // When unitsLeft is called
        Option<Integer> result = CashMachine.unitsLeft(currencySupply, 20, 3);

        // Then we find out this would leave Some(2) units left
        assert(result.equals(Option.some(2)));
    }

    @Test
    public void testUnitsLeftAfterTakeOneHundred() {
        // Given a currency supply with 0 of the currency amount 100
        // And that we want one unit

        // When unitsLeft is called
        Option<Integer> result = CashMachine.unitsLeft(currencySupply, 100, 1);

        // Then we find out this would result in None
        assert(result.equals(Option.none()));
    }

    @Test
    public void testListMachineNotes() {
        // Given a currency supply
        // And two different currencies
        List<String> currencies = List.itemList("$AU", "$NZ");

        // When listNotes is called
        List<String> result = CashMachine.listNotes(List.itemList(20, 50, 100), currencies);

        // Then all the possible currency-value combinations are produced
        assert(result.equals(List.itemList("$AU20", "$NZ20", "$AU50", "$NZ50", "$AU100", "$NZ100")));
    }
}
