**Banque00.java :** exemple mettant en évidence le problème d'accès concurrent à la ressource (un compte). Plusieurs éxécutions ne donnent pas le résultat attendu => déterminer la section critique, modifier le code en utilisant un moniteur

**Banque01.java :** mise en évidence de la section critique, encadrement par un moniteur

**Banque02.java :** utilisation d'un verrou au lieu d'un moniteur (attention le verrou est unique et protège la ressource) => le code nécessite de rendre public le verrou => effets de bords non souhaités => le ressource doit se protéger elle même des accès concurrent et ne pas déléguer cette tâche aux thread l'utilisant => reconcevoir le code 

**Banque03.java :** on remonte de la seciton critique dans la ressource, la ressource se protège elle même avec un verrou privé

**Banque04.java :** meme version avec un moniteur sur la méthode de retrait

Pour exécuter plusieurs fois le programme, depuis le shell bash :  
```{r, engine='bash', count_lines}
for i in `seq 1 100` ;  do java Banque01 ; done
```
