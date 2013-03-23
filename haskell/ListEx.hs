-- Example implementations of return, bind and sequence for the built-in list type

module ListEx where

-- picture a constructor, conceptually, something like: data [a] = [] | a:[a]

returnList :: a -> [a]
returnList a = [a]

bindList :: [a] -> (a -> [b]) -> [b]
bindList l f = foldr (\x acc -> f x ++ acc) [] l



returnListExample = returnList "Bar"

bindListExample = ["Super", "Spider", "Bar"] `bindList` (\x -> returnList (x ++ "man"))
