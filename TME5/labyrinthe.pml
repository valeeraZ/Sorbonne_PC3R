active proctype labyrinthe(){
    initial:
        if
            :: true -> printf("Entrer à (0,4)"); goto case0_4
        fi
    case0_4:
        if
            ::true -> printf("Haut vers (1,4)"); goto case1_4
        fi
    case1_4:
        if
            ::true -> printf("Gauche vers (1,3)"); goto case1_3
            ::true -> printf("Haut vers (2,4)"); goto case2_4
            ::true -> printf("Bas vers (0,4)"); goto case0_4
        fi
    case2_4:
        if
            ::true -> printf("Haut vers (3,4)"); goto case3_4
            ::true -> printf("Bas vers (1,4)"); goto case1_4
        fi
    case3_4:
        if
            ::true -> printf("Haut vers (4,4)"); goto case4_4
            ::true -> printf("Bas vers (2,4)"); goto case2_4
        fi
    case4_4:
        if
            ::true -> printf("Bas vers (3,4)"); goto case3_4
        fi
    case1_3:
        if
            ::true -> printf("Gauche vers (1,2)"); goto case1_2
            ::true -> printf("Droite vers (1,4)"); goto case1_4
        fi
    case1_2:
        if
            ::true -> printf("Gauche vers (1,1)"); goto case1_1
            ::true -> printf("Droite vers (1,3)"); goto case1_3
        fi
    case1_1:
        if
            ::true -> printf("Haut vers (2,1)"); goto case2_1
            ::true -> printf("Bas vers (0,1)"); goto case0_1
        fi
    case0_1:
        if
            ::true -> printf("Haut vers (1,1)"); goto case1_1
            ::true -> printf("Droite vers (0,2)"); goto case0_2
        fi
    case0_2:
        if
            ::true -> printf("Gauche vers (0,1)"); goto case0_1
            ::true -> printf("Droite vers (0,3)"); goto case0_3
        fi
    case0_3:
        if
            ::true -> printf("Gauche vers (0,2)"); goto case0_2
        fi
    case2_1:
        if
            ::true -> printf("Haut vers (3,1)"); goto case3_1
            ::true -> printf("Gauche vers (2,0)"); goto case2_0
        fi
    case2_0:
        if
            ::true -> printf("Droite vers (2,1)"); goto case2_1
        fi
    case3_1:
        if
            ::true -> printf("Haut vers (3,2)"); goto case3_2
        fi
    case3_2:
        if  
            ::true -> printf("Gauche vers (3,1)"); goto case3_1
            ::true -> printf("Droite vers (3,3)"); goto case3_3
        fi
    case3_3:
        if
            ::true -> printf("Haut vers (4,3)"); goto case4_3
            ::true -> printf("Gauche vers (3,2)"); goto case3_2
        fi
    case4_3:
        if
            ::true -> printf("Gauche vers (4,2)"); goto case4_2
            ::true -> printf("Bas vers (3,3)"); goto case3_3
        fi
    case4_2:
        if
            ::true -> printf("Gauche vers (4,1)"); goto case4_1
            ::true -> printf("Droite vers (4,3)"); goto case4_3
        fi
    case4_1:
        if
            ::true -> printf("Gauche vers (4,0)"); goto case4_0
            ::true -> printf("Droite vers (4,2)"); goto case4_2
        fi
    case4_0:
        if
            ::true -> goto final
            ::true -> printf("Bas vers (3,0)"); goto case3_0
            ::true -> printf("Droite vers (4,1)"); goto case4_1
        fi
    case3_0:
        if
            ::true -> printf("Haut vers (4,0)"); goto case4_0
        fi
    final:
        printf("Sortie"); assert true
}