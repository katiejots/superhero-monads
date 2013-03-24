-- Example implementations of return, bind and sequence for the built-in list type

module ListEx where

-- picture a constructor, conceptually, something like: data [a] = [] | a:[a]

returnList :: a -> [a]
returnList a = [a]

bindList :: [a] -> (a -> [b]) -> [b]
bindList l f = foldr (\x acc -> f x ++ acc) [] l

mapList :: (a -> b) -> [a] -> [b]
mapList f l = foldr (\x acc -> (f x):acc) [] l

liftList :: (a -> b -> c) -> [a] -> [b] -> [c]
liftList f la lb = (mapList f la) `bindList` (`mapList` lb)

sequenceList :: [[a]] -> [[a]]
sequenceList ls = foldr (\l acc -> liftList (:) l acc) (returnList []) ls

returnListExample = returnList "Bar"

bindListExample = ["Super", "Spider", "Bar"] `bindList` (\x -> returnList (x ++ "man"))
