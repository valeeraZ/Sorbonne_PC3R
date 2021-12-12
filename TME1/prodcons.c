#include<stdio.h>
#include<string.h>
#include<pthread.h>
#include<stdlib.h>
#include<unistd.h>

char * produits[5] = {"pomme", "orange", "peche", "ananas", "banane"};
int cible_production = 3;
int nb_prod = 5;
int nb_con = 4;
int size_tapis = 10;
int cpt;

/**
 * paquet, avec un nom + number
 */ 
struct paquet
{
    char * nom;
};
/**
 * tapis, implementer un FIFO et des conditions variables
 */
struct tapis
{
    struct paquet ** fifo;
	size_t allocsize; //capacité
	size_t begin;
	size_t sz; //quantité
	pthread_mutex_t lock;
	pthread_cond_t  cv_full;
	pthread_cond_t  cv_empty ;
};
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
/**
 *  initialiser un tapis FIFO
 */
void makeTapis(struct tapis * t, size_t sz){
    t->allocsize = sz;
    t->begin = 0;
    t->sz = 0;

    t->fifo = malloc(t->allocsize * sizeof(struct paquet));

    if(pthread_mutex_init(&t->lock, NULL) != 0 ){
        perror("error on mutex");
        exit(1);
    }

    if(pthread_cond_init(&t->cv_empty, NULL) != 0){
        perror("error on condition");
        exit(1);
    }

    if(pthread_cond_init(&t->cv_full, NULL) != 0){
        perror("error on condition");
        exit(1);
    }
        
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
struct paquet * defiler(struct tapis * t){
    pthread_mutex_lock(&t->lock);
    while(empty(t) && cpt > 0){
        pthread_cond_wait(&t->cv_empty, &t->lock);
    }
    if(full(t)){
        pthread_cond_signal(&t->cv_full);
    }
    struct paquet * p = t->fifo[t->begin];
    t->sz --;
    t->begin = (t->begin + 1) % t->allocsize;
    cpt --;
    pthread_mutex_unlock(&t->lock);
    pthread_cond_signal(&t->cv_full);
    return p;
}

/**
 * enfiler un paquet dans le tapis
 * appeleé par producteur
 */ 
void enfiler(struct tapis * t, struct paquet * p){
    pthread_mutex_lock(&t->lock);
    while(full(t)){
        pthread_cond_wait(&t->cv_full, &t->lock);
    }
    if(empty(t)){
        pthread_cond_signal(&t->cv_empty);
    }
    t->fifo[ (t->begin + t->sz) % t->allocsize ] = p;
    t->sz++;
    pthread_mutex_unlock(&t->lock);
    pthread_cond_signal(&t->cv_empty);
}

/**
 *  producteur 
 */
struct prod{
    char * nomProduit;
    int cibleProduction;
    int nbProduction;
    struct tapis * tapis;
};

/**
 * consommateur
 */ 
struct cons{
    int id;
    //int * cpt;
    struct tapis * tapis;
};

/**
 * free un producteur
 */ 
void freeProd(struct prod * p){
    p = NULL;
    free(p);
}

/**
 * job de producteur
 */
void * jobProd(void * prod)
{
    struct prod * p = (struct prod *) prod;

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
    }
    free(p);
} 

/**
 *  job de consommateur
 */ 
void * jobCons(void * con)
{

    struct cons * c = (struct cons *) con;

    while(cpt > 0){
        struct paquet * paquet = defiler(c->tapis);
        if(cpt > 0){
            printf("C%d mange %s \n", c->id, paquet->nom);
            freePaquet(paquet);
        }
    }
    free(c);
}

int main(){
    pthread_t tProd[nb_prod];
    pthread_t tCons[nb_con];

    struct tapis tapis;
    makeTapis(&tapis, size_tapis);

    cpt = cible_production * nb_prod;

    int i = 0;
    int j = 0;
    while(i < nb_prod){
        struct prod * p = malloc(sizeof(struct prod));
        p->nomProduit = produits[i];
        p->nbProduction = 0;
        p->cibleProduction = cible_production;
        p->tapis = &tapis;
        if(pthread_create(&(tProd[i]), NULL, &jobProd, p) != 0){
            perror("error on creation of thread");
        }
        i++;
    }

    while(j < nb_con){
        struct cons * c = malloc(sizeof(struct cons));
        //c->cpt = &cpt;
        c->id = j;
        c->tapis = &tapis;
        if(pthread_create(&(tCons[j]), NULL, &jobCons, c) != 0){
            perror("error on creation of thread");
        }
        j++;
    }

    i = 0;
    while(i < nb_prod){
        pthread_join(tProd[i], NULL);
        i++;
    }

    j=0;
    while(j < nb_con){
        pthread_join(tCons[j], NULL);
        j++;
    }

    free(tapis.fifo);
    pthread_cond_destroy(&tapis.cv_empty);
    pthread_cond_destroy(&tapis.cv_full);

    return 0;
}


