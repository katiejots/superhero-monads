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

mapList :: (a -> b) -> List a -> List b
mapList f = foldRight (\x acc -> f x :| acc) Nil 

filterList :: (a -> Bool) -> List a -> List a
filterList f = foldRight (\x -> if f x then (x :|) else id) Nil 

appendList :: List a -> List a -> List a
appendList = flip $ foldRight (:|) 

flatMapList :: (a -> List b) -> List a -> List b
flatMapList f = foldRight (appendList . f) Nil 

applyList :: List (a -> b) -> List a -> List b
applyList lf la = flatMapList (`mapList` la) lf

liftList :: (a -> b -> c) -> List a -> List b -> List c
liftList f = applyList . mapList f

sequenceList :: [List a] -> List [a]
sequenceList = foldr (liftList (:)) ([] :| Nil)

