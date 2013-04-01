module OptionAnnotated =

    let returnOption (a : 'a) : 'a option =
        Some a
    let bindOption (ma : 'a option) (f : 'a -> 'b option) : 'b option =
        match ma with
        | None -> None
        | Some(a) -> f a
    let (>>=) ma f =
        bindOption ma f
    let mapOption (f : 'a -> 'b) (ma : 'a option) : 'b option =
        match ma with
        | None -> None
        | Some(a) -> Some (f a)
    let liftOption (f : 'a -> 'b) (ma : 'a option) =
        bindOption ma (returnOption << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb)
    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
        bindOption (mapOption f ma) (fun x -> mapOption x mb)
    let sequenceOption (ls : 'a option list) : 'a list option =
        List.foldBack (fun l acc -> liftOption2 (fun a b -> a :: b) l acc) ls (returnOption [])
//module OptionAnnotated = begin
//  val returnOption : a:'a -> 'a option
//  val bindOption : ma:'a option -> f:('a -> 'b option) -> 'b option
//  val ( >>= ) : ma:'a option -> f:('a -> 'b option) -> 'b option
//  val mapOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption2 :
//    f:('a -> 'b -> 'c) -> ma:'a option -> mb:'b option -> 'c option
//  val sequenceOption : ls:'a option list -> 'a list option
//end

// The following are other ways of implementing some of these functions.
//    let liftOption (f : 'a -> 'b) (ma : 'a option) =
//        ma >>= (returnOption << f)
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        match ma, mb with
//        | None, _ -> None
//        | _, None -> None
//        | Some a, Some b -> Some (f a b)
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        match ma with
//        | None -> None
//        | Some a ->
//            match mb with
//            | None -> None
//            | Some b -> Some (f a b)
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        match mapOption f ma with
//        | None -> None
//        | Some g ->
//            match mb with
//            | None -> None
//            | Some _ -> mapOption g mb
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        ma >>= fun x1 -> mb >>= fun x2 -> returnOption (f x1 x2)
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        bindOption ma (fun x1 -> bindOption mb (fun x2 -> returnMaybe (f x1 x2)))
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        let x = mapOption f ma
//        let y g = mapOption g mb
//        x >>= y
//    let liftOption2 (f : 'a -> 'b ->'c) (ma : 'a option) (mb : 'b option) =
//        mapOption f ma >>= fun x -> mapOption x mb