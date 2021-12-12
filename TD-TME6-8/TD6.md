



**Use Cases**

- Alice se connecte à l'application. Une page d'accueil lui suggere des produits en promotion. Elle en choisit un, elle arrive sur un écran qui liste les caractéristiques du produit en question.

- Bob se connecte à l'application. Il entre la chaine "theiere" dans un champ de rechercher. On lui propose un écran qui contient une liste d'objets correspondant à ce critere. Il en choisit un, il est redirigé vers le site de sa banque pour s'authentifier et payer, ce qui place une commande.

- <panier>

**Fonctionnalités**

-> authentification (compte utilisateur)
-> page d'accueil
-> suggestions pour les utilisateurs
-> pages produits
-> recherche par chaine de caractere
-> connexion avec le reseau bancaire
-> enregistrement d'une commande
-> panier d'achat

** Bibliotheque **

**Use Cases**

- Alice s'authentifie sur le site de la bibliothèque. Elle arrive sur une page d'accueil qui lui indique les emprunts en cours. Elle cherche un livre par son titre. Elle peut valider un emprunt de ce livre car il est disponible.

- Bob est bibliothécaire, il s'identifie. Il ajoute un nouvel exemplaire d'un livre déjà présent dans le catalogue ainsi qu'un exemplaire d'un nouveau livre.

- Carole rend un livre qu'elle avait emprunté et clique dans l'application sur un bouton qui valide le retour.

**Fonctionnalités**

- compte utilisateur double : usager / bibliothécaire
- recherche de livre : par titre, par auteur, par année
- affichage des emprunts en cours (avec deadline)
- ajout de livre pour les bibliothécaire
- validation d'emprunt et de retour pour les usagers

**Protocole**

-> Requete REST (Méthode + ressource [+ arguments])
<- Réponse JSON

- *utilisateur*
GET id -> récupere les infos d'un utilisateur
POST login mdp -> authentifie un utilisateur / récupere     
    son identiant
PUT login mdp nom prenom -> crée un nouvel utilisateur / 
    récupere son identifiant [COMPTE BIBLI]
DELETE id -> supprime un utilisateur [COMPTE BIBLI]

- *livre*
GET titre -> récupérer une liste de livres qui correspondent 
    au titre
GET auteur -> récupérer une liste de livres qui correspondent
    à l'auteur
GET id -> récupérer un livre par son identifiant
PUT titre auteur stock -> ajouter un nouveau livre 
    [COMPTE BIBLI]
POST id stock -> mettre à jour les stock [COMPTE BIBLI]
DELETE id -> supprimer un livre [COMPTE BIBLI]

- *emprunt*
GET id -> récupérer des infos
POST id deadline -> mettre à jour une deadline [COMPTE BIBLI]
PUT id_livre id_user -> créer un emprunt
DELETE id -> rendre un livre emprunté

- **exemple 1**
-> GET http://bibli.fr/livre?titre="Le_Joueur_d'_echec"
<- [{id : 346, titre : "Le_Joueur_d'_echec", 
        auteur : "Zweig", stock : 2}]
-> GET http://bibli.fr/livre?auteur="Le_Joueur_d'_echec"
<- [{id : 346, titre : "Le_Joueur_d'_echec", auteur :   
        "Zweig", stock : 2},
    {id : 349, titre : "Le-Monde_d'_hier", auteur :   
        "Zweig", stock : 0}
-> PUT http://bibli.fr/emprunt?id_livre=346&id_user=0012
<- {id : 45}
-> DELETE http://bibli.fr/emprunt?id=45
<- {result : "deleted"}

**Parking**

**Use cases**

- Alice ouvre la page de l'appli, elle arrive sur une page qui contient la liste des parkings de la ville avec inscrit le nombre de places restantes dans chaque parking. Les parkings complets sont affichés en rouge.

**Fonctionnalités**

- dire si un parking est complet
- donner les places restantes d'un parking
- donner la capacité d'un parking





