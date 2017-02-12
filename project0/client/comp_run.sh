DATA=$1
SERV_IP=$2
SERV_PORT=$3

g++ -o /root/catclient /root/client.cpp
/root/catclient $DATA $SERV_IP $SERV_PORT
