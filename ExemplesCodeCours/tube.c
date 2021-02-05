#include <stdio.h>
#include <sys/signal.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char *argv[]){
      int i, p[2], count;
      char buffer[6];
      /*  création du tube */
      pipe(p);
      /* On vide le buffer */
      memset (buffer, 0, 6);
      /*  création du fils (recepteur) */
      i=fork();
      if (i== 0){
      	close (p[1]);
        printf("fils pid=%d en lecture\n",getpid());
	      count = read(p[0],buffer,5);
	      printf("%d a lu %d caractères :  %s\n",getpid(),count,buffer);
	      close (p[0]);
	      printf("Fin du proessus fils\n");
      }
      else {
	       close (p[0]);
	       printf("Le père pid= %d va envoyer les données au fils...\n",getpid());
         sleep(2);
	       count = write(p[1],"Hello",5);
         printf("Le père à envoyé les données...\n");
       	/* attendre la fin du fils avant de quitter */
	     int waitFils; /* On ne traitera pas ici l'information de retour du fils... */
	     wait (&waitFils);
	     /*   fermeture du pipe  */
       close(p[1]);
	     printf ("Fin du proessus père\n");
      }
}
