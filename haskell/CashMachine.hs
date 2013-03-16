module CashMachine where

import Option
import List
import Monadd

currency = (20.0, 5) :| (50.0, 10) :| (100.0, 0) :| Nil

listLookup :: Eq a => a -> List (a, b) -> Option b
listLookup key Nil = None
listLookup key ((x,y) :| xs)
    | key == x = Some y
    | otherwise = listLookup key xs

unitsLeft :: List (Float,Integer) -> Float -> Integer -> Option Integer
unitsLeft xs val num = (listLookup val xs) `flatMapOption` (\x -> if num < 0 || x - num < 0 then None else Some (x - num))

listNotes :: List Float -> List String -> List String
listNotes amounts currencies = amounts `flatMapList` (\amount -> currencies `flatMapList` (\currency -> reeturn (currency ++ (show amount))))

take3twenties = unitsLeft currency 20 3
take1hundred = unitsLeft currency 100 1

machineNotes = listNotes (20 :| 50 :| 100 :| Nil) ("$AU" :| "$NZ" :| Nil)

checkAmountServiceable :: List (Float,Integer) -> List (Float,Integer) -> Option (List Integer)
checkAmountServiceable currency combination = sequenceOption (foldRight (\(val,num) acc -> (unitsLeft currency val num) :| acc) Nil combination)  

supplyAfter70Combo = checkAmountServiceable currency ((20.0,1) :| (50.0,1) :| Nil) 
supplyAfter170Combo = checkAmountServiceable currency ((20.0,6) :| (50.0,1) :| Nil) 
supplyAfter170Combo2 = checkAmountServiceable currency ((20.0,1) :| (50.0,3) :| Nil)

createValueUnitPairsForCurrencyValue :: Float -> Float -> List (Float,Integer)
createValueUnitPairsForCurrencyValue amount currencyValue = toList $ zip (repeat currencyValue) [0..floor(amount/currencyValue)] 

findCombinationsForAmount :: Float -> List (Float) -> List (List (Float,Integer)) 
findCombinationsForAmount _ Nil = Nil 
findCombinationsForAmount 0.0 _ = Nil 
findCombinationsForAmount amount currencyValues = filterList valueEqualsAmount combinations
        where combinations = sequenceList $ foldRight (\val acc -> (createValueUnitPairsForCurrencyValue amount val) :| acc) Nil currencyValues 
              valueEqualsAmount xs = amount == foldRight(\(val,num) acc -> val * (fromInteger num) + acc) 0.0 xs

combinations amount = sequenceList $ foldRight (\val acc -> (createValueUnitPairsForCurrencyValue amount val) :| acc) Nil (20.0 :| 50.0 :| 100.0 :| Nil) 

findServiceableCombinations :: Float -> List (Float,Integer) -> List (List (Float,Integer)) 
findServiceableCombinations amount machineSupply = filterList isServiceable $ findCombinationsForAmount amount machineCurrencies
        where isServiceable combo = checkServiceability (checkAmountServiceable machineSupply combo)
              checkServiceability None = False
              checkServiceability (Some _) = True 
              machineCurrencies = toList $ [value | (value,units) <- (fromList machineSupply)]
 
