//
// Created by Sylvain on 08/02/2021.
//

#ifndef TME2_TAPIS_H
#define TME2_TAPIS_H
#include <stdlib.h>
#include <stdio.h>
#include <fthread.h>
#include "paquet.h"
/**
 * tapis, implementer un FIFO et la condition variable en fair thread
 */
struct tapis
{
    char * nom;
    struct paquet ** fifo;
    size_t allocsize; //capacité
    size_t begin;
    size_t sz; //quantité
    ft_event_t * cv;
};

/**
 *  initialiser un tapis
 */
void makeTapis(char * str, struct tapis * t, size_t sz, ft_event_t * cv);

/**
 *  renvoyer true si tapis est vide
 */
int empty(struct tapis * t);

/**
 *  renvoyer true si tapis est plein
 */
int full(struct tapis * t);

/**
 * défiler un paquet depuis le tapis
 * appeleé par consommateur
 */
struct paquet * defiler(struct tapis * t, int cpt);

/**
 * enfiler un paquet dans le tapis
 * appeleé par producteur
 */
void enfiler(struct tapis * t, struct paquet * p);
#endif //TME2_TAPIS_H