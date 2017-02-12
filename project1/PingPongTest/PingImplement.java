package PingPongTest;

import rmi.RMIException;

public class PingImplement implements PingInterface{
	public String ping(int id) throws RMIException{
		return "Pong "+id;
	}
}