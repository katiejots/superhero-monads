-- Code based on exercises by Tony Morris (see http://github.com/tonymorris/course)

module Option where

-- Custom Maybe type
data Option a = Some a | None deriving (Eq, Show)

mapOption :: (a -> b) -> Option a -> Option b
mapOption _ None = None 
mapOption f (Some a) = Some (f a)

flatMapOption :: Option a -> (a -> Option b) -> Option b
flatMapOption None _ = None 
flatMapOption (Some a) f = f a

applyOption :: Option (a -> b) -> Option a -> Option b
applyOption opf opa = flatMapOption opf (`mapOption` opa) 

liftOption :: (a -> b -> c) -> Option a -> Option b -> Option c
liftOption f = applyOption . mapOption f

sequenceOption :: [Option a] -> Option [a]
sequenceOption = foldr (liftOption (:)) (Some [])

