#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <algorithm>
#include <fstream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <bitset>

#define TOTALTIME 30 //seconds
#define GAPTIME 3	//seconds
#define BUFSIZE 1024

using namespace std;

void error(char *message){
	cout << message << endl;
	exit(1);
}

int main(int argc, char *argv[]){
	int client;
	int portNum;
	bool isExit = false;
	char recvbuf[BUFSIZE];
	char sendbuf[] = "LINE\n";
	struct sockaddr_in server_addr;
	struct hostent *server;
	string line;
	if (argc != 4){
		error("ERROR, you need 4 arguments.");
	}

	ifstream fin(argv[1]);	
	portNum = atoi(argv[3]);

	/*Set up socket*/
	client = socket(AF_INET, SOCK_STREAM, 0);
	if (client < 0){
		error("Error establishing socket.");
	}
	cout << "=> Socket client has been created." << endl;

	/*Get server IP*/
	server = gethostbyname(argv[2]);	
	if (server == NULL)
		error("ERROR, no such host\n");
	cout << "=> Successfully get host by name." << endl;

	/*Get server IP and port*/
	bzero((char *) &server_addr, sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	bcopy((char *)server->h_addr, (char *)&server_addr.sin_addr.s_addr, server->h_length);
	server_addr.sin_port = htons(portNum);
	
	/*Connect with Server*/
	if (connect(client, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0)
		error("ERROR connecting server.");
	cout << "=> Connection to the server port number: " << portNum << endl;

	/*Send and receive*/
	for (int i = 0; i < TOTALTIME / GAPTIME; ++i){
		cout << "=> Send the " << i + 1 << "th request" << endl;	
		/*Send LINE\n to server*/
		send(client, sendbuf, strlen(sendbuf), 0);
		cout << "Sended\n";
		bzero(recvbuf, sizeof(recvbuf));
		recv(client, recvbuf, BUFSIZE, 0);

		bool found = false;
		fin.clear();
		fin.seekg(0, ios::beg);

		/*Find if it is in the file*/
		while(getline(fin, line)){
			for (int j = 0; j < line.size(); ++j){
				line[j] = toupper(line[j]);
			}
			line += "\n";
			if (!strcmp(line.c_str(), recvbuf)){
				found = true;
				cout << "OK" << endl;
				break;
			}
		}
		if (!found){
			cout << "MISSING" << endl;
		}
		sleep(GAPTIME);	
	}

	cout << "=> Connection terminated." << endl;

	close(client);
	return 0;

}

