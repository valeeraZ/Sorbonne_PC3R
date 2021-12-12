//
// Created by Sylvain on 08/02/2021.
//

#ifndef TME2_PAQUET_H
#define TME2_PAQUET_H
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
/**
 * paquet, avec un nom + number
 */
struct paquet
{
    char * nom;
};
/**
 * initialiser un paquet avec un nom
 */
void makePaquet(struct paquet * p, char * str);
/**
 *  free un pointer paquet
 */
void freePaquet(struct paquet * p);

#endif //TME2_PAQUET_H
