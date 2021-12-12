Binôme: 

- Wenzhuo ZHAO
- Chengyu YANG

# *News*: une application web d'actualités

Avec notre application *News*, vous recevrez toute l'actualités internationale en regroupant les catégories et les titres de plusieurs sources en un flux unique mis à jour chaque jour. Pour connaître l'actualités du jour qui vous intéressent , il est simple de vous inscrire et nous dire les catégories intéressés. 

# API web choisie

Nous utiliserons l'API https://newsapi.org/ qui collecte des nouvelles et des histoires sur de différentes presses et les articles de blogs en internet par JSON API. Cette API est gratuite pour un plan développeur qui permet de faire 100 requêtes chaque jour. Pour une requête, elle donne plusieurs critères possibles pour la recherche d'articles:

- Mot-clés ou phrase 
- Une date ou un plage de dates
- Éditeurs
- Langages 

Notre application cherche des articles par des mot-clés, la date du jour et des langages selons les critères saisies par utilisateurs. 

Pour chaque article, cette API donne l'information de:

- la source
- l'auteur
- le titre
- la description
- l'URL vers une image de vignette (thumbnail)
- la date de publication
- le contenu court en centaine catactères
- l'URL vers l'article original 

Pour illustrer un article aux utilisateurs, nous utilisons ces informations et les rangeons dans un composant qui sera détaillé dans la suite de ce document.

# Fonctionnalités de l'application

- Notre application collecte 50 articles chaque jour, selon les catégories et le langage configuré par l'utilisateur. 
- Les articles seront illustrés dans un flux d'information comme Twitter
- L'utilisateur peut ajouter un article au favori
- **Le flux d'articles est mis à jour à 18:00 de chaque jour. Les articles du jour précédent n'apparaîtront plus.**

# Cas d'utilisation

- Alice s'inscrit et se connecte à l'application, une page d'accueil lui montre 20 catégories à choisir. Elle en choisit un ou plusieurs. La page suivante donne plusieurs options de langages de sources, elle en choisit un ou plusieurs. Enfin, elle arrive à l'écran qui lui donne les 50 articles dans les catégories qu'elle vient de choisir. 
- Bob se connecte à l'application, il peut lire les 50 articles dans les catégories qu'il a choisit lors de l'inscription.
- Alice et Bon peut aussi ajouter des articles à leurs favoris et ainsi voir sa liste de favori.
- Chloé n'est pas connectée à l'application, mais elle peut aussi lire 50 articles de divers sujets.

# Données

## SQL

### user

Nous stockons les informations de comptes:

| id_user (Clé primaire)           | email                                   | pseudo  | password                                                     |
| -------------------------------- | --------------------------------------- | ------- | ------------------------------------------------------------ |
| 8a50811d7712c3e3017712c980e00000 | wenzhuo.zhao@etu.sorbonne-universite.fr | Wenzhuo | `$2a$10$69OlXtrnBg7MH6dH73YTLuc5Q83llCxT3KuE4L/dV0sNFETd8Ycca` |

Le champ "id" est généré automatiquement et unique pour chaque ligne, il prend le rôle d'être la clé primaire. Le mot de passe sera encrypté par l'algorithme MD5.

### category

Nous donnons une énumération de *20 catégories* qui sont disponibles pour utilisateurs à choisir:

| id_category (Clé primaire) | name_category |
| -------------------------- | ------------- |
| 1                          | Apple         |
| 2                          | Tesla         |
| 3                          | Bitcoin       |
| 4                          | ...           |

### subscription (user-category)

Puis, nous sauvegardons le lien entre utilisateur et catégorie:

| id_user (Clé étrangère)          | id_category (Clé étrangère) |
| -------------------------------- | --------------------------- |
| 8a50811d7712c3e3017712c980e00000 | 2                           |
| 8a50811d7712c3e3017712c980e00000 | 3                           |
| ff808181771648260177164953790000 | 15                          |
| ...                              | ...                         |

### favorites

Pour permettre ajouter des articles au favori, nous désignons une collection de favori. Le champ `id_article` est représenté dans un document d'un article dans la base de données MongoDB.

| id_user                          | id_article |
| -------------------------------- | ---------- |
| 8a50811d7712c3e3017712c980e00000 | 0          |

Voici un schéma de modèles de tables.

<img src="https://raw.githubusercontent.com/valeeraZ/-image-host/master/Screenshot%202021-03-28%20at%2022.35.18.png" alt="Screenshot 2021-03-28 at 22.35.18" style="zoom:50%;" />

## NoSQL (MongoDB)

### articles

Nous stockons un article dans plusieurs champs dans un document de MongoDB.

| id_article | keyword | source     | author         | title                      | description                                 | url  | urlImage | publisedAt           | content                                                      |
| ---------- | ------- | ---------- | -------------- | -------------------------- | ------------------------------------------- | ---- | -------- | -------------------- | ------------------------------------------------------------ |
| 1          | Apple   | TechCrunch | Zack Whittaker | Apple releases iPhone, ... | Apple has released an update for iPhones... | ...  | ...      | 2021-03-27T23:30:37Z | Apple has released an update for iPhones, iPads and Watches... |

# Mise à jour de données

Nous appelons à l'API externe **à 18:00 chaque jour** et sauvegardons **50 articles par catégorie** et comme nous offrons 20 catégories pour utilisateurs à choisir lors de l'inscription, chaque jour la base de données charge **20 * 50 = 1,000** nouveaux articles et *supprimer* les articles du jour précédent pour décharger l'espace de disque.

# Serveur

Nous réalisons la **REST API** donc optons l'approche *ressource*. Nous réaliserons deux composants principaux.

- Authentification: principalement par méthode POST/PUT
  - Inscription: permettre d'envoyer des paramètres d'inscription et retourner une réponse ou une erreur
  - Connexion:  permettre d'envoyer des paramètres d'inscription et retourner une **JWT** ou une erreur
- Articles: par méthode POST
  - Unes: permettre d'envoyer un identifiant d'utilisateur et son JWT et retourner 50 nouveaux articles

# Plan du site

![News (1)](https://raw.githubusercontent.com/valeeraZ/-image-host/master/News%20(1).png)

Nous désignons une single page application (monopage). Cette page contient plusieurs parties:

- Une application bar (les 3 barres en haut à gauche) qui permet de configurer des paramètres de comptes. Cette partie correspond aux ressources fournies par le composant *Authentification*.
- Un containeur qui est le composant principal, illustrant les ressources du composant *Articles*.
  - Une navigation de catégories (les mots "Apple", "Samsung") qui permet de changer la catégorie donc lire des articles dans une autre catégorie.
  - Des articles qui est représenté sous forme d'un button contenant son titre, son contenu court, son auteur et une image permettant de cliquer à sauter vers le lien de l'article original

# Requêtes et Réponses

Toutes les requêtes seront réalisées en requête HTTP parce que la plupart entre elles concerne l'authentification pour une demande de ressource. Nous mettons des arguments dans la partie *Parameters* et la JWT dans la partie *Header*.

Les réponses seront un contenu JSON, elles apporteront soit une réponse simple ou une erreur pour le composant Authentification, soit une ressource représentant des articles.

 # Résumé

![systeme](https://raw.githubusercontent.com/valeeraZ/-image-host/master/systeme.png)