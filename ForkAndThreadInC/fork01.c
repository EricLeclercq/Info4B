#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main (void)
{ 
  int valeur;
  valeur = fork(); 
  if (valeur == 0) sleep (4); 
  printf (" Valeur retournee par la fonction fork : %d\n", (int)valeur);
  printf ("Je suis le processus : %d\n", (int)getpid());
}
