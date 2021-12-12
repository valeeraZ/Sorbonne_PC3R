***Exercice 1.1***

Ecrire un programme qui lance trois processus fournisseurs qui mettent un temps aléatoire pour envoyer, chacun sur un canal différent, un *produit* différent (par exemple, des fruits) et un quatrième processus qui doit recevoir tous les produits. Les threads fournisseurs se relancent un nombre fini de fois.
```ocaml
let fournisseur arg =
    let (chaine, canal, n) = arg in
    let rec aux = function
          0 -> ()
        | x -> Event.sync (Even.send canal chaine) ;
               aux (x-1)
    in aux n

let rec recepteur arg = 
    let (c1, c2, c3) = arg in
    let chaine = Event.select [Event.receive c1;
                               Event.receive c2;
                               Event.receive c3] in
    print_string chaine^"\n" ;
    recepteur arg


let cp = Event.new_channel ()
and cb = Event.new_channel ()
and co = Event.new_channel ()
in let tp = Thread.create fournisseur ("pomme", cp, 3) 
in let tb = Thread.create fournisseur ("banane", cb, 3) 
in let to = Thread.create fournisseur ("orange", co, 3) 
in let rc = Thread.create recepteur (cb, co, cp)
in Thread.join tp; Thread.join tb; Thread.join to
```

***Deuxieme version***

```ocaml
let fournisseur arg =
    let (chaine, canal, n) = arg in
    let rec aux = function
          0 -> ()
        | x -> Event.sync (Even.send canal chaine) ;
               aux (x-1)
    in aux n

let rec recepteur li = 
    let chaine = Event.select (map Event.receive li) in
    print_string chaine^"\n" ;
    recepteur li


let cp = Event.new_channel ()
and cb = Event.new_channel ()
and co = Event.new_channel ()
in let tp = Thread.create fournisseur ("pomme", cp, 3) 
in let tb = Thread.create fournisseur ("banane", cb, 3) 
in let to = Thread.create fournisseur ("orange", co, 3) 
in let rc = Thread.create recepteur [cb; co; cp]
in Thread.join tp; Thread.join tb; Thread.join to
```

***Exercice 1.2.2***

```ocaml
let c_sell = Event.new_channel ()
let c_brok = Event.new_channel ()
let c_buy1 = Event.new_channel ()
let c_buy2 = Event.new_channel ()
let log1 = ref ""
let log2 = ref ""

let rec seller n =
    let (y, z) = Event.sync (Event.receive c_sell) in
    Event.sync (Event.send y (z^" "^(string_of_int n)));
    seller n + 1

let rec buyer arg =
    let (demande, n, c_buy, log, varlog) in
    if n == 0 then varlog := log else
        begin
            Event.sync (Event.send c_brok (demande, c_buy));
            let c_priv = Event.sync (Event.receive c_buy) in
            let produit = Event.sync (Event.receive c_priv) in
            buyer (demande, n-1, c_buy, 
                    log^"J'avais demandé "^demande^", j'ai    reçu "^produit^"\n",
                    varlog)
        end

let rec broker () =
    let (dem, c_buy) = Event.sync (Event.receive c_brok) in
    c_priv = new_channel () in 
    Event.sync (Event.send c_buy c_priv);
    Event.sync (Event.send c_sell (c_priv, dem));
    broker ()

let main =
    let _ = Thread.create seller 1
    and _ = Thread.create broker ()
    and buy1 = Thread.create ("the", 3, c_buy1, "", log1)
    and buy2 = Thread.create ("cafe", 2, c_buy2, "", log2)
    in 
    Thread.join buy1; Thread.join buy2;
    print_endline !log1;
    print_endline !log2
```

*** Exercice 1.2.3 ***
```ocaml

let enleve e = function
      [] -> []
    | t::q when t = e -> q
    | t::q -> t::(enleve e q)

let broadcast i l =
    let rec aux m li = 
        Event.select (map (fun c -> 
                      wrap (Event.send c m)
                      (fun () -> aux m (enleve c li)) 
                     ) li)
    in
    let m = Event.sync (Event.receive i) in
    aux m l
```

***Exercice 1.3***

si `o1` récupère le message en premier alors il envoie ce message à `o2` et `o3`.
```ocaml
(*l: liste de canaux, m: message*)
let rec brod l m = match l with
	| [] -> ()
	| l -> select [o1 wrap (send o1 m) (fun() -> brod [o2;o3] m);
								 o2 wrap (send o2 m) (fun() -> brod [o1;o3] m);
								 o3 wrap (send o3 m) (fun() -> brod [o1;o2] m);]
```

***Futures***

```ocaml

type 'a future
val spawn ; ('a -> 'b) -> 'a -> 'b future
val get : 'a future -> 'a
val isDone : 'a future -> bool

type 'a future = ('a channel, bool ref)

let spawn calcul arg = 
    let c = Event.new_channel () 
    and b = ref False
    and let routine () =
            let res = calcul arg in
            b := True;
            Event.sync (Event.send c res)
    in
    Thread.create routine ();
    (c, b)

let get fut =
    let (c, _) = fut in
    Event.sync (Event.receive c)

let isDone fut =
    let (_, b) = fut in
    b

let rec somme_ordre acc = function
      [] -> acc
    | futs -> Event.select (map 
              (fun fut -> let (c,_) = fut in wrap (Event.receive c)
                            (fun n -> somme_ordre (acc + n) (enleve fut futs))
              ) 
              futs)



val f : int −> int

let x1 = spawn f 5000
and x2 = spawn f 4000
and x3 = spawn f 6000
in
... 
let result = ((get x1) + (get x2) + (get x3)) / 3
//
let result = somme_ordre 0 [x1; x2; x3] 

```

***Futures en Go***
```go

type futureInt struct{
    fut chan int
    fini bool
}

func spawnIntInt(calcul func(int) int, arg int) futureInt {
    c := make(chan int)
    *d := false
    go func(){res := calcul arg,
              *d = true,
               c <- res} ()
    return futureInt{fut : c, fini: d}
}

func getInt(f futureInt){
    return <- f.fut
}

func isDone(f futureInt){
    return f.(*d)
}
```
