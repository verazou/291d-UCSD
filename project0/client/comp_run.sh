WORK_DIR=$1
DATA=$2
SERV_IP=$3
SERV_PORT=$4

g++ -o $WORK_DIR/catclient $WORK_DIR/client.cpp
$WORK_DIR/catclient $DATA $SERV_IP $SERV_PORT
