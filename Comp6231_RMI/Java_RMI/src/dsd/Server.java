package dsd;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server 
{

	public static void main(String[] args) throws RemoteException, AlreadyBoundException
	{
		// TODO Auto-generated method stub
		Registry registry=LocateRegistry.createRegistry(2400);
		HospInterfaceServer mtl=new HospInterfaceServer("MTL");
		HospInterfaceServer que=new HospInterfaceServer("QUE");
		HospInterfaceServer she=new HospInterfaceServer("SHE");
		
		registry.rebind("Montreal", mtl);
		registry.rebind("Quebec", que);
		registry.rebind("Sherbrook", she);
		
		System.out.println("Servers Started");
	}

}
