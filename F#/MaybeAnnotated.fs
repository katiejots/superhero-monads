module MaybeAnnotated =

    type Maybe<'a> = | Just of 'a | Nothing

    let returnMaybe (a : 'a) : 'a Maybe =
        Just a
    let bindMaybe (ma : 'a Maybe) (f : 'a -> 'b Maybe) : 'b Maybe =
        match ma with
        | Nothing -> Nothing
        | Just(a) -> f a
    let (>>=) ma f =
        bindMaybe ma f
    let mapMaybe (f : 'a -> 'b) (ma : 'a Maybe) : 'b Maybe =
        match ma with
        | Nothing -> Nothing
        | Just(a) -> Just (f a)
    let liftMaybe (f : 'a -> 'b) (ma : 'a Maybe) =
        bindMaybe ma (returnMaybe << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb).
    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
        bindMaybe (mapMaybe f ma) (fun x -> mapMaybe x mb)
    let sequenceMaybe (ls : 'a Maybe list) : 'a list Maybe =
        List.foldBack (fun l acc -> liftMaybe2 (fun a b -> a :: b) l acc) ls (returnMaybe [])
//module MaybeAnnotated = begin
//  type Maybe<'a> =
//    | Just of 'a
//    | Nothing
//  val returnMaybe : a:'a -> Maybe<'a>
//  val bindMaybe : ma:Maybe<'a> -> f:('a -> Maybe<'b>) -> Maybe<'b>
//  val ( >>= ) : ma:Maybe<'a> -> f:('a -> Maybe<'b>) -> Maybe<'b>
//  val mapMaybe : f:('a -> 'b) -> ma:Maybe<'a> -> Maybe<'b>
//  val liftMaybe : f:('a -> 'b) -> ma:Maybe<'a> -> Maybe<'b>
//  val liftMaybe2 :
//    f:('a -> 'b -> 'c) -> ma:Maybe<'a> -> mb:Maybe<'b> -> Maybe<'c>
//  val sequenceMaybe : ls:Maybe<'a> list -> Maybe<'a list>
//end

// The following are other ways of implementing some of these functions.
//    let liftMaybe (f : 'a -> 'b) (ma : 'a Maybe) =
//        ma >>= (returnMaybe << f)
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        match ma, mb with
//        | Nothing, _ -> Nothing
//        | _, Nothing -> Nothing
//        | Just a, Just b -> Just (f a b)
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        match ma with
//        | Nothing -> Nothing
//        | Just a ->
//            match mb with
//            | Nothing -> Nothing
//            | Just b -> Just (f a b)
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        match mapMaybe f ma with
//        | Nothing -> Nothing
//        | Just g ->
//            match mb with
//            | Nothing -> Nothing
//            | Just _ -> mapMaybe g mb
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        ma >>= fun x1 -> mb >>= fun x2 -> returnMaybe (f x1 x2)
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        bindMaybe ma (fun x1 -> bindMaybe mb (fun x2 -> returnMaybe (f x1 x2)))
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        let x = mapMaybe f ma
//        let y g = mapMaybe g mb
//        x >>= y
//    let liftMaybe2 (f : 'a -> 'b ->'c) (ma : 'a Maybe) (mb : 'b Maybe) =
//        mapMaybe f ma >>= fun x -> mapMaybe x mb