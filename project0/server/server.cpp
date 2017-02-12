#include <iostream>
#include <stdio.h>
#include <fstream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <algorithm>
#include <unistd.h>
#include <string.h>

#define BUFSIZE 256
#define TOTALTIME 30
#define GAPTIME 3

using namespace std;

void error(char * msg){
	cout << msg << endl;
	exit(1);
}

int main(int argc, char *argv[]){
	int sockFd, connsockFd, portNum;
	socklen_t cliLen;
	char buffer[BUFSIZE];
	struct sockaddr_in server_addr, client_addr;
	string line;

	if (argc != 3){
		error("ERROR, need 2 arguments");
	}
	
	ifstream fin(argv[1]);

	/*Set up socket*/
	sockFd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockFd < 0)
		error("ERROR opening socket");
	cout << "=> Socket server has been created..." << endl;

	/*Bind socket with port*/
	bzero((char *) &server_addr, sizeof(server_addr));
	portNum = atoi(argv[2]);
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(portNum);
	server_addr.sin_addr.s_addr = INADDR_ANY;
	if (bind(sockFd, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0)
		error("ERROR on binding");
	cout << "=> Succeed in binding." << endl;

	/*Listen to the socket and accept new connection with client*/
	listen(sockFd, 5);
	cliLen = sizeof(client_addr);
	connsockFd = accept(sockFd, (struct sockaddr *) &client_addr, &cliLen);

	if (connsockFd < 0)
		error("ERROR on accept");
	cout << "=> Accepted a new connection." << endl;

	/*Receive and send*/
	for (int i = 0; i < TOTALTIME / GAPTIME; ++i){	
		bzero(buffer, BUFSIZE);
		recv(connsockFd, buffer, BUFSIZE, 0);

		if (!strcmp(buffer, "LINE\n")){
			if (!getline(fin, line)){
				fin.clear();
				fin.seekg(0, ios::beg);
				getline(fin, line);
			}
			for (int j = 0; j < line.size(); ++j)
				line[j] = toupper(line[j]);
			line += "\n";
			send(connsockFd, line.c_str(), line.length(), 0);
			cout << line;
		}
	}

	close(connsockFd);
	close(sockFd);
	return 0;

}
