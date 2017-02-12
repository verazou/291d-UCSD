DATA=$1
SERV_PORT=$2

g++ -o /root/catserver /root/server.cpp
/root/catserver $DATA $SERV_PORT

