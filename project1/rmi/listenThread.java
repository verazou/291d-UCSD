package rmi;

import java.io.*;
import java.net.*;
import java.lang.reflect.*;

public class listenThread<T> extends Thread{

	private Skeleton<?> skeleton=null;
	private Class<T> serverClass=null;
	private ServerSocket listen_socket=null;

	public listenThread(Skeleton<?> sk, Class<T> c, ServerSocket skt){
		this.skeleton = sk;
		this.serverClass = c;
		this.listen_socket = skt;
	} 

	@Override
	public void run(){
		Exception exception=null;
		while(this.skeleton.isRunning() && !this.isInterrupted()){
			try{
				Socket client=this.listen_socket.accept();
				processThread process_thread =  new processThread(this.skeleton, this.serverClass, client);
				if (this.skeleton.isRunning()) process_thread.start();
			}
			catch(IOException ioe){
				if (!this.skeleton.isRunning() || !this.skeleton.listen_error(ioe)){
					this.skeleton.stopRunning();
					this.interrupt();
					exception=ioe;
				}
			}			
		}
		this.skeleton.stopped(exception);
	}

}