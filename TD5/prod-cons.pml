#define MAX 5
#define PMAX 5
#define CMAX 4
#define TAILLE 3

mtype = {p, s, d, r};

proctype producteur(chan min; int id; chan obs; chan fini; chan ack){
    int n = 1
    int k
    debut:
        if
        ::obs!p; ack?k; min!n; printf("Prod %d produit %d.\n", id, n);
            if
            :: n < MAX -> n = n + 1; goto debut
            :: n >= MAX -> goto fin
            fi
        ::min!n; printf("Prod %d produit secretement %d.\n", id, n);
            if
            :: n < MAX -> n = n + 1; goto debut
            :: n >= MAX -> goto fin
            fi
        fi
    fin:
        fini!0
}

proctype gestionnaire(chan min; chan mout; chan obs; chan ack; chan fini){
    int tableau[TAILLE]
    int courant = 0
    int k, i, l
    boucle:
        do
        :: courant < TAILLE ->
            min?k;
            obs!s; ack?l;
            tableau[courant] = k;
            printf("Gestionnaire stocke %d en position %d.\n", k, courant)
            courant = courant + 1;
            goto boucle
        :: courant > 0 -> 
            obs!d; ack?l;
            mout!tableau[0]
            printf("Gestionnaire envoie %d.\n", tableau[0])
            courant = courant - 1
            for(i:0..(courant-1)){
                tableau[i]=tableau[i+1]
            };
            goto boucle
        :: fini?k -> goto fin
        od
    fin:
        printf("Mort du gestionnaire.\n")
}

proctype consommateur(chan mout; int id; chan obs; chan ack; chan fini){
    int k,l
    boucle:
        if
        :: mout?k ->
            obs!r; ack?l;
            printf("Consommateur %d recoit %d.\n", id, k)
            goto boucle
        :: fini?k -> goto fin
        fi
    fin:
        printf("Mort du consommateur %d.\n", id)
}

proctype observateur(chan obs ; chan ack){
    int produits = 0;
    int stockes = 0;
    int recus = 0;
    mtype m;
    boucle:
        assert(produits >= stockes + recus);
        assert(stockes <= TAILLE);
        assert(stockes >= 0);
        obs?m -> if
            :: m == p -> produits ++;
            :: m == s -> stockes ++;
            :: m == d -> stockes --;
            :: m == r -> recus ++;
            fi
        ack!0
        printf("Observateur: %d; %d; %d\n", produits, stockes, recus)
        goto boucle
}

init{
    int j, k;
    int cm = CMAX;
    int pm = PMAX;
    chan min = [0] of {int}
    chan mout = [0] of {int}
    chan obs = [0] of {mtype}
    chan fini = [0] of {int}
    chan fini2 = [0] of {int}
    chan ack = [0] of {int}
    run observateur(obs, ack)
    for(j:1..pm){
        run producteur(min, j, obs, fini, ack)
    }
    run gestionnaire(min, mout, obs, ack, fini2)
    for(j:1..cm){
        run consommateur(mout, j, obs, ack, fini2)
    }
    for(j:1..pm){
        fini?k
    }
    for(j:1..(cm+1)){
        fini2!0
    }
}