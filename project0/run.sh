LOCAL_SERVER=$(pwd)'/server'
LOCAL_CLIENT=$(pwd)'/client'
LOCAL_DATA=$(pwd)'/data'

PORT=5000

echo "============Build Images==========="
docker build -t ubuntu:server $LOCAL_SERVER
docker build -t ubuntu:client $LOCAL_CLIENT
docker build -t ubuntu:data $LOCAL_DATA


echo "============Run containers==========="

docker run -d --name data ubuntu:data

docker run -itd --name server -v $LOCAL_SERVER:/root --volumes-from data ubuntu:server bash /root/comp_run.sh /data/string.txt $PORT
SERV_IP=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' server)
docker run -itd --name client -v $LOCAL_CLIENT:/root --volumes-from data ubuntu:client bash /root/comp_run.sh /data/string.txt $SERV_IP $PORT

echo "===========Client Log==========="

for i in {1..10}; do
	docker logs client
	sleep 3
done

docker stop data server client
docker rm data server client
