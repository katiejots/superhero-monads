-- Code based on exercises by Tony Morris (see http://github.com/tonymorris/course)

module Monadd where

import Option
import List

class Monadd m where
  bind :: (a -> m b) -> m a -> m b
  reeturn :: a -> m a
  fmaap :: (a -> b) -> m a -> m b
  fmaap f = bind (reeturn . f) 

instance Monadd List where
  bind = flatMap  
  reeturn x = (x :| Nil) 

instance Monadd Option where
  bind _ None = None 
  bind f (Some a) = f a
  reeturn = Some 

apply :: Monadd m => m (a -> b) -> m a -> m b
apply mf ma = bind (`fmaap` ma) mf

lift2 :: Monadd m => (a -> b -> c) -> m a -> m b -> m c
lift2 f = apply . fmaap f   

seequence :: Monadd m => [m a] -> m [a]
seequence = foldr (lift2 (:)) (reeturn []) 
