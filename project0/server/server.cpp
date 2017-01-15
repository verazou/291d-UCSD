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
	perror(msg);
	exit(1);
}

int main(int argc, char *argv[]){
	int sockFd, newsockFd, portNum;
	socklen_t cliLen;
	char buffer[BUFSIZE];
	struct sockaddr_in serv_addr, cli_addr;
	string line;

	if (argc != 3){
		fprintf(stderr, "ERROR, need 2 arguments\n");
		exit(1);
	}
	
	ifstream fin(argv[1]);

	sockFd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockFd < 0)
		error("ERROR opening socket");
	cout << "=> Socket server has been created..." << endl;

	bzero((char *) &serv_addr, sizeof(serv_addr));
	portNum = atoi(argv[2]);
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(portNum);
	serv_addr.sin_addr.s_addr = INADDR_ANY;
	if (bind(sockFd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
		error("ERROR on binding");
	cout << "=> Succeed in binding..." << endl;
	listen(sockFd, 5);
	cliLen = sizeof(cli_addr);
	newsockFd = accept(sockFd, (struct sockaddr *) &cli_addr, &cliLen);
	cout << "=> Accepted a new connection" << endl;

	if (newsockFd < 0)
		error("ERROR on accept");
	cout << "=> Succeed in accepting..." << endl;

	for (int i = 0; i < TOTALTIME / GAPTIME; ++i){	
		bzero(buffer, BUFSIZE);
		recv(newsockFd, buffer, BUFSIZE, 0);

		if (!strcmp(buffer, "LINE\n")){
			if (!getline(fin, line)){
				fin.clear();
				fin.seekg(0, ios::beg);
				getline(fin, line);
			}
			for (int j = 0; j < line.size(); ++j)
				line[j] = toupper(line[j]);
			line += "\n";
			send(newsockFd, line.c_str(), line.length(), 0);
			cout << line;
		}

	}

	close(newsockFd);
	close(sockFd);
	return 0;

}
