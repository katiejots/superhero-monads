module CashMachine where

import Option
import List
import Monadd

currency = (20, 5) :| (50, 10) :| (100, 0) :| Nil

listLookup :: Eq a => a -> List (a, b) -> Option b
listLookup key Nil = None
listLookup key ((x,y) :| xs)
    | key == x = Some y
    | otherwise = listLookup key xs

unitsLeft :: List (Int,Int) -> Int -> Int -> Option Int
unitsLeft xs val num = (listLookup val xs) `flatMapOption` (\x -> if num < 0 || x - num < 0 then None else Some (x - num))

listNotes :: List Int -> List String -> List String
listNotes amounts currencies = amounts `flatMapList` (\amount -> currencies `flatMapList` (\currency -> reeturn (currency ++ (show amount))))

take3twenties = unitsLeft currency 20 3
take1hundred = unitsLeft currency 100 1

machineNotes = listNotes (20 :| 50 :| 100 :| Nil) ("$AUD" :| "$NZ" :| Nil)
