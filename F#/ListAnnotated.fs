module ListAnnotated =

    let returnList (a : 'a) =
        [a]
    let bindList (l : 'a list) (f : 'a -> 'b list) : 'b list =
        List.foldBack (fun x acc -> f x @ acc) l []
    let (>>=) la f =
        bindList la f
    let mapList (f : 'a -> 'b) (l : 'a list) : 'b list =
        List.foldBack (fun x acc -> (f x)::acc) l []
    let liftList (f : 'a -> 'b) (la : 'a list) : 'b list =
        bindList la (returnList << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb).
    let liftList2 (f : 'a -> 'b -> 'c) (la : 'a list) (lb : 'b list) : 'c list =
        bindList (mapList f la) (fun x -> mapList x lb)
    let sequenceList (ls : 'a list list) : 'a list list =
        List.foldBack (fun l acc -> liftList2 (fun a b -> a :: b) l acc) ls (returnList [])
//module ListAnnotated = begin
//  val returnList : a:'a -> 'a list
//  val bindList : l:'a list -> f:('a -> 'b list) -> 'b list
//  val ( >>= ) : la:'a list -> f:('a -> 'b list) -> 'b list
//  val mapList : f:('a -> 'b) -> l:'a list -> 'b list
//  val liftList : f:('a -> 'b) -> la:'a list -> 'b list
//  val liftList2 : f:('a -> 'b -> 'c) -> la:'a list -> lb:'b list -> 'c list
//  val sequenceList : ls:'a list list -> 'a list list
//end

// The following are other ways of implementing some of these functions.
//    let liftList (f : 'a -> 'b) (la : 'a list) : 'b list =
//        la >>= (returnList << f)
//    let liftList2 (f : 'a -> 'b -> 'c) (la : 'a list) (lb : 'b list) : 'c list =
//        la >>= fun x1 -> lb >>= fun x2 -> returnList (f x1 x2)
//    let liftList2 (f : 'a -> 'b -> 'c) (la : 'a list) (lb : 'b list) : 'c list =
//        bindList la (fun x1 -> bindList lb (fun x2 -> returnList (f x1 x2)))
//    let liftList2 (f : 'a -> 'b -> 'c) (la : 'a list) (lb : 'b list) : 'c list =
//        let x = mapList f la
//        let y g = mapList g lb
//        x >>= y
//    let liftList2 (f : 'a -> 'b -> 'c) (la : 'a list) (lb : 'b list) : 'c list =
//        mapList f la >>= fun x -> mapList x lb