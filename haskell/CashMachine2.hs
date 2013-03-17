-- Second version of CashMachine using built-in types

module CashMachine2 where

-- Preconstructed list of currency supply, for convenience
currencySupply = [(20.0, 5),(50.0, 10),(100.0, 0)]

-- Function returning the number of units that would be left of a currency value if we took away a given number of units
unitsLeft :: [(Float,Integer)] -> Float -> Integer -> Maybe Integer
unitsLeft supply val num = (lookup val supply) >>= (\u -> if num < 0 || u - num < 0 then Nothing else return (u - num))

-- Function to list all the notes our machine can supply, given its currency values and currencies
listNotes :: [Float] -> [String] -> [String]
listNotes amts curs = amts >>= (\amt -> curs >>= (\cur -> return (cur ++ (show amt))))

-- Example uses of unitsLeft
take3twenties = unitsLeft currencySupply 20 3
take1hundred = unitsLeft currencySupply 100 1

-- Function to get machine currencies
machineCurrencies :: [(Float,Integer)] -> [Float]
machineCurrencies = foldr ((:) . fst) [] 

-- Example use of listNotes
machineNotes = listNotes (machineCurrencies currencySupply) ["$AU","$NZ"]

-- Example use of sequenceOption; function giving a list of the units left for each currency value used  
checkComboServiceable :: [(Float,Integer)] -> [(Float,Integer)] -> Maybe [Integer]
checkComboServiceable cur combo = sequence (foldr (\(val,num) acc -> (unitsLeft cur val num) : acc) [] combo)  

-- Example uses of checkComboServiceable
supplyAfter70Combo = checkComboServiceable currencySupply [(20.0,1),(50.0,1)]
supplyAfter170Combo = checkComboServiceable currencySupply [(20.0,6),(50.0,1)] 
supplyAfter170Combo2 = checkComboServiceable currencySupply [(20.0,1),(50.0,3)]

-- Helper function to create all the value/unit tuples that could possibly appear in a valid combination to supply an amount
createValueUnitPairs :: Float -> Float -> [(Float,Integer)]
createValueUnitPairs amt val = zip (repeat val) [0..floor(amt/val)] 

-- Example use of sequenceList; calculating all the possible currency combinations to supply a given amount  
findCombos :: Float -> [Float] -> [[(Float,Integer)]]
findCombos 0.0 _ = [] 
findCombos amt vals = filter valEqAmount combos
        where combos = sequence $ foldr (\val acc -> (createValueUnitPairs amt val) : acc) [] vals 
              valEqAmount combo = amt == foldr(\(val,num) acc -> val * (fromInteger num) + acc) 0.0 combo 

-- Function showing the result of just the combinations calculation, before the results are filtered based on value
combinations amt = sequence $ foldr (\val acc -> (createValueUnitPairs amt val) : acc) [] (machineCurrencies currencySupply) 

-- Function showing how we could bring it all together, making use of flatMapOption, sequenceList and sequenceOption to find serviceable combinations  
findUsableCombos :: Float -> [(Float,Integer)] -> [[(Float,Integer)]]
findUsableCombos amt supply = filter isUsable $ findCombos amt curs
        where isUsable combo = checkUsable (checkComboServiceable supply combo)
              checkUsable Nothing = False
              checkUsable (Just _) = True 
              curs = [val | (val,num) <- supply]

-- Example use of findUsableCombos
combosFor100 = findUsableCombos 100.0 currencySupply 
