-- Code based on exercises by Tony Morris (see http://github.com/tonymorris/course)

module List where

-- Custom list type
data List t = Nil | t :| List t deriving Eq

-- Right-associative
infixr 5 :|

instance (Show t) => Show (List t) where
  show = show . foldRight (:) []

foldRight :: (a -> b -> b) -> b -> List a -> b
foldRight _ b Nil      = b
foldRight f b (h :| t) = f h (foldRight f b t)

maap :: (a -> b) -> List a -> List b
maap f = foldRight (\x acc -> f x :| acc) Nil 

fiilter :: (a -> Bool) -> List a -> List a
fiilter f = foldRight (\x -> if f x then (x :|) else id) Nil 

append :: List a -> List a -> List a
append = flip $ foldRight (:|) 

flatMap :: (a -> List b) -> List a -> List b
flatMap f = foldRight (append . f) Nil 
