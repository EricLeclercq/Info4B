#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>

char message[]= "Hello world!";

void *run_du_thread(void *arg)
{
 printf("run du thread est en cours d'exécution, param = %s\n",(char *)arg);
 sleep(5);
 strcpy(message,"le thread"); // attention à la taille réservée par message[]
 pthread_exit("fin du thread");
}

int main()
{
 int res;
 pthread_t le_thread;
 void *resultat_thread;
 
 res = pthread_create(&le_thread, NULL, run_du_thread, (void *)message);
 if (res != 0)
 {
  perror("Echec à la creation du thread");
  exit(EXIT_FAILURE);
 }
 printf("En attente de la terminaison du thread...\n");
 res = pthread_join(le_thread, &resultat_thread);
 printf("Retour du thread : %s \n", (char *)resultat_thread);
 printf("Contenu de message : %s \n", message);
 exit(EXIT_SUCCESS);
}
