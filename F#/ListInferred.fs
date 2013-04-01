module ListInferred =

    let returnList a =
        [a]
    let bindList l f =
        List.foldBack (fun x acc -> f x @ acc) l []
    let (>>=) la f =
        bindList la f
    let mapList f l =
        List.foldBack (fun x acc -> (f x)::acc) l []
    let liftList f la =
        bindList la (returnList << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb).
    let liftList2 f la lb =
        bindList (mapList f la) (fun x -> mapList x lb)
    let sequenceList ls =
        List.foldBack (fun l acc -> liftList2 (fun a b -> a :: b) l acc) ls (returnList [])
//module ListInferred = begin
//  val returnList : a:'a -> 'a list
//  val bindList : l:'a list -> f:('a -> 'b list) -> 'b list
//  val ( >>= ) : la:'a list -> f:('a -> 'b list) -> 'b list
//  val mapList : f:('a -> 'b) -> l:'a list -> 'b list
//  val liftList : f:('a -> 'b) -> la:'a list -> 'b list
//  val liftList2 : f:('a -> 'b -> 'c) -> la:'a list -> lb:'b list -> 'c list
//  val sequenceList : ls:'a list list -> 'a list list
//end
