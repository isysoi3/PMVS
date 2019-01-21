#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <dirent.h>
#include <errno.h>
#include <memory.h>

int getDirFilesCountR(char *dirName)
{
    int dsize = 0;
    struct dirent *dirstr;
    DIR *dirs;

    if (dirs = opendir(dirName))
    {
        while (dirstr = readdir(dirs))
        {
            char *fnm = dirstr->d_name;
            char *fullnm = (char *)alloca(strlen(dirName) + strlen(fnm) + 1);
            strcpy(fullnm, dirName);
            strcat(fullnm, "/");
            strcat(fullnm, fnm);
            struct stat fst;
            stat(fullnm, &fst);
            if (S_ISREG(fst.st_mode) && !S_ISLNK(fst.st_mode))
            {
                dsize++;
            }
            else if (S_ISDIR(fst.st_mode) && strcmp(fnm, ".") && strcmp(fnm, "..") && !S_ISLNK(fst.st_mode)) //Если
            {
                dsize = dsize + getDirFilesCountR(fullnm);
            }
        }
        closedir(dirs);
    }
    else
        printf("Couldn't open derictory - %s\n", dirName);
    return dsize;
}

void getFilesListWithSizeR(int *_i, char **_str, char *dirn, int *_fsize)
{
    DIR *dirs;
    struct dirent *dirstr;

    if (dirs = opendir(dirn))
    {

        while (dirstr = readdir(dirs))
        {
            char *fnm = dirstr->d_name;
            char *fullnm = (char *)alloca(strlen(dirn) + strlen(fnm) + 1);
            strcpy(fullnm, dirn);
            strcat(fullnm, "/");
            strcat(fullnm, fnm);
            struct stat fst;
            stat(fullnm, &fst);
            if (S_ISREG(fst.st_mode) && !S_ISLNK(fst.st_mode))
            {
                strcpy(_str[*_i], fullnm);
                *(_fsize + *_i) = fst.st_size;
                (*_i)++;
            }
            if (S_ISDIR(fst.st_mode) && strcmp(fnm, ".") && strcmp(fnm, "..") && !S_ISLNK(fst.st_mode))
            {
                getFilesListWithSizeR(_i, _str, fullnm, _fsize);
            }
        }
        closedir(dirs);
    }
    else
        printf("Couldn't open derictory - %s\n", dirn);
}

int compareFilesInForkWithSameSize(char *fnm1, char *fnm2, int *_fs)
{
    int ppid = fork();
    if (ppid == 0)
    {
        int t = 0;
        FILE *fd1 = fopen(fnm1, "r");
        FILE *fd2 = fopen(fnm2, "r");
        while (abs(fgetc(fd1)) == fgetc(fd2))
            t++;
        printf("\n********* PID: %d *********\n1> %s\n2> %s\n------Result: %d of %d bytes match------\n\n", getpid(), fnm1, fnm2, t, *_fs);
        fclose(fd1);
        fclose(fd2);
    }
    return ppid;
}

int main(int argc, char *argv[])
{
    if (argc > 4)
    {
        puts("There are too much args");
        return -1;
    }
    if (argc < 4)
    {
        puts("There are too less args");
        return -1;
    }

    char *dir1 = argv[1];
    char *dir2 = argv[2];
    int N;
    if (!sscanf(argv[3], "%d", &N))
    {
        puts("Not a number");
        return -1;
    }

    char **fileNamesList1;
    char **fileNamesList2;
    int *fileSizes1;
    int *fileSizes2;
    int dirFilesCount1 = getDirFilesCountR(dir1);
    int dirFilesCount2 = getDirFilesCountR(dir2);
    int pd = 1, n = 0;

    printf("Total files: %d\n", dirFilesCount1 + dirFilesCount2);

    fileNamesList1 = malloc(dirFilesCount1 * (sizeof(char *)));
    fileNamesList2 = malloc(dirFilesCount2 * (sizeof(char *)));
    fileSizes1 = malloc(dirFilesCount1 * (sizeof(int)));
    fileSizes2 = malloc(dirFilesCount2 * (sizeof(int)));

    for (int index = 0; index < dirFilesCount1; index++)
        *(fileNamesList1 + index) = malloc(512 * sizeof(char));

    for (int index = 0; index < dirFilesCount2; index++)
        *(fileNamesList2 + index) = malloc(512 * sizeof(char));

    dirFilesCount1 = 0;
    dirFilesCount2 = 0;
    getFilesListWithSizeR(&dirFilesCount1, fileNamesList1, dir1, fileSizes1);
    getFilesListWithSizeR(&dirFilesCount2, fileNamesList2, dir2, fileSizes2);
    printf("Total files read: %d\n", dirFilesCount1 + dirFilesCount2);

    for (int index1 = 0; index1 < dirFilesCount1; index1++)
        for (int index2 = 0; index2 < dirFilesCount2; index2++)
            if (pd > 0 && (*(fileSizes1 + index1) == *(fileSizes2 + index2))) {
                if (n++ > N)
                    wait(NULL);
                pd = compareFilesInForkWithSameSize(*(fileNamesList1 + index1), *(fileNamesList2 + index2), fileSizes1 + index1);
            }
        
    

    if (pd > 0)
    {
        while (wait(NULL) > 0);
        char *err = strerror(errno);
        printf("\nErrno result: %s \n", err);
    }

    for (int index = 0; index < dirFilesCount1; index++)
        free(fileNamesList1[index]);

    for (int index = 0; index < dirFilesCount2; index++)
        free(fileNamesList2[index]);

    free(fileNamesList1);
    free(fileNamesList2);
    free(fileSizes1);
    free(fileSizes2);

    return 0;
}
