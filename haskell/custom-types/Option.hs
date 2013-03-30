-- Code based on exercises by Tony Morris (see http://github.com/tonymorris/course)

module Option where

import List

-- Custom Maybe type
data Option a = Some a | None deriving (Eq, Show)

mapOption :: (a -> b) -> Option a -> Option b
mapOption _ None = None 
mapOption f (Some a) = Some (f a)

flatMapOption :: Option a -> (a -> Option b) -> Option b
flatMapOption None _ = None 
flatMapOption (Some a) f = f a

returnOption :: a -> Option a
returnOption a = Some a

applyOption :: Option (a -> b) -> Option a -> Option b
applyOption opf opa = flatMapOption opf (`mapOption` opa) 

lift2Option :: (a -> b -> c) -> Option a -> Option b -> Option c
lift2Option f = applyOption . mapOption f

sequenceOption :: List (Option a) -> Option (List a)
sequenceOption = foldRight (lift2Option (:|)) (returnOption Nil)

