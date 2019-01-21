
USERNAME=$1
IP=$2
trap nokillme 2 15

nokillme()
{
    echo 'I will kill you, dragon'
    for i in $(ssh $USERNAME@$IP ps -eo ppid,args | grep 'dragon.sh' | awk '{print $1}')
	do 
   		if ps -p $i > /dev/null
   		then
 			sudo kill -term $i
   		fi
	done
    kill -term pid
}

GERBFILE=$(ssh $USERNAME@$IP find ~/ f -name gerb.gif)
scp $USERNAME@$IP:$GERBFILE ~/Gerbs/

