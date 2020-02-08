#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main (void)
{
  
  int valeur, valeur1 ;
  printf ("  1 - Je suis le processus pere num = %d \n",
	  (int)getpid() );
  valeur = fork();
  printf ("  2 - retour fork : %d - processus num= %d - num pere=%d \n",
	  valeur, (int)getpid(), (int)getppid() );
  valeur1 = fork();
  printf ("  3 - retour fork : %d - processus num= %d - num pere=%d \n",
	  valeur1, (int)getpid(), (int)getppid() );
 sleep(10);
}
