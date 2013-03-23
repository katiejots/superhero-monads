-- Example showing some different FP features in Haskell

module Example where

myList = ['a'] 

myFunc :: (Num a, Enum a) => [b] -> Int -> [(a,b)]
myFunc (x:xs) n = foldr (\i acc -> (i,x):acc) [] (take n [1..])

myFuncResult = myFunc myList 3
