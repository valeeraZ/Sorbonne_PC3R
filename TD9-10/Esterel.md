**Exercice 1**
```
module ZUDBNB :

input ZERO, UN, DEUX;
output NB : integer, RESET;

var compt := 0 : integer in
    loop
    	emit RESET;
        compt := 0;
        loop
            await [UN or DEUX];
            present UN then 
                compt := compt + 1
            end present;
            present DEUX then
                compt := compt + 2
            end present;
            if compt >= 3 and (compt mod 3) = 0 then
                emit NB(compt)
            end if;
        end loop
    each ZERO
end var
end module

"; UN DEUX ; UN ; ZERO ; UN DEUX ; ;"
```
**Exercice 1 Variante (Sémantique différente)**
```
var compt := 0 : integer in
    loop
    	emit RESET;
        compt := 0;
        loop
            await immediate [UN or DEUX];
            present UN then 
                compt := compt + 1
            end present;
            present DEUX then
                compt := compt + 2
            end present;
            if compt >= 3 and (compt mod 3) = 0 then
                emit NB(compt)
            end if;
            pause;
        end loop
    each ZERO
end var
end module
```

**Exercice 3 : Consommateur** 
```
module Consommateur:

input FIN;
input Ci, CS : integer;
output C : integer;
output FCi : integer;
constant numero : integer;

var nbconso := 0 in
    abort
    loop
        await Ci;
        emit C(numero);
        await immediate CS;
        if ?CS = numero then
            nbconso := nbconso + 1
        end if
    end loop
    when FIN;
    emit FCi(nbconso);
end var
end module
```
**Exercice 3 : Producteur**
```
module Producteur :

input FIN;
output P, FP : integer;
output PS;

var nbprod := 0 : integer in
    abort
    loop
        await 3 tick;
        nbprod := nbprod + 1;
        emit P;
        await immediate PS:
    end loop
    when FIN;
    emit FP(nbprod);
end var
end module
```
**Exercice 3 : Gérant**
```
module Gerant 

input FIN;
constant max : integer;
input C : integer;
output CS : integer:
input P;
output PS;
output FS : integer;

output MAX, VIDE;
output ST : integer

var stock := 0 : integer in
abort
var attend := false : boolean in
    signal MAXL, VIDEL, STL : combine integer with + in
        loop
            if stock = max 
                then emit MAXL
            else if stock = 0 then emit VIDEL end if 
            end if;
        [
            present C then
                present VIDEL else emit STL(-1) emit CS(?C) end PRESENT
            end present
            ||
            present P then
                present MAXL then 
                     attend := true;
                else emit STL(1);
                     emit PS;
                     present VIDEL then emit CS(0) end present
            else
                present MAXL 
                else if attend then
                        emit STL(1):
                        attend := false;
                        emit PS;
                    end if
                end present
            end present
        ]
        present STL then stock != stock + ?STL end present
        if stock = max then emit MAX
        else if stock = 0 then emit VIDE end if end if
        pause;
        end loop
    end signal
end var
when FIN
emit FS(stock)
end var


end module
```
**Exercice 3 : Main**
```
module main : 

constant max = 5;
input FIN, C1, C2;
relation C1 # C2;

output P;
output CS: integer;
output PS;
output MAX, VIDE;
output FP : integer, FS : integer, FC1 : integer, FC2 : integer;
output ST : integer;

signal CL :integer
    run Gerant [constant max/max, signal CL/C]
    ||
    run Producteur
    ||
    run Consommateur [constant 1/numero; signal CL/C, C1/Ci, FC1/FCi]
    ||
    run Consommateur [constant 2/numero; signal CL/C, C2/Ci, FC2/FCi]
end signal
```