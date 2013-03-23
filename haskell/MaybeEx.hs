-- Example implementations of bind, return and sequence for built-in Maybe type

module MaybeEx where

-- What Maybe type constructor looks like
-- data Maybe a = Just a | Nothing

returnMaybe :: a -> Maybe a
returnMaybe a = Just a

bindMaybe :: Maybe a -> (a -> Maybe b) -> Maybe b
Nothing `bindMaybe` _ = Nothing
Just a `bindMaybe` f = f a



returnExample = returnMaybe 7

bindExample1 = Just 7 `bindMaybe` (\x -> returnMaybe (x+1))
bindExample2 = Nothing `bindMaybe` (\x -> returnMaybe (x+1))
bindExample3 = Just 7 `bindMaybe` (\x -> returnMaybe (x+1)) `bindMaybe` (\x -> returnMaybe (x*5))

