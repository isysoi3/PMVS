#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>

void wc() {
    execlp("wc", "wc", "-l", NULL);
}

void ls(char* dir) {
    execlp("ls", "ls", dir, NULL);
}


int main(int argc, char* argv[]) {
    pid_t pid_ls;
    int fd[2], nbytes;
    char dir[1000];

    if ( argc < 2 )
        strcpy(dir, "/");
    else
        strcpy(dir, argv[1]);

    pipe(fd); 

    pid_ls = fork();
    
    if(pid_ls == 0) {
        close(fd[1]);
        dup2(fd[0], STDIN_FILENO);
        close(fd[0]);
        wc();
    } else if (pid_ls > 0) {
        close(fd[0]);
        dup2(fd[1], STDOUT_FILENO);
        close(fd[1]);
        ls(dir);
    } else {
        printf ("Ошибка вызова fork, потомок не создан\n");
    }


}
