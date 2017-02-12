package rmi;

import java.io.*;

public class RMIObject implements Serializable{
    //private static final long serialVersionUID = 1L;
	private Object obj = null;
	private boolean excp = false;
	public RMIObject(Object o, boolean e) {this.obj=o;this.excp=e;}
	public boolean hasException(){return this.excp;}
	public Object getObject(){return this.obj;}
}