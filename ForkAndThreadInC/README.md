Pour compiler les programmes en C : `gcc -o prog prog.c`

La commance gcc a pour effet de générer l'executable prog à partir du fichier source prog.c, on complètera ensuite avec des bibliothèques dont les appels seront effectifs à l'édition de liens.

Pour le lancer le programme : `./prog`

Vous pouvez observer l'activité des processus au moyen des commandes : `pstree` ou `top` (à lancer dans une autre fenêtre terminal). `pstree -p` permet d'afficher les numero de processus, `ps -efL` permet de lister les threads crées.

Partie Fork en C :

`fork00.c` :  crée un processus, affiche valeur de retour du `fork()`

`fork01.c` :  crée un processus, puis le père s'arrête et le fils attend 4s avant d'afficher. 
                Illustre la technique fork and die. Utiliser pstree pour observer que le fils est adopté par `systemd` ou `init`

`fork02.c` : fork multiples

`fork03.c` : création  de processus en boucle sans test de la valeur de retour

Partie Thread en C :
