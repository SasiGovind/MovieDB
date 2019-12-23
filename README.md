# Application MovieDB
## Présentation

MovieDB est une application démontrant l'utilisation du MVC en appelant un API REST ([TheMovieDB](https://www.themoviedb.org/documentation/api)) dans une application android codé en Java sous Android Studio. Cette application vous listera les films populaires, les meilleurs notés et ceux à venir. Vous pouvez également en apprendre plus

## Prérequis

 1. Avoir Android Studio installé ou un IDE de votre choix
 2. Récupérer la branche prod du lien ci-dessous
 [Link GitHub](https://github.com/SasiGovind/MovieDB.git)

## Consignes respectées :

 1. Appel à une API Rest pour récupérer des données.
 2. Deux écrans : un écran avec une liste et un écran avec un détail de l'item.
 3. Stockage des données en cache (de toutes les données chargées).
 4. Architecture ModelViewControler(MVC).
 5. GitFlow
 7. Design Patterns : Singleton, Adapter, Builder
 8. Injection de dépendances
 9. Animation entre écrans 2 points (SharedElements)
 10. Utilisation de Fragment

## Autres Fonctionnalités : 

### Fonctions communes :
1. Design et couleurs personnalisés
2. Application icône personnalisé 
<img src="imgReadMe/1_icon.jpg" alt="icon">
3. Lancement de l'application : écran de chargement animé
<img src="imgReadMe/23_app_loading.jpg" width="20%" heignt="40%" alt="loading_screen">

### Movie List Activity (1er Ecran) :

 1. Bottom Navigation View + 3 listes consultables (film populaire, meilleurs notes, à venir)
 
 <img src="imgReadMe/3_Main_pop.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/4_Main_top.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/5_Main_upc.jpg" width="20%" heignt="40%" alt="pop">
 
 2. Navigation de pages (flèches sur le Bottom Navigation View)
 <img src="imgReadMe/6_Nav_pages.jpg" width="20%" heignt="40%" alt="pop">
 
 3. Recherche de films améliorée (icon 'loupe' en haut à droite)
 <img src="imgReadMe/20_search.jpg" width="20%" heignt="40%" alt="pop">
 4. Choix de langues (icon 'language' en haut à droite) 
 5. Nettoyer le cache (icon 'clear_all' en haut à droite)
 6. Tirer pour recharger la page
 <img src="imgReadMe/7_lang.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/8_3choix.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/9_refresh.jpg" width="20%" heignt="40%" alt="pop">
 
 7. Sélection de genres de films à afficher
<img src="imgReadMe/22_genres_selection.jpg" width="20%" heignt="40%" alt="genres_selection"> 
 
 8. AlertDialog de confirmation lorsque l'utilisateur quitte l'application
 <img src="imgReadMe/10_exit.jpg" width="20%" heignt="40%" alt="pop">
 9. Affichage de la page chargée en Toast

### Movie Detail List Activity (2ème Ecran) :

 1. Toucher le poster du film pour zoomer l'affiche.
 
 <img src="imgReadMe/11_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/14_md_exp.jpg" width="20%" heignt="40%" alt="pop">
 
 2. Separateur entre les différents éléments affichés.
  <img src="imgReadMe/15_md_sep.jpg" width="20%" heignt="40%" alt="pop">
 3. Option de recherche sur google du film (icon 'loupe' en haut à droite de l'écran)
  <img src="imgReadMe/16_md_g.jpg" width="20%" heignt="40%" alt="pop">
 4. Option de partage (icon 'share' en haut à droite de l'écran) : permet de partager les informations du film à quelqu'un.
  <img src="imgReadMe/17_md_share.jpg" width="20%" heignt="40%" alt="pop">
 5. Effet CollapsingToolbarLayout pour la bannière & l'effet du poster flottant.
 6. Option de recherche de cast sur google : en cliquant sur un membre du cast.
  <img src="imgReadMe/12_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/18_md_cast.jpg" width="20%" heignt="40%" alt="pop">
 7. Possibilité de visionner les bandes d'annonces des films.
  <img src="imgReadMe/13_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/19_md_trailers.jpg" width="20%" heignt="40%" alt="pop">
 8. Affichage de films similaires au film sélectioné.
 <img src="imgReadMe/21_similar_movies.jpg" width="20%" heignt="40%" alt="similar_movies">
  
### People Activity (3ème Ecran) :
 Contient le fragment people.
 <img src="imgReadMe/24_fragment.jpg" width="20%" heignt="40%" alt="fragment_people"> 
 
### Librairies :
 - Retrofit
 - Glide
 - Gson
 - RecyclerView
 - CardView
 - Fragment
 - Design
# Application MovieDB
## Présentation

MovieDB est une application démontrant l'utilisation du MVC en appelant un API REST ([TheMovieDB](https://www.themoviedb.org/documentation/api)) dans une application android codé en Java sous Android Studio. Cette application vous listera les films populaires, les meilleurs notés et ceux à venir. Vous pouvez également en apprendre plus

## Prérequis

 1. Avoir Android Studio installé ou un IDE de votre choix
 2. Récupérer la branche prod du lien ci-dessous
 [Link GitHub](https://github.com/SasiGovind/MovieDB.git)

## Consignes respectées :

 1. Appel à une API Rest pour récupérer des données.
 2. Deux écrans : un écran avec une liste et un écran avec un détail de l'item.
 3. Stockage des données en cache (de toutes les données chargées).
 4. Architecture ModelViewControler(MVC).
 5. GitFlow
 7. Design Patterns : Singleton, Adapter, Builder
 8. Injection de dépendances
 9. Animation entre écrans 2 points (SharedElements)
 10. Utilisatoin de Fragment

## Autres Fonctionnalités : 

### Fonctions communes :
1. Design et couleurs personnalisés
2. Application icône personnalisé 
<img src="imgReadMe/1_icon.jpg" alt="icon">
3. Lancement de l'application : écran de chargement animé
<img src="imgReadMe/23_app_loading.jpg" width="20%" heignt="40%" alt="loading_screen">

### Movie List Activity (1er Ecran) :

 1. Bottom Navigation View + 3 listes consultables (film populaire, meilleurs notes, à venir)
 
 <img src="imgReadMe/3_Main_pop.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/4_Main_top.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/5_Main_upc.jpg" width="20%" heignt="40%" alt="pop">
 
 2. Navigation de pages (flèches sur le Bottom Navigation View)
 <img src="imgReadMe/6_Nav_pages.jpg" width="20%" heignt="40%" alt="pop">
 
 3. Recherche de films améliorée (icon 'loupe' en haut à droite)
 <img src="imgReadMe/20_search.jpg" width="20%" heignt="40%" alt="pop">
 4. Choix de langues (icon 'language' en haut à droite) 
 5. Nettoyer le cache (icon 'clear_all' en haut à droite)
 6. Tirer pour recharger la page
 <img src="imgReadMe/7_lang.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/8_3choix.jpg" width="20%" heignt="40%" alt="pop"> <img src="imgReadMe/9_refresh.jpg" width="20%" heignt="40%" alt="pop">
 
 7. Sélection de genres de films à afficher
<img src="imgReadMe/22_genres_selection.jpg" width="20%" heignt="40%" alt="genres_selection"> 
 
 8. AlertDialog de confirmation lorsque l'utilisateur quitte l'application
 <img src="imgReadMe/10_exit.jpg" width="20%" heignt="40%" alt="pop">

 9. Affichage de la page chargée en Toast

### Movie Detail List Activity (2ème Ecran) :

 1. Toucher le poster du film pour zoomer l'affiche.
 
 <img src="imgReadMe/11_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/14_md_exp.jpg" width="20%" heignt="40%" alt="pop">
 
 2. Separateur entre les différents éléments affichés.
  <img src="imgReadMe/15_md_sep.jpg" width="20%" heignt="40%" alt="pop">
 3. Option de recherche sur google du film (icon 'loupe' en haut à droite de l'écran)
  <img src="imgReadMe/16_md_g.jpg" width="20%" heignt="40%" alt="pop">
 4. Option de partage (icon 'share' en haut à droite de l'écran) : permet de partager les informations du film à quelqu'un.
  <img src="imgReadMe/17_md_share.jpg" width="20%" heignt="40%" alt="pop">
 5. Effet CollapsingToolbarLayout pour la bannière & l'effet du poster flottant.
 6. Option de recherche de cast sur google : en cliquant sur un membre du cast.
  <img src="imgReadMe/12_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/18_md_cast.jpg" width="20%" heignt="40%" alt="pop">
 7. Possibilité de visionner les bandes d'annonces des films.
  <img src="imgReadMe/13_md.jpg" width="20%" heignt="40%" alt="pop">  <img src="imgReadMe/19_md_trailers.jpg" width="20%" heignt="40%" alt="pop">
 8. Affichage de films similaires au film sélectioné : 
 - En cliquant sur un film similaire : on accède à la fiche du film choisit.
 <img src="imgReadMe/21_similar_movies.jpg" width="20%" heignt="40%" alt="similar_movies">
  
### People Activity (3ème Ecran) :
 Contient le fragment people : on peut faire une recherche internet de la célébrité avec le button en bas à droite du fragment 
 <img src="imgReadMe/24_fragment.jpg" width="20%" heignt="40%" alt="fragment_people"> 
 
### Librairies :
 - Retrofit
 - Glide
 - Gson
 - RecyclerView
 - CardView
 - Fragment
 - Design
