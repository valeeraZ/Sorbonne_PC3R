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
**Exercice 2**

```
%% Feu Nord-Sud

module FNS :

input ACNS;
output ACEO, RNS, VNS, ONS;

loop
    abort
        sustain RNS
    when ACNS

    emit RNS;
    repeat 4 times
        emit VNS; pause;
    end repeat
    emit ONS; pause;
    emit ACEO

end loop

end module;
```
```
%% Feu Est-Ouest

module FEO :

input ACEO;
output ACNS, REO, VEO, OEO;

loop
    abort
        sustain REO
    when ACEO

    emit REO;
    repeat 4 times
        emit VEO; pause;
    end repeat
    emit OEO; pause;
    emit ACNS

end loop

end module;
```
```
%% Main

module feux :

output RNS, REO, INS, OEO, VNS, VEO;
relation ACNS # ACEO;

[
    run FNS
    ||
    run FEO
    ||
    await 5 tick;
    emit ACNS
]
```

**Test du programme**
```
feux>;;;;;;;;;;;;;;;;;;;
--- Output: RNS REO
--- Output: RNS REO
--- Output: RNS REO
--- Output: RNS REO
--- Output: RNS REO
--- Output: RNS REO %% emission ACNS
--- Output: REO VNS
--- Output: REO VNS
--- Output: REO VNS
--- Output: REO VNS
--- Output: REO ONS
--- Output: REO RNS
--- Output: VEO RNS
--- Output: VEO RNS
--- Output: VEO RNS
--- Output: VEO RNS
--- Output: OEO RNS
...
```

**Exercice 2 Q 1- Feu code générique**
```
module F :

input AM;
output AL, R, V, O;

loop
    abort
        sustain R
    when AM

    emit R;
    repeat 4 times
        emit V; pause;
    end repeat
    emit O; pause;
    emit AL

end loop
```
```
module feux :

output RNS, REO, INS, OEO, VNS, VEO;
input ACNS, ACEO;
relation ACNS # ACEO;

[
    run F[signal ACNS/AM, ACEO/AL, RNS/R, VNS/V,ONS/O]
    ||
    run F[signal ACEO/AM, ACNS/AL, REO/R, VEO/V,OEO/O]
    ||
    await 5 tick;
    emit ACNS
]
```

**Exercice 2 : signaux d'extinction et d'allumage**
```
module F :

input AM;
output AL, R, V, O, RE, VE, OE;

loop
    emit R;
    await AM;
    pause;
    emit RE;
    emit V;
    await 4 tick:
    emit VE;
    emit O; pause;
    emit OE
    emit AL
end loop

end module;
```
```
module feux :

output RNS, RNSE, REO, REOE, ONS, ONSE, OEO, OEOE, VNS, VNSE, VEO, VEOE;
input ACNS, ACEO;
relation ACNS # ACEO;

[
    run F[signal ACNS/AM, ACEO/AL, RNS/R, VNS/V,ONS/O, RNSE/RE, VNSE/VE, ONSE/OE]
    ||
    run F[signal ACEO/AM, ACNS/AL, REO/R, VEO/V,OEO/O, REOE/RE, VOEE/VE, OEOE/OE]
    ||
    await 5 tick;
    emit ACNS
]
```

**Exercice 2 Q 3**
```
module F :

input AM;
output AL, R, V, O, RE, VE, OE;

loop 
    loop
        emit R;
        await AM;
        pause;
        emit RE;
        emit V;
        await 4 tick:
        emit VE;
        emit O; pause;
        emit OE
        emit AL
    end loop
each RESET

end module;
```
**Exercice 2 Q 4**
```
module F :

input AM, D;
output AL, R, V, O;

loop
    abort
        sustain R
    when AM

    emit R;
    await immediate D;
    repeat ?D times
        emit V; pause;
    end repeat
    emit O; pause;
    emit AL

end loop

end module;
```
```
module C :

input R, A1, A2;
output D : integer;
loop;
await R
    var duree := 2 : integer in
    abort
        loop
            present A1 then duree := duree + 1 end present;
            present A2 then duree := duree + 1 end present;
        each tick
    when [not R];
    emit D(duree);
    end var
end loop
end module
```
```
module feux :

output RNS, REO, INS, OEO, VNS, VEO;
input ACNS, ACEO;
input AN, AS, AE, AO;
input DNS : integer;
input DEO : integer;
relation ACNS # ACEO;

[
    run C[signal RNS/R, AN/A1, AS/A2, DNS/D]
    ||
    run C[signal REO/R, AE/A1, AO/A2, DEO/D]
    ||
    run F[signal ACNS/AM, ACEO/AL, RNS/R, VNS/V,ONS/O, DNS/D]
    ||
    run F[signal ACEO/AM, ACNS/AL, REO/R, VEO/V,OEO/O, DEO/D]
    ||
    await 5 tick;
    emit ACNS
]
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