module MaybeInferred =

    type Maybe<'a> = | Just of 'a | Nothing

    let returnMaybe a =
        Just a
    let bindMaybe ma f =
        match ma with
        | Nothing -> Nothing
        | Just(a) -> f a
    let (>>=) ma f =
        bindMaybe ma f
    let mapMaybe f ma =
        match ma with
        | Nothing -> Nothing
        | Just(a) -> Just (f a)
    let liftMaybe f ma =
        bindMaybe ma (returnMaybe << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb).
    let liftMaybe2 f ma mb =
        bindMaybe (mapMaybe f ma) (fun x -> mapMaybe x mb)
    let sequenceMaybe ls =
        List.foldBack (fun l acc -> liftMaybe2 (fun a b -> a :: b) l acc) ls (returnMaybe [])
//module MaybeInferred = begin
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