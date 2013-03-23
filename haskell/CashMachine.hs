-- CashMachine example using built-in List and Maybe types

module CashMachine where

-- Preconstructed list of currency supply, for convenience
currencySupply = [(20, 5),(50, 10),(100, 0)]

-- Function returning the number of units that would be left of a currency value if we took away a given number of units
unitsLeft :: [(Int,Int)] -> Int -> Int -> Maybe Int
unitsLeft supply val num = (lookup val supply) >>= (\u -> if num < 0 || u - num < 0 then Nothing else return (u - num))

-- Function to list all the notes our machine can supply, given its currency values and currencies
listNotes :: [Int] -> [String] -> [String]
listNotes amts curs = amts >>= (\amt -> curs >>= (\cur -> return (cur ++ (show amt))))

-- Example uses of unitsLeft
take3twenties = unitsLeft currencySupply 20 3
take1hundred = unitsLeft currencySupply 100 1

-- Function to get machine currencies
machineCurrencies :: [(Int,Int)] -> [Int]
machineCurrencies = foldr ((:) . fst) [] 

-- Example use of listNotes
machineNotes = listNotes (machineCurrencies currencySupply) ["$AU","$NZ"]

-- Example use of sequence with Maybe; function giving a list of the units left for each currency value used  
checkComboServiceable :: [(Int,Int)] -> [(Int,Int)] -> Maybe [Int]
checkComboServiceable cur combo = sequence (foldr (\(val,num) acc -> (unitsLeft cur val num) : acc) [] combo)  

-- Example uses of checkComboServiceable
supplyAfter70Combo = checkComboServiceable currencySupply [(20,1),(50,1)]
supplyAfter170Combo = checkComboServiceable currencySupply [(20,6),(50,1)] 
supplyAfter170Combo2 = checkComboServiceable currencySupply [(20,1),(50,3)]

-- Helper function to create all the value/unit tuples that could possibly appear in a valid combination to supply an amount
createValueUnitPairs :: Int -> Int -> [(Int,Int)]
createValueUnitPairs amt val = zip (repeat val) [0..(amt `div` val)] 

-- Example use of sequence with list; calculating all the possible currency combinations to supply a given amount  
findCombos :: Int -> [Int] -> [[(Int,Int)]]
findCombos 0 _ = [] 
findCombos amt vals = filter valEqAmount combos
        where combos = sequence $ foldr (\val acc -> (createValueUnitPairs amt val) : acc) [] vals 
              valEqAmount combo = amt == foldr(\(val,num) acc -> val * num + acc) 0 combo 

-- Function showing the result of just the combinations calculation, before the results are filtered based on value
combinations amt = sequence $ foldr (\val acc -> (createValueUnitPairs amt val) : acc) [] (machineCurrencies currencySupply) 

-- Function showing how we could bring it all together, making use of bind (>>=) and sequence with list and Maybe to find serviceable combinations  
findUsableCombos :: Int -> [(Int,Int)] -> [[(Int,Int)]]
findUsableCombos amt supply = filter isUsable $ findCombos amt curs
        where isUsable combo = checkUsable (checkComboServiceable supply combo)
              checkUsable Nothing = False
              checkUsable (Just _) = True 
              curs = [val | (val,num) <- supply]

-- Example use of findUsableCombos
combosFor100 = findUsableCombos 100 currencySupply 
