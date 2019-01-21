#include <sys/types.h>
#include <signal.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

void hanlde_SIGALRM () {
    printf("\nSIGALRM hanlde\n");
}

int main () {
    
    signal(SIGALRM, hanlde_SIGALRM); // handle signal
    alarm(1); // send alarm signal
    pause(); // waiting
    
    printf("\nContinued\n");

    return 0;
}

