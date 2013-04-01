let myList = ['a']

let myFunc l n =
    match l with
    | (x::_) -> List.foldBack (fun i acc -> (i, x) :: acc) [1..n] []
    | [] -> []

let myFuncResult = myFunc myList 3
//val myList : char list = ['a']
//val myFunc : l:'a list -> n:int -> (int * 'a) list
//val myFuncResult : (int * char) list = [(1, 'a'); (2, 'a'); (3, 'a')]
