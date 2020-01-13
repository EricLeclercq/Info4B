#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
    int k;
    if (argc != 2) { 
	fprintf(stderr, "usage: mem <value>\n"); 
	exit(1); 
    } 
    int *i_adresse; 
    i_adresse = malloc(sizeof(int));
    printf("pid= (%d)  i_adresse designe l'adresse : %p\n", (int)getpid(), i_adresse);
    *i_adresse = atoi(argv[1]); // assigner une valeur à l'adresse designee
    for(k=1;k<=5;k++) {
	sleep(1);
	*i_adresse = *i_adresse + 1;
	printf("pid= (%d) valeur a l'adresse %p = %d\n", (int)getpid(), i_adresse, *i_adresse);
    }
    return 0;
}

