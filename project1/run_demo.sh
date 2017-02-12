#############
#Config vars#
#############
PORT=7000

echo "============Build Images==========="
#docker build -t ubuntu:rmi $(pwd)

echo "============Run Containers============"
docker run -itd --name server -v $(pwd):/root/RMI javarmi bash /root/RMI/run_server.sh $PORT
SERV_IP=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' server)
docker run -itd --name client -v $(pwd):/root/RMI javarmi bash /root/RMI/run_client.sh $SERV_IP $PORT


sleep 20
echo "~~~~~~~~Server Log~~~~~~~~~"
docker logs server
echo "~~~~~~~~Client Log~~~~~~~~~"
docker logs client

docker stop server client
docker rm server client