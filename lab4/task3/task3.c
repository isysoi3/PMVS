#define FUSE_USE_VERSION 30

#include <fuse.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#define lastTwoNumbersOfStudentCard 69

const char* readme_txt = "Student <Илья Сысой>, 1623369";
const char* example_txt = "Hello world";
char test_txt[lastTwoNumbersOfStudentCard][20];
char mount_path[256] = "";
mode_t *mkdired_mode;
const int user_id = 0;
static int rmfoo = 0, rmbar = 0;

static int task3_open(const char *path, struct fuse_file_info *fi) {
	return 0;
}

static int task3_read(const char *path, char *buf, size_t size, off_t offset, struct fuse_file_info *fi) {
    size_t len;
    (void) fi;
    char *fileBuffer;
    char tmp[lastTwoNumbersOfStudentCard*20 + 40];

    if (strcmp(path, "/bin/baz/cat") == 0) {
        struct stat cat_stat;
        stat("/baz/cat", &cat_stat);
        len = cat_stat.st_size;

        FILE *f;
        unsigned char buffer[len];
        f = fopen("bin/cat", "r");
        fread(buffer, len, 1, f);
        fileBuffer = buffer;        
    }
    else if (strcmp(path, "/bin/baz/readme.txt") == 0) {
        len = strlen(readme_txt);
        fileBuffer = readme_txt;
    }
    else if (strcmp(path, "/bin/baz/example") == 0) {
        len = strlen(example_txt);
        fileBuffer = example_txt;
    }
    else if (strcmp(path, "/foo/test.txt") == 0) {
        strcpy(tmp, "");
        for(int i=0; i<lastTwoNumbersOfStudentCard; i++) {
            len += strlen(test_txt[i]);
            strcat(tmp, test_txt[i]);
            strcat(tmp, "\n");
        }
        fileBuffer = tmp;
    }
    else{
        return -ENOENT;
    }

    if (offset < len){
        if (offset + size > len){
            size = len-offset;
        }
        memcpy(buf, fileBuffer+offset, size);
        return size;
    }
    else{
        return 0;
    }
}

static int task3_mkdir(const char *path, mode_t mode) {
    if (mkdir(path, mode == -1))
        return -errno;
    return 0;
}

static int task3_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t offset, struct fuse_file_info *fi) {
    (void) offset;
    (void) fi;
    
    if (strcmp(path, "/") == 0) {
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        filler(buf, "foo", NULL, 0);
        filler(buf, "bin", NULL, 0);
        if (strcmp(mount_path, "/mnt/fuse/") != 0){
            filler(buf, mount_path+1, NULL, 0);
        }
        return 0;
    }
    if (strcmp(path, "/bin") == 0) {
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        filler(buf, "bar", NULL, 0);
        filler(buf, "baz", NULL, 0);
        return 0;
    }
    if (strcmp(path, "/bin/bar") == 0 && !rmbar) {
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        return 0;
    }
    if (strcmp(path, "/bin/baz") == 0) {
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        filler(buf, "cat", NULL, 0);
        filler(buf, "example", NULL, 0);
        filler(buf, "readme.txt", NULL, 0);
        return 0;
    }
    if (strcmp(path, "/foo") == 0 && !rmfoo) {
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        filler(buf, "test.txt", NULL, 0);
        return 0;
    }
    
    return -ENOENT;
}

static int task3_getattr(const char *path, struct stat *stbuf) {
    int res = 0;
    int size = 0;

    stbuf->st_uid = user_id;
    memset(stbuf, 0, sizeof(struct stat));

    if (strcmp(path, "/") == 0) {
        stbuf->st_mode = S_IFDIR | 0755;
        stbuf->st_nlink = 2;
    }
    else if (strcmp(path, "/bin") == 0){
        stbuf->st_mode = S_IFDIR | 0755;
        stbuf->st_nlink = 3+1;
    }
    else if (strcmp(path, "/bin/bar") == 0 && !rmbar){
        stbuf->st_mode = S_IFDIR | 0777;
        stbuf->st_nlink = 0;
    }
     else if (strcmp(path, "/bin/baz") == 0){
        stbuf->st_mode = S_IFDIR | 0744;
        stbuf->st_nlink = 3;
    }
    else if (strcmp(path, "/bin/baz/cat") == 0){
        stbuf->st_mode = S_IFREG | 0677;
        stbuf->st_nlink = 1;

        struct stat buffer;
        stat("/baz/cat", &buffer);
        stbuf->st_size = buffer.st_size;
    }
    else if (strcmp(path, "/bin/baz/example") == 0){
        stbuf->st_mode = S_IFREG | 0200;
        stbuf->st_nlink = 1;

        stbuf->st_size = strlen(example_txt);
    }
    else if (strcmp(path, "/bin/baz/readme.txt") == 0){
        stbuf->st_mode = S_IFREG | 0444;
        stbuf->st_nlink = 1;

        stbuf->st_size = strlen(readme_txt);
    }
    else if (strcmp(path, "/foo") == 0 && !rmfoo){
        stbuf->st_mode = S_IFDIR | 0665;
        stbuf->st_nlink = 2;
    }
    else if (strcmp(path, "/foo/test.txt") == 0){
        stbuf->st_mode = S_IFREG | 0556;
        stbuf->st_nlink = 1;

        for(int i=0; i<lastTwoNumbersOfStudentCard; i++) {
            size += strlen(test_txt[i]);
        }
        stbuf->st_size = size;
    }
    else if (strcmp(path, mount_path) == 0){
        stbuf->st_mode = mkdired_mode;
        stbuf->st_nlink = 2;
    }
    else{
        res = -ENOENT;
    }
            
    return res;
}

static int task3_rmdir(const char *path) {
	if (!strcmp(path, "/"))	
		return -ENOTEMPTY;
	else if (!strcmp(path,"/bin"))
		return -ENOTEMPTY;
	else if (!strcmp(path, "/bin/bar") && !rmbar) {
		rmbar = 1;
        return 0;
	} else if (!strcmp(path, "/bin/baz"))
		return -ENOTEMPTY;	
	else if (!strcmp(path, "/foo") && !rmfoo){
		rmfoo = 1;
		return 0;
	}
	else
		return -ENOENT;	
}

int main(int argc, char *argv[]) {
    if(argc < 2) {
        printf("Mountpoint is empty, pass it as arg.\n");
        return 0;
    }
    strcpy(mount_path, argv[1]);

    char buffer[3];
    for (int i=0; i<lastTwoNumbersOfStudentCard; i++){
        sprintf(buffer, "%d", i);
        strcpy(test_txt[i], "");
        strcat(test_txt[i], "Hello world ");
        strcat(test_txt[i], buffer);
    }

    struct fuse_operations operations = {
            .mkdir          = task3_mkdir,
            .rmdir	        = task3_rmdir,
            .open           = task3_open,
            .read           = task3_read,
            .getattr        = task3_getattr,
            .readdir        = task3_readdir,
    };

    return fuse_main(argc, argv, &operations, NULL);
}
