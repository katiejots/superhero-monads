[<AutoOpen>]
module private __ =
    let returnOption a =
        Some a
    let bindOption ma f =
        match ma with
        | None -> None
        | Some(a) -> f a
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
    let returnList a =
        [a]
    let bindList l f =
        List.foldBack (fun x acc -> f x @ acc) l []
    let mapList f l =
        List.foldBack (fun x acc -> (f x)::acc) l []
    let liftList f la =
        bindList la (returnList << f)
    // F# doesn't have sections, hence the lambda instead of (`mapList` lb).
    let liftList2 f la lb =
        bindList (mapList f la) (fun x -> mapList x lb)
    let sequenceList ls =
        List.foldBack (fun l acc -> liftList2 (fun a b -> a :: b) l acc) ls (returnList [])
    let lookup v xys =
        match xys |> List.tryFind (fun (a, _) -> a = v) with
        | Some(_, b) -> Some(b)
        | _ -> None

module CashMachine =

    // Preconstructed list of currency supply, for convenience
    let currencySupply = [(20, 5); (50, 10); (100, 0)]

    // Function returning the number of units that would be left of a currency value if we took away a given number of units
    //unitsLeft :: [(Int,Int)] -> Int -> Int -> Maybe Int
    let unitsLeft (supply : (int * int) list) value num =
        bindOption (lookup value supply) (fun u -> if num < 0 || u - num < 0 then None else returnOption (u - num))

    // Function to list all the notes our machine can supply, given its currency values and currencies
    //listNotes :: [Int] -> [String] -> [String]
    let listNotes (amts : int list) (curs : string list) =
        bindList amts (fun amt -> bindList curs (fun cur -> returnList (cur + (sprintf "%A" amt))))

    // Example uses of unitsLeft
    let take3twenties = unitsLeft currencySupply 20 3
    let take10twenties = unitsLeft currencySupply 20 10
    let take1seventy = unitsLeft currencySupply 70 1

    // Function to get machine currencies
    //machineCurrencies :: [(Int,Int)] -> [Int]
    let machineCurrencies (l : (int * int) list) =
        List.foldBack ((fun a b -> a :: b) << fst) l []

    // Example use of listNotes
    let machineNotes = listNotes (machineCurrencies currencySupply) ["$AU"; "$NZ"]

    // Example use of sequence with Maybe; function giving a list of the units left for each currency value used  
    //checkComboServiceable :: [(Int,Int)] -> [(Int,Int)] -> Maybe [Int]
    let checkComboServiceable (cur : (int * int) list) (combo : (int * int) list) =
        [] 
        |> List.foldBack (fun (value, num) acc -> (unitsLeft cur value num) :: acc) combo 
        |> sequenceOption

    // Example uses of checkComboServiceable
    let supplyAfter70Combo = checkComboServiceable currencySupply [(20,1); (50,1)]
    let supplyAfter170Combo = checkComboServiceable currencySupply [(20,6); (50,1)] 
    let supplyAfter170Combo2 = checkComboServiceable currencySupply [(20,1); (50,3)]

    // Helper function to create all the value/unit tuples that could possibly appear in a valid combination to supply an amount
    //createValueUnitPairs :: Int -> Int -> [(Int,Int)]
    let createValueUnitPairs amt value =
        let n = amt / value
        [for x in 0..n do yield (value, x)]

    // Example use of sequence with list; calculating all the possible currency combinations to supply a given amount  
    //findCombos :: Int -> [Int] -> [[(Int,Int)]]
    let findCombos n l =
        match n, l with
        | 0, l -> [] 
        | amt, vals ->
            let valEqAmount combo =
                amt = List.foldBack (fun (value, num) acc -> value * num + acc) combo 0
            let combos =
                sequenceList (List.foldBack (fun value acc -> (createValueUnitPairs amt value) :: acc) vals [])
            List.filter valEqAmount combos

    // Function showing the result of just the combinations calculation, before the results are filtered based on value
    let combinations amt =
        sequenceList (List.foldBack (fun value acc -> (createValueUnitPairs amt value) :: acc) (machineCurrencies currencySupply) [])
    let combinations70 = combinations 70

    // Function showing how we could bring it all together, making use of bind (>>=) and sequence with list and Maybe to find serviceable combinations  
    //findUsableCombos :: Int -> [(Int,Int)] -> [[(Int,Int)]]
    let findUsableCombos (amt : int) (supply : (int * int) list) =
        let checkUsable = function
            | None -> false
            | Some _ -> true
        let curs = [for value, num in supply do yield value]
        let isUsable combo =
            checkUsable (checkComboServiceable supply combo)
        List.filter isUsable (findCombos amt curs)

    // Example use of findUsableCombos
    let combosFor100 = findUsableCombos 100 currencySupply 
//module private __ = begin
//  val returnOption : a:'a -> 'a option
//  val bindOption : ma:'a option -> f:('a -> 'b option) -> 'b option
//  val mapOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption : f:('a -> 'b) -> ma:'a option -> 'b option
//  val liftOption2 :
//    f:('a -> 'b -> 'c) -> ma:'a option -> mb:'b option -> 'c option
//  val sequenceOption : ls:'a option list -> 'a list option
//  val returnList : a:'a -> 'a list
//  val bindList : l:'a list -> f:('a -> 'b list) -> 'b list
//  val mapList : f:('a -> 'b) -> l:'a list -> 'b list
//  val liftList : f:('a -> 'b) -> la:'a list -> 'b list
//  val liftList2 : f:('a -> 'b -> 'c) -> la:'a list -> lb:'b list -> 'c list
//  val sequenceList : ls:'a list list -> 'a list list
//  val lookup : v:'a -> xys:('a * 'b) list -> 'b option when 'a : equality
//end
//module CashMachine = begin
//  val currencySupply : (int * int) list = [(20, 5); (50, 10); (100, 0)]
//  val unitsLeft :
//    supply:(int * int) list -> value:int -> num:int -> int option
//  val listNotes : amts:int list -> curs:string list -> string list
//  val take3twenties : int option = Some 2
//  val take10twenties : int option = None
//  val take1seventy : int option = None
//  val machineCurrencies : l:(int * int) list -> int list
//  val machineNotes : string list =
//    ["$AU20"; "$NZ20"; "$AU50"; "$NZ50"; "$AU100"; "$NZ100"]
//  val checkComboServiceable :
//    cur:(int * int) list -> combo:(int * int) list -> int list option
//  val supplyAfter70Combo : int list option = Some [4; 9]
//  val supplyAfter170Combo : int list option = None
//  val supplyAfter170Combo2 : int list option = Some [4; 7]
//  val createValueUnitPairs : amt:int -> value:int -> (int * int) list
//  val findCombos : n:int -> l:int list -> (int * int) list list
//  val combinations : amt:int -> (int * int) list list
//  val combinations70 : (int * int) list list =
//    [[(20, 0); (50, 0); (100, 0)]; [(20, 0); (50, 1); (100, 0)];
//     [(20, 1); (50, 0); (100, 0)]; [(20, 1); (50, 1); (100, 0)];
//     [(20, 2); (50, 0); (100, 0)]; [(20, 2); (50, 1); (100, 0)];
//     [(20, 3); (50, 0); (100, 0)]; [(20, 3); (50, 1); (100, 0)]]
//  val findUsableCombos :
//    amt:int -> supply:(int * int) list -> (int * int) list list
//  val combosFor100 : (int * int) list list =
//    [[(20, 0); (50, 2); (100, 0)]; [(20, 5); (50, 0); (100, 0)]]
//end