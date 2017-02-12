package PingPongTest;

import java.net.*;
import rmi.*;

public class PingClient{


	public static void main(String[] args){
		InetSocketAddress addr;
		if (args.length != 2){
			System.out.println("Needs 2 Arguments!");
			return;
		}

		String hostName = args[0];
		int portNo = Integer.parseInt(args[1]);

		addr = new InetSocketAddress(hostName, portNo);

		PFInterface serverFactory = null;
		PingInterface server = null;

		try{
			serverFactory = Stub.create(PFInterface.class, addr);
			server = serverFactory.makePingServer(hostName);
		}catch(Throwable e){
			System.out.println(e.toString());
			System.out.println("Failed connecting server.");
            return;
		}
		int cnt = 0;
		for (int i = 1; i <= 4; ++i){
			try{
				String s = server.ping(i);
				System.out.println(s);
			}catch(Exception e){
				cnt++;
				System.out.println(e.toString());
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		System.out.println((4 - cnt) + " Passed, " + cnt + " Failed");
	}
}