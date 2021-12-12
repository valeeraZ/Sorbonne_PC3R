//
// Created by Sylvain on 08/02/2021.
//
#include "paquet.h"

/**
 * initialiser un paquet avec un nom
 */
void makePaquet(struct paquet * p, char * str){
    char * dest = malloc( sizeof(char) * (strlen(str) + 1));
    for(size_t i = 0; i <= strlen(str); i++){
        dest[i] = str[i];
    }
    p->nom = dest;
}
/**
 *  free un pointer paquet
 */
void freePaquet(struct paquet * p){
    if(p != NULL){
        free(p->nom);
        free(p);
    }
}