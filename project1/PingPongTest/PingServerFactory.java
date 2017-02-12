package PingPongTest;

import rmi.*;
import java.net.*;

public class PingServerFactory implements PFInterface{
	public static void main(String[] args){
    if(args.length!=1){System.out.println("Needs 1 Arguments to declare the portNo!");return;}
		PingServerFactory pf = new PingServerFactory();
    int portNo = Integer.parseInt(args[0]);
		InetSocketAddress addr = new InetSocketAddress(portNo);
		Skeleton<PFInterface> skeleton= new Skeleton<PFInterface>(PFInterface.class,pf,addr);
		try{skeleton.start();}
		catch(Throwable err){
            System.out.println("Error: " + err.getMessage()+"\nskeleton cannot start.");
            err.printStackTrace();
        }
    }

    public PingInterface makePingServer(String sIP) throws RMIException{
    	InetSocketAddress addr = new InetSocketAddress(7001);
    	PingImplement server = new PingImplement();
    	Skeleton<PingInterface> skeleton = new Skeleton<PingInterface>(PingInterface.class,server,addr);
      skeleton.start();
      PingInterface rmserver=Stub.create(PingInterface.class,skeleton,sIP);
      return rmserver;
    }
}