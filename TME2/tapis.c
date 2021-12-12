//
// Created by Sylvain on 08/02/2021.
//
#include "tapis.h"
void makeTapis(char * str, struct tapis * t, size_t sz, ft_event_t * cv){
    char * dest = malloc( sizeof(char) * (strlen(str) + 1));
    for(size_t i = 0; i <= strlen(str); i++) {
        dest[i] = str[i];
    }
    t->nom = dest;
    t->allocsize = sz;
    t->begin = 0;
    t->sz = 0;
    t->fifo = malloc(sz * sizeof(struct paquet));
    t->cv = cv;
}

/**
 *  renvoyer true si tapis est vide
 */
int empty(struct tapis * t)
{
    return t->sz == 0 ;
}
/**
 *  renvoyer true si tapis est plein
 */
int full(struct tapis * t)
{
    return t->sz == t->allocsize;
}
/**
 * défiler un paquet depuis le tapis
 * appeleé par consommateur
 */
struct paquet * defiler(struct tapis * t, int cpt){
    while(empty(t) && cpt > 0){
        ft_thread_await(*t->cv);
        ft_thread_cooperate();
    }
    struct paquet * p = t->fifo[t->begin];
    t->sz --;
    t->begin = (t->begin + 1) % t->allocsize;
    //(*cpt) --;
    ft_scheduler_broadcast(*t->cv);
    return p;
}

/**
 * enfiler un paquet dans le tapis
 * appeleé par producteur
 */
void enfiler(struct tapis * t, struct paquet * p){
    while(full(t)){
        ft_thread_await(*t->cv);
        ft_thread_cooperate();
    }
    /*if(empty(t)){
        pthread_cond_signal(&t->cv_empty);
    }*/
    t->fifo[ (t->begin + t->sz) % t->allocsize ] = p;
    t->sz++;
    ft_thread_generate(*t->cv);
}
