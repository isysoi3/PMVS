#include <linux/buffer_head.h>
#include <linux/fs.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/slab.h>
#include <linux/unistd.h>
#include <linux/vmalloc.h>
#include <asm/segment.h>
#include <asm/uaccess.h>
#include <linux/cred.h>
#include <linux/sched.h>
#include <linux/path.h>
#include <linux/namei.h>

#define lastTwoNumbersOfStudentCard 69
#define MAGIC_NUMBER 0x69696969


char *test_txt = NULL;
char *readme_txt = "Student <Илья Сысой>, 1623369";
char *example_txt = "Hello world";

enum Dir {
    ROOT = 0,
    BIN,
    FOO,
    TEST,
    EXAMPLE,
    README,
    BAR,
    BAZ,
    CAT
};

static struct dentry *staticfs_lookup(struct inode *dir, struct dentry *dentry, unsigned int no);

static const struct inode_operations lab4_isysoi_file_inode_operations = {
        .lookup = staticfs_lookup,
        .setattr = simple_setattr,
        .getattr = simple_getattr,
};
static const struct inode_operations lab4_isysoi_dir_inode_operations = {
        .lookup = staticfs_lookup,
};

int lab4_isysoi_iterate(struct file *file, struct dir_context *ctx) {
    struct dentry *dentry = file->f_path.dentry;
    int ino = d_inode(dentry)->i_ino;
    if (!dir_emit_dots(file, ctx))
        return 0;
    switch (ino) {
        case ROOT:
            if (ctx->pos < 4) {
                dir_emit(ctx, "bin", 3, ctx->pos++, DT_DIR);
                dir_emit(ctx, "foo", 3, ctx->pos++, DT_DIR);
            }
            break;
        case BIN:
            if (ctx->pos < 6) {
                dir_emit(ctx, "bar", 3, ctx->pos++, DT_DIR);
                dir_emit(ctx, "baz", 3, ctx->pos++, DT_DIR);
            }
            break;
        case FOO:
            if (ctx->pos < 3) {
                dir_emit(ctx, "cat", 4, ctx->pos++, DT_REG);
                dir_emit(ctx, "example", 7, ctx->pos++, DT_REG);
                dir_emit(ctx, "readme.txt", 10, ctx->pos++, DT_REG);
            }
            break;
        case BAR:
            break;
        case BAZ:
            if (ctx->pos < 3) {
                dir_emit(ctx, "test.txt", 8, ctx->pos++, DT_REG);
            }
            break;
    }
    return 0;

}

static const struct file_operations lab4_isysoi_dir_operations = {
        .iterate      = lab4_isysoi_iterate,
        .open        = dcache_dir_open,
        .release    = dcache_dir_close,
        .llseek    = dcache_dir_lseek,
        .read        = generic_read_dir,
        .fsync        = noop_fsync,
};

ssize_t lab4_isysoi_read(struct file *file, char *buf, size_t size, loff_t *offset) {
    char *str;
    struct file *f = NULL;
    struct dentry *dentry = file->f_path.dentry;
    int ino = d_inode(dentry)->i_ino;
    ssize_t len = 0;
    switch (ino) {
        case README:
            str = readme_txt;
            break;
        case TEST:
            str = test_txt;
            break;
        case EXAMPLE:
            str = example_txt;
            break;
        case CAT:

            f = filp_open("/bin/cat", O_RDONLY, 0);
            if (f == NULL) {
                printk(KERN_INFO
                "open error");
                return -ENOENT;
            }
            len = kernel_read(f, buf, size, offset);
            filp_close(f, NULL);
            return len;
        default:
            return -ENOENT;
    }
    len = strlen(str);
    if (*offset >= len)
        return 0;

    strncpy(buf, str + *offset, size);
    len = strlen(buf);
    *offset += len;
    return len;
}

ssize_t insert(char **dest, const char *source, size_t size, loff_t *offset) {
    int len = strlen(*dest);
    len = size + *offset > len ? size + *offset : len;

    char *str = vmalloc(len * sizeof(char));

    strncpy(str + *offset, source, len);
    vfree(*dest);
    *dest = str;
    *offset += len;
    return size;
}

ssize_t lab4_isysoi_write(struct file *file, const char *buf, size_t vsize, loff_t *offset) {

    struct dentry *dentry = file->f_path.dentry;
    int ino = d_inode(dentry)->i_ino;
    switch (ino) {
        case README:
            return insert(&readme_txt, buf, vsize, offset);
        case TEST:
            return insert(&test_txt, buf, vsize, offset);
        case EXAMPLE:
            return insert(&example_txt, buf, vsize, offset);
    }
    return -EINVAL;
}


const struct file_operations lab4_isysoi_file_operations = {
        .read = lab4_isysoi_read,
        .write = lab4_isysoi_write,
};

struct inode *get_inode_from_path(const char *pathname) {
    struct path path;
    kern_path("/bin/cat", LOOKUP_FOLLOW, &path);
    return path.dentry->d_inode;
}

static struct dentry *staticfs_lookup(struct inode *dir, struct dentry *dentry, unsigned int flags) {
    int ino = -1;
    umode_t mode = 0;
    umode_t type = 0;
    struct inode *inode = NULL;
    const char *name = name = dentry->d_name.name;

    switch (dir->i_ino) {
        case ROOT:
            if (!strcmp(name, "bin")) {
                ino = BIN;
                mode = 0755;
                type = S_IFDIR;
            }
            if (!strcmp(name, "foo")) {
                ino = FOO;
                mode = 0665;
                type = S_IFDIR;
            }
            break;
        case BIN:
            if (!strcmp(name, "bar")) {
                ino = BAR;
                mode = 0777;
                type = S_IFDIR;
            }
            if (!strcmp(name, "baz")) {
                ino = BAZ;
                mode = 0744;
                type = S_IFDIR;
            }

        case FOO:
            if (!strcmp(name, "test.txt")) {
                ino = TEST;
                mode = 0556;
                type = S_IFREG;
            }
            break;
        case BAZ:
            if (!strcmp(name, "example")) {
                ino = EXAMPLE;
                mode = 0200;
                type = S_IFREG;
            }
            if (!strcmp(name, "cat")) {
                ino = CAT;
                mode = 0677;
                type = S_IFREG;
                inode = get_inode_from_path("/bin/cat");
                d_add(dentry, inode);
                return NULL;
            }
            if (!strcmp(name, "readme.txt")) {
                ino = README;
                mode = 0444;
                type = S_IFREG;
            }
            break;
    }

    if (ino >= 0) {

        inode = new_inode(dir->i_sb);
        inode->i_ino = ino;
        inode_init_owner(inode, dir, mode | type);
        inode->i_size = 0;
        inode->i_blocks = 0;
        inode->i_sb = dir->i_sb;

        switch (mode & S_IFMT) {
            case S_IFREG:
                inode->i_op = &lab4_isysoi_file_inode_operations;
                inode->i_fop = &lab4_isysoi_file_operations;
                break;
            case S_IFDIR:
                inode->i_op = &lab4_isysoi_dir_inode_operations;
                inode->i_fop = &lab4_isysoi_dir_operations;
                inc_nlink(inode);
                break;
        }
    }

    d_add(dentry, inode);
    return NULL;
}


static void lab4_isysoi_put_super(struct super_block *sb) {
    printk(KERN_INFO
    "aufs super block destroyed\n");
}

static struct super_operations const lab4_isysoi_super_ops = {
        .put_super = lab4_isysoi_put_super,
};


static int aufs_fill_sb(struct super_block *sb, void *data, int silent) {
    struct inode *root = NULL;

    sb->s_blocksize = PAGE_SIZE;
    sb->s_blocksize_bits = PAGE_SHIFT;
    sb->s_op = &lab4_isysoi_super_ops;
    sb->s_magic = MAGIC_NUMBER;

    root = new_inode(sb);

    root->i_ino = 0;
    inode_init_owner(root, NULL, S_IFDIR | S_IRWXUGO);

    root->i_size = 0;
    root->i_blocks = 0;
    root->i_sb = sb;

    switch ((S_IFDIR | S_IRWXUGO) & S_IFMT) {
        case S_IFREG:
            root->i_op = &lab4_isysoi_file_inode_operations;
            root->i_fop = &lab4_isysoi_file_operations;
            break;
        case S_IFDIR:
            root->i_op = &lab4_isysoi_dir_inode_operations;
            root->i_fop = &lab4_isysoi_dir_operations;
            inc_nlink(root);
            break;
    }


    root->i_atime = root->i_mtime = root->i_ctime = CURRENT_TIME;

    sb->s_root = d_make_root(root);
    if (sb->s_root)
        return 0;

    printk(KERN_ALERT
    "failed root making\n");
    return -ENOMEM;
}


static struct dentry *lab4_isysoi_mount(struct file_system_type *type,
                                        int flags,
                                        char const *dev,
                                        void *data) {
    struct dentry *const entry = mount_bdev(type, flags, dev, data, aufs_fill_sb);
    if (IS_ERR(entry))
        printk(KERN_ALERT
    "failed mount \n");
    else
    printk(KERN_INFO
    "mounted\n");
    return entry;
}

static struct file_system_type type = {
        .owner = THIS_MODULE,
        .name = "lab4_isysoi_fs",
        .mount = lab4_isysoi_mount,
        .kill_sb = kill_block_super,
        .fs_flags = FS_USERNS_MOUNT,
};

static int lab4_isysoi_init(void) {
    int res = register_filesystem(&type);
    test_txt = vmalloc((lastTwoNumbersOfStudentCard * 20 + 40) * sizeof(char));

    int i = 0;
    while (i < lastTwoNumbersOfStudentCard) {
        strcat(test_txt, "Hello world");
        strcat(test_txt, i);
        strcat(test_txt, "\n");
        i++;
    }

    if (res == 0) {
        printk(KERN_INFO
        "inited\n");
        return 0;
    } else {
        printk(KERN_ALERT
        "failed to init module\n");
        return res;
    }
}

static void lab4_isysoi_exit(void) {
    int res = unregister_filesystem(&type);
    vfree(test_txt);

    if (res == 0) {
        printk(KERN_INFO
        "exited\n");
    } else {
        printk(KERN_ALERT
        "failed to exit module\n");
    }
}

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Ilya Sysoi");
MODULE_DESCRIPTION("Lab4: task4: mount without fuse");

module_init(lab4_isysoi_init);
module_exit(lab4_isysoi_exit);

