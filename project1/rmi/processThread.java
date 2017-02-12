package rmi;

import java.io.*;
import java.net.*;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

public class processThread<T> extends Thread{
	
	private Skeleton<?> skeleton=null;
	private Class<T> serverClass=null;
	private Socket client=null;
	private Method sMethod=null;

	public processThread(Skeleton<?> sk, Class<T> c, Socket skt){
		this.skeleton = sk;
		this.serverClass = c;
		this.client = skt;
	} 

	@Override
	public void run(){
		try{
			ObjectOutputStream os=new ObjectOutputStream(this.client.getOutputStream());
			os.flush();
			ObjectInputStream is=new ObjectInputStream(this.client.getInputStream());
			String methodName=(String) is.readObject();
			Class<?>[] parameterTypes=(Class []) is.readObject();
            Object[] args=(Object[]) is.readObject();
			RMIObject resObject = null;

			try{
				this.sMethod=this.serverClass.getMethod(methodName, parameterTypes);
			}
			catch(Exception e){
				RMIException rmiE = new RMIException(e.getCause());
				resObject = new RMIObject(rmiE, true);
			}

			if (this.sMethod != null){
				try{
					Object retObject=this.sMethod.invoke(this.skeleton.getServer(), args);
					resObject=new RMIObject(retObject, false);
				}
				catch(Exception e){
					resObject=new RMIObject(e.getCause(), true);
				}
			}
			os.writeObject(resObject);
			this.client.close();
		}catch(Throwable e){
			RMIException rmiEx = new RMIException(e.getCause());
			this.skeleton.service_error(rmiEx);
		}

	}

}