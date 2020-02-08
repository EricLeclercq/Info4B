#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main(void)
{ 
  int valeur;
  valeur = fork();
  printf ("Valeur retournée par la fonction fork : %d\n", (int)valeur);
  printf ("Je suis le processus : %d\n", (int)getpid());
}

