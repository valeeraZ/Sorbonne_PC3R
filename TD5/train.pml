mtype {ROUGE, VERT};
mtype{rentre, sort}

proctype observateur(chan obs){
    mtype evt;
    int nb_trains_voie = 0;
    boucle:
        printf("%d trains sur la voie.", nb_trains_voie)
        assert(nb_trains_voie <= 1)
        obs?evt -> if
            :: evt == rentre; nb_trains_voie ++
            :: evt == sort; nb_trains_voie --
        fi
        goto boucle

}

proctype senseur(chan sens; chan signal){
    bool b;
    end:
        do
            ::sens?b -> signal!true
        od
}

proctype feu(chan swtch; chan change){
    bool b;
    mtype couleur;

    end_rouge:
        couleur = ROUGE;
        change!couleur;
    end_attente:
        swtch?b -> if  
            :: couleur == ROUGE -> goto end_vert
            :: couleur == VERT -> goto end_rouge
            :: couleur == VERT -> goto end_vert
            fi
    end_vert:
        couleur = VERT;
        change!couleur;
        goto end_attente;
}

proctype train(int id; chan obs; chan sensin; chan changefeu; chan sensout){
    mtype couleur;

    arrivee:
        printf("Train %d arrive.\n", id)
        sensin!true -> goto attente

    attente:
        printf("Train %d attends.\n", id)
        changefeu?couleur -> if
            :: couleur == VERT -> goto passe
            :: couleur == ROUGE -> goto attente
            fi
    
    passe:
        printf("Train %d passe.\n", id)
        obs!rentre
    
    sortie:
        obs!sort
        atomic{
            printf("Train %d sort.\n", id)
            sensout!true;
            goto arrivee
        }
}

proctype control(chan in1_signal; chan in2_signal;
                 chan out1_signal; chan out2_signal;
                 chan feu1_swtch; chan feu2_swtch) {
  bool b;

notrain_f1red_f2red:
  printf("00RR\n")
  if
  ::in1_signal?b -> goto train1_in_f1red_f2red
  ::in2_signal?b -> goto train2_in_f1red_f2red
  fi

train1_in_f1red_f2red:
  printf("I0RR\n")
  if
  ::in2_signal?b -> goto train1_in_train2_in_f1red_f2red
  ::feu1_swtch!true -> goto train1_pass_f1green_f2red
  fi

train1_in_train2_in_f1red_f2red:
  printf("IIRR\n")
  if
  ::true -> feu1_swtch!true -> goto train1_pass_train2_in_f1green_f2red
  ::true -> feu2_swtch!true -> goto train1_in_train2_pass_f1red_f2green
  fi

train1_pass_f1green_f2red:
  printf("P0VR\n")
  if
  ::in2_signal?b -> goto train1_pass_train2_in_f1green_f2red
  ::out1_signal?b -> feu1_swtch!true -> goto notrain_f1red_f2red
  fi

train1_pass_train2_in_f1green_f2red:
  printf("PIVR\n")
  out1_signal?b -> feu1_swtch!true -> goto train2_in_f1red_f2red

train2_in_f1red_f2red:
    printf("0IRR\n")
  if
  ::in1_signal?b -> goto train1_in_train2_in_f1red_f2red
  ::feu2_swtch!true -> goto train2_pass_f1red_f2green
  fi

train2_pass_f1red_f2green:
      printf("0PRV\n")
  if
  ::in1_signal?b -> goto train1_in_train2_pass_f1red_f2green
  ::out2_signal?b -> feu2_swtch!true -> goto notrain_f1red_f2red
  fi

train1_in_train2_pass_f1red_f2green:
    printf("IPRV\n")
  out2_signal?b -> feu2_swtch!true -> goto train1_in_f1red_f2red

}

init {
  chan in_sense1 = [0] of { bool };
  chan in_signal1 = [0] of { bool };
  chan in_sense2 = [0] of { bool };
  chan in_signal2 = [0] of { bool };

  chan out_sense1 = [0] of { bool };
  chan out_signal1 = [0] of { bool };
  chan out_sense2 = [0] of { bool };
  chan out_signal2 = [0] of { bool };

  chan feu_swtch1 = [0] of { bool };
  chan feu_change1 = [0] of { mtype };
  chan feu_swtch2 = [0] of { bool };
  chan feu_change2 = [0] of { mtype };

  chan obs = [0] of {mtype}
  atomic {
    run observateur(obs);

    run senseur(in_sense1, in_signal1);
    run senseur(in_sense2, in_signal2);
    run senseur(out_sense1, out_signal1);
    run senseur(out_sense2, out_signal2);

    run feu(feu_swtch1, feu_change1);
    run feu(feu_swtch2, feu_change2);

    run train(1, obs, in_sense1, feu_change1, out_sense1);
    run train(2, obs, in_sense2, feu_change2, out_sense2);

    run control(in_signal1, in_signal2,
                out_signal1, out_signal2,
                feu_swtch1, feu_swtch2);
  }

}