//
// Created by Sylvain on 08/02/2021.
//

#include <stdio.h>
#include <fthread.h>
#include "threads.h"

char * produits[5] = {"pomme", "orange", "peche", "ananas", "banane"};
const int cible_production = 3;
const int nb_prod = 5;
const int nb_con = 4;
const int nb_mes = 4;
const int size_tapis = 15;
int cpt = cible_production * nb_prod;

int main(){

    ft_scheduler_t schedProd = ft_scheduler_create();
    ft_scheduler_t schedCons = ft_scheduler_create();
    ft_scheduler_t schedMes = ft_scheduler_create();

    ft_event_t cvProd = ft_event_create(schedProd);
    ft_event_t cvCons = ft_event_create(schedCons);
    ft_event_t cvTermine = ft_event_create(schedCons);

    struct tapis tapisProd, tapisCons;
    makeTapis("TapisProduction",&tapisProd, size_tapis, &cvProd);
    makeTapis("TapisConsommation", &tapisCons, size_tapis, &cvCons);

    FILE * journalProd = fopen("journal_production.txt","w");
    FILE * journalCons = fopen("journal_consommation.txt","w");
    FILE * journalMes = fopen("journal_messager.txt","w");

    int i = 0, j = 0, k = 0;
    while(i < nb_prod){
        struct prod * p = malloc(sizeof(struct prod));
        p->id = i;
        p->nomProduit = produits[i];
        p->nbProduction = 0;
        p->cibleProduction = cible_production;
        p->tapis = &tapisProd;
        p->schedProd = &schedProd;
        p->journalProd = journalProd;
        ft_thread_create(schedProd, jobProd, NULL, (void *)p);
        i++;
    }

    while(j < nb_con){
        struct cons * c = malloc(sizeof(struct cons));
        c->id = j;
        c->cpt = &cpt;
        c->tapis = &tapisCons;
        c->schedCons = &schedCons;
        c->fin = &cvTermine;
        c->journalCons = journalCons;
        ft_thread_create(schedCons, jobCons, NULL, (void *)c);
        j++;
    }

    while(k < nb_mes){
        struct messenger * m = malloc(sizeof(struct messenger));
        m->id = k;
        m->cpt = &cpt;
        m->tapisProd = &tapisProd;
        m->tapisCons = &tapisCons;
        m->schedProd = &schedProd;
        m->schedCons = &schedCons;
        m->journalMes = journalMes;
        ft_thread_create(schedMes, jobMes, NULL, (void *)m);
        k++;
    }

    struct terminaison * ter = malloc(sizeof(struct terminaison));
    ter->cvTermine = &cvTermine;
    ft_thread_t terminaisonThread = ft_thread_create(schedCons, jobTerminaison, NULL, (void *)ter);

    ft_scheduler_start(schedProd);
    ft_scheduler_start(schedCons);
    ft_scheduler_start(schedMes);

    pthread_join(ft_pthread(terminaisonThread),NULL);
    fclose(journalProd);
    fclose(journalCons);
    fclose(journalMes);
    return 0;
}
