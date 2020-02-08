#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

void *run(void *i) {
    int a = *((int *) i);
    printf("jai recu %d \n",a);
}

int main() {
    pthread_t thread;
    int valeur = 5;
    int *i ; 
    i=&valeur;
    pthread_create(&thread, 0, run, (void *) i);
    sleep(2);
}
