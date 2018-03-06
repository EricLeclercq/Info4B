#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

/* creation anarchique de processus 	      */
/* sans test de la valeur de retour de fork() */ 

int main(int argc, char *argv[])
{ 
  int i, valeur;
  for(i=0; i<5; i++)
  {
   valeur = fork(); 
   printf ("Je suis le processus : %d\n -- retour fork =%d", (int)getpid(),valeur);
  }

  sleep(20);
}
