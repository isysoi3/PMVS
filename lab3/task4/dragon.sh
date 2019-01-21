trap nokillme 2 15

nokillme() 
{
    echo 'I will kill you, warrior'
}

while true
do
for i in $(ps -eo ppid,args | grep 'gerb' | awk '{print $1}')
do 
   if ps -p $i > /dev/null
   then
   kill -term $i
   fi
done
done
