WORK_DIR=$1
DATA=$2
SERV_PORT=$3

g++ -o $WORK_DIR/catserver $WORK_DIR/server.cpp
$WORK_DIR/catserver $DATA $SERV_PORT

