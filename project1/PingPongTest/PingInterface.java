package PingPongTest;

import rmi.*;


public interface PingInterface{
	public String ping(int id) throws RMIException;
}