#! /bin/bash
#Написать скрипт с использованием цикла for, 
#выводящий на консоль размеры и права доступа для всех файлов в 
#заданном каталоге и всех его подкаталогах 
#(имя каталога задается пользователем в качестве первого аргумента 
#командной строки).
 
for i in $(find "$1" -type f)
do
    ls -l "$i" | awk '{print $1, $5, $9}' 
done
