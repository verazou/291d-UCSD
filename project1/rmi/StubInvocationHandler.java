package rmi;

import java.lang.reflect.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class StubInvocationHandler implements InvocationHandler, Serializable{
	private InetSocketAddress addr;
	private Class remoteInterface;

	public StubInvocationHandler(InetSocketAddress addr,Class c){
		this.addr=addr;
		this.remoteInterface = c;
	}

	public InetSocketAddress getAddress(){return this.addr;}

	public Class getRemoteInterface(){return this.remoteInterface;}


	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

        RMIObject res = null;
		
		if (method.getName().equals("toString") && method.getReturnType().getName().equals("java.lang.String") && method.getParameterTypes().length == 0){
			String ans="Remote Interface: " + this.remoteInterface.getName() + "\n" + "Remote Address: " + this.addr.toString() +"\n";
		    return ans;
		}

		if (method.getName().equals("equals") && method.getReturnType().getName().equals("boolean")
			&& method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getName() == "java.lang.Object"){
			if (args.length != 1 || args[0] == null) return false;

			if (!Proxy.isProxyClass(args[0].getClass())) return false;

			StubInvocationHandler sHandler = (StubInvocationHandler)Proxy.getInvocationHandler(args[0]);
			//InvocationHandler handler = Proxy.getInvocationHandler(args[0]);
			if (!sHandler.getClass().equals(this.getClass())) return false;
			return this.addr.equals(sHandler.getAddress())&& this.remoteInterface.equals(sHandler.getRemoteInterface());
	     }

		if (method.getName().equals("hashCode") ){
            if(method.getReturnType().getName().equals("int") && method.getParameterTypes().length == 1) {
                return args[0].hashCode();
            } 
			if (method.getReturnType().getName().equals("int") && method.getParameterTypes().length == 0) {
                return this.remoteInterface.hashCode() * 227 + this.addr.hashCode() * 107;
            } 
            throw new RMIException("HashCode generation failed.");
		}

		try{
			Socket client = new Socket(this.addr.getHostName(), this.addr.getPort());
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			os.writeObject(method.getName());
			os.writeObject(method.getParameterTypes());
			os.writeObject(args);
			ObjectInputStream is = new ObjectInputStream(client.getInputStream());
			res = (RMIObject) is.readObject();
			client.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			throw new RMIException(e.getCause());
		}
		
		if (res.hasException()){
			throw (Exception)res.getObject();
		}

		return res.getObject();
	}

}

