-- Code based on exercises by Tony Morris (see http://github.com/tonymorris/course)

module Option where

-- Custom Maybe type
data Option a = Some a | None deriving (Eq, Show)

mapOption :: (a -> b) -> Option a -> Option b
mapOption _ None = None 
mapOption f (Some a) = Some (f a)

flatMapOption :: (a -> Option b) -> Option a -> Option b
flatMapOption _ None = None 
flatMapOption f (Some a) = f a

applyOption :: Option (a -> b) -> Option a -> Option b
applyOption opf opa = flatMapOption (`mapOption` opa) opf 

liftOption :: (a -> b -> c) -> Option a -> Option b -> Option c
liftOption f = applyOption . mapOption f

sequenceOption :: [Option a] -> Option [a]
sequenceOption = foldr (liftOption (:)) (Some [])

