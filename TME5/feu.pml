mtype {ROUGE, ORANGE, VERT, INDETERMINEE}

chan obs = [0] of {mtype, bool};

active proctype observateur(){
    mtype couleur, ancienne;
    bool cli; 
    ancienne = INDETERMINEE;
    do
    :: obs?(couleur, cli) -> 
        if
        ::atomic{couleur == ORANGE ->
                assert(cli == true || ancienne != ROUGE);
                ancienne = ORANGE
            }
        ::atomic{couleur == ROUGE ->
                assert(ancienne != VERT);
                ancienne = ROUGE
            }
        ::atomic{couleur == VERT ->
                assert(ancienne != ORANGE);
                ancienne = VERT
            } 
        ::atomic{cli -> assert(couleur == ORANGE)}   
        fi
    od
}

active proctype feu(){
    bool clignotant = false;
    mtype couleur = INDETERMINEE;

    initial:
        couleur = ORANGE;
        clignotant = true;

        if
        :: true -> clignotant = false; goto etatrouge;
        :: true -> goto initial
        fi
    etatrouge:
        atomic{
            couleur = ROUGE;
            obs!couleur, clignotant;
        }
        if
        :: true -> goto etatvert;
        :: true -> goto panne;
        :: true -> goto etatrouge;
        fi
    etatvert:
        atomic{
            couleur = VERT;
            obs!couleur, clignotant;
        }
        
        if
        :: true -> goto etatorange;
        :: true -> goto panne;
        :: true -> goto etatvert;
        fi
    etatorange:
        atomic{
            couleur = ORANGE;
            obs!couleur, clignotant;
        }  
        if
        :: true -> goto etatrouge;
        :: true -> goto panne;
        :: true -> goto etatorange;
        fi 
    panne:
        clignotant = true;
    panne_boucle:
        couleur = ORANGE;
        obs!couleur, clignotant;
        if
        :: true -> goto panne_boucle
        fi   
}