SERV_IP=$1
PORT=$2
make

echo "=========Run PingPong Test on client==========="
sleep 10
java PingPongTest.PingClient $SERV_IP $PORT