module OptionInferred =

    let returnOption a =
        Some a
    let bindOption ma f =
        match ma with
        | None -> None
        | Some(a) -> f a
    let (>>=) ma f =
        bindOption ma f
    let mapOption f ma =
        match ma with
        | None -> None
        | Some(a) -> Some (f a)
    let liftOption f ma =
        bindOption ma (returnOption << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb)
    let liftOption2 f ma mb =
        bindOption (mapOption f ma) (fun x -> mapOption x mb)
    let sequenceOption ls =
        List.foldBack (fun l acc -> liftOption2 (fun a b -> a :: b) l acc) ls (returnOption [])
//module OptionInferred = begin
//  val returnOption : a:'a -> 'a option
//  val bindOption : ma:'a option -> f:('a -> 'b option) -> 'b option
//  val ( >>= ) : ma:'a option -> f:('a -> 'b option) -> 'b option
//  val mapOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption2 :
//    f:('a -> 'b -> 'c) -> ma:'a option -> mb:'b option -> 'c option
//  val sequenceOption : ls:'a option list -> 'a list option
//end