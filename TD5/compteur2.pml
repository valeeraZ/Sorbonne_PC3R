# define LIMITE 4
# define NB_INC 4

int compteur = 0;

proctype incrementeur(int id; chan fini; chan obs){
    int i = 0;
    int temp;
    debut:
        atomic{i = i + 1;
        temp = compteur;
        temp = temp + 1;
        compteur = temp;}
        obs!id, temp;
        if
            :: i < LIMITE -> goto debut
            :: i >= LIMITE -> goto fin
        fi
    fin:
        fini!id
}

proctype observateur(chan obs; chan fini){
    int x, y;
    int done = 0;
    debut:
        do
        :: obs?(x,y) -> printf("L'incrementeur %d a mis le compteur a %d.\n", x, y)
        :: fini?x -> printf("L'incrementeur %d a fini.\n", x); done = done + 1;
        :: done == NB_INC -> goto fin
        od
    fin:
        printf("Fin du programme.\n")
        assert compteur == LIMITE * NB_INC
}

init{
    int n, j;
    int lim = NB_INC;
    chan obs = [0] of {int, int};
    chan fini = [0] of {int};
    for(j:1..lim){
        run incrementeur(j, fini, obs)
    }
    run observateur(obs, fini)
}
