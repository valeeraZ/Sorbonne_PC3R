//
// Created by Sylvain on 08/02/2021.
//
#include "threads.h"
#include "paquet.h"
/**
 * job de producteur
 */
void jobProd(void * prod)
{
    struct prod * p = prod;

    while(p->nbProduction < p->cibleProduction){
        struct paquet * paquet = malloc(sizeof(struct paquet));

        int length = snprintf( NULL, 0, "%d", p->nbProduction);
        char * nb = malloc( length + 1 );
        snprintf( nb, length + 1, "%d", p->nbProduction );

        char * result = malloc(strlen(p->nomProduit) + strlen(nb) + 1);
        sprintf(result, "%s%s%s", p->nomProduit, " ", nb);
        makePaquet(paquet, result);
        enfiler(p->tapis, paquet);
        p->nbProduction++;
        printf("PRODUCTION: P%d produit %s \n", p->id, paquet->nom);
        fprintf(p->journalProd, "PRODUCTION: P%d produit %s \n", p->id, paquet->nom);
        ft_thread_cooperate();
    }
    free(p);
}

void jobCons(void * cons){
    struct cons * c = cons;

    while((*c->cpt) > 0){
        struct paquet * paquet = defiler(c->tapis, *(c->cpt));
        if(*(c->cpt) > 0){
            (*c->cpt) --;
            printf("CONSOMMATION: C%d mange %s \n", c->id, paquet->nom);
            fprintf(c->journalCons, "CONSOMMATION: C%d mange %s \n", c->id, paquet->nom);
            freePaquet(paquet);
        }
        ft_thread_cooperate();
    }
    ft_thread_generate(*(c->fin));
    free(c);
}

void jobMes(void * mes){
    struct messenger * m = mes;
    //sched = ordonnanceur courant
    ft_scheduler_t sched = ft_thread_scheduler();
    //détacher de l'ordonnanceur courant
    ft_thread_unlink();

    while(*(m->cpt) > 0){
        //attacher au producteur
        ft_thread_link(*(m->schedProd));
        struct paquet * p = defiler(m->tapisProd, *(m->cpt));
        if(*(m->cpt) > 0){
            ft_thread_unlink();
            //détacher du producteur, attaché au consommateur
            printf("VOYAGE: %s par messager %d \n", p->nom, m->id);
            fprintf(m->journalMes, "VOYAGE: %s par messager %d \n", p->nom, m->id);
            ft_thread_link(*(m->schedCons));
            enfiler(m->tapisCons,p);
        }
        ft_thread_unlink();
    }
    ft_thread_link(sched);
    free(m);
}

void jobTerminaison(void * ter){
    struct terminaison * t = ter;
    ft_thread_await(*t->cvTermine);
}
