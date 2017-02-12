package PingPongTest;

import rmi.RMIException;

public interface PFInterface{
	public PingInterface makePingServer(String sIP) throws RMIException;
}