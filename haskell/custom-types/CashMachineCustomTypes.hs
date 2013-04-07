-- Module giving example uses of Option and List types

module CashMachineCustomTypes where

import Option
import List

-- Preconstructed list of currency supply, for convenience
currencySupply = (20, 5) :| (50, 10) :| (100, 0) :| Nil

-- Method to find the value Option 'b' for a key 'a' in a list of tuples
-- We could use the function 'lookup' for this if we were using the standard list type (or we could use a map)
listLookup :: Eq a => a -> List (a, b) -> Option b
listLookup key Nil = None
listLookup key ((x,y) :| xs)
    | key == x = Some y
    | otherwise = listLookup key xs

-- Function returning the number of units that would be left of a currency value if we took away a given number of units
unitsLeft :: List (Int,Int) -> Int -> Int -> Option Int
unitsLeft supply val num = (listLookup val supply) `flatMapOption` (\u -> if num < 0 || u - num < 0 then None else returnOption (u - num))

-- Function to list all the notes our machine can supply, given its currency values and currencies
listNotes :: List Int -> List String -> List String
listNotes amts curs = amts `flatMapList` (\amt -> curs `flatMapList` (\cur -> returnList (cur ++ (show amt))))

-- Example uses of unitsLeft
take3twenties = unitsLeft currencySupply 20 3
take1hundred = unitsLeft currencySupply 100 1

-- Example use of listNotes
machineNotes = listNotes (20 :| 50 :| 100 :| Nil) ("$AU" :| "$NZ" :| Nil)

-- Example use of sequenceOption; function giving a list of the units left for each currency value used  
checkComboServiceable :: List (Int,Int) -> List (Int,Int) -> Option (List Int)
checkComboServiceable cur combo = sequenceOption (foldRight (\(val,num) acc -> (unitsLeft cur val num) :| acc) Nil combo)  

-- Example uses of checkComboServiceable
supplyAfter70Combo = checkComboServiceable currencySupply ((20,1) :| (50,1) :| Nil) 
supplyAfter170Combo = checkComboServiceable currencySupply ((20,6) :| (50,1) :| Nil) 
supplyAfter170Combo2 = checkComboServiceable currencySupply ((20,1) :| (50,3) :| Nil)

-- Helper function to create all the value/unit tuples that could possibly appear in a valid combination to supply an amount
createValueUnitPairs :: Int -> Int -> List (Int,Int)
createValueUnitPairs amt val = toList $ zip (repeat val) [0..(amt `div` val)] 

-- Example use of sequenceList; calculating all the possible currency combinations to supply a given amount  
findCombos :: Int -> List (Int) -> List (List (Int,Int)) 
findCombos 0 _ = Nil 
findCombos amt vals = filterList valEqAmount combos
        where combos = sequenceList $ foldRight (\val acc -> (createValueUnitPairs amt val) :| acc) Nil vals 
              valEqAmount combo = amt == foldRight(\(val,num) acc -> val * num + acc) 0 combo 

-- Function showing the result of just the combinations calculation, before the results are filtered based on value
combinations :: Int -> List (List (Int,Int))
combinations amt = sequenceList $ foldRight (\val acc -> (createValueUnitPairs amt val) :| acc) Nil (20 :| 50 :| 100 :| Nil) 

-- Function showing how we could bring it all together, making use of flatMapOption, sequenceList and sequenceOption to find serviceable combinations  
findUsableCombos :: Int -> List (Int,Int) -> List (List (Int,Int)) 
findUsableCombos amt supply = filterList isUsable $ findCombos amt curs
        where isUsable combo = checkUsable (checkComboServiceable supply combo)
              checkUsable None = False
              checkUsable (Some _) = True 
              curs = toList $ [val | (val,num) <- (fromList supply)]

-- Example use of findUsableCombos
combosFor100 = findUsableCombos 100 currencySupply 
