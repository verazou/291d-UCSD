SERVER_IMG=ubuntu:server
CLIENT_IMG=ubuntu:client
DATA_IMG=ubuntu:data

SERVER_CONT=server
CLIENT_CONT=client
DATA_CONT=data

NETWORK=my-bridge-network

LOCAL_SERVER=$(pwd)'/server'
LOCAL_CLIENT=$(pwd)'/client'
LOCAL_DATA=$(pwd)'/data'

WORK_DIR='/root'
DATA_DIR='/data/string.txt'

PORT=5000

echo "============Build Images==========="
docker build -t $SERVER_IMG $LOCAL_SERVER
docker build -t $CLIENT_IMG $LOCAL_CLIENT
docker build -t $DATA_IMG $LOCAL_DATA


echo "============Run containers==========="

docker run -d --name $DATA_CONT $DATA_IMG

docker run -itd --name $SERVER_CONT -v $LOCAL_SERVER:$WORK_DIR --volumes-from $DATA_CONT $SERVER_IMG bash $WORK_DIR/comp_run.sh $WORK_DIR $DATA_DIR $PORT
SERV_IP=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' $SERVER_CONT)
docker run -itd --name $CLIENT_CONT -v $LOCAL_CLIENT:$WORK_DIR --volumes-from $DATA_CONT $CLIENT_IMG bash $WORK_DIR/comp_run.sh $WORK_DIR $DATA_DIR $SERV_IP $PORT

echo "===========Client Log==========="

for i in {1..10}; do
	docker logs $CLIENT_CONT
	sleep 3
done

docker stop $DATA_CONT $SERVER_CONT $CLIENT_CONT
docker rm $DATA_CONT $SERVER_CONT $CLIENT_CONT
