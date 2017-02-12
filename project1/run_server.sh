Port=$1
echo "=========Run comformance tests and unit test==========="
ls
make test

echo "=========Run PingPong Test on server==========="

java PingPongTest.PingServerFactory $Port