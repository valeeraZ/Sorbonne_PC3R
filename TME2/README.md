Temps utilisé: environ 5 heures  
  
Vu que la bibliothèque `fthread` est légère, elle est déjà inclue dans ce projet  

#Compilation
- soit `gcc main.c threads.c paquet.c tapis.c 
 -I $<chemin>/ft_v1.0/include -L $<chemin>/ft_v1.0/lib 
 -lfthread -lpthread -o tme2`
 
- soit `make`

- CMake est aussi disponible