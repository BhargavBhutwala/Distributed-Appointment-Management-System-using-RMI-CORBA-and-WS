package Server;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import Appointments.HospInterface;
import Appointments.HospInterfaceHelper;
import Implementation.HospInterfaceServer;
import Implementation.*;

public class Server 
{

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try 
		{
			ORB orb=ORB.init(args, null);
			POA rootpoa=POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			HospInterfaceServer montreal=new HospInterfaceServer("MTL");
			montreal.setORB(orb);
			org.omg.CORBA.Object refmtl = rootpoa.servant_to_reference(montreal);
			HospInterface hrefmtl = HospInterfaceHelper.narrow(refmtl);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRefmtl = NamingContextExtHelper.narrow(objRef);			
            NameComponent pathmtl[] = ncRefmtl.to_name("MTL");
            ncRefmtl.rebind(pathmtl, hrefmtl);
            
            HospInterfaceServer quebec=new HospInterfaceServer("QUE");
			quebec.setORB(orb);
			org.omg.CORBA.Object refque = rootpoa.servant_to_reference(quebec);
			HospInterface hrefque = HospInterfaceHelper.narrow(refque);
			//org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRefque = NamingContextExtHelper.narrow(objRef);			
            NameComponent pathque[] = ncRefque.to_name("QUE");
            ncRefque.rebind(pathque, hrefque);
            
            HospInterfaceServer sherbrook=new HospInterfaceServer("SHE");
			sherbrook.setORB(orb);
			org.omg.CORBA.Object refshe = rootpoa.servant_to_reference(sherbrook);
			HospInterface hrefshe = HospInterfaceHelper.narrow(refshe);
			//org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRefshe = NamingContextExtHelper.narrow(objRef);			
            NameComponent pathshe[] = ncRefshe.to_name("SHE");
            ncRefshe.rebind(pathshe, hrefshe);
            
            System.out.println("All servers started...");
            for(;;)
            {
            	orb.run();
            }
		} 
		catch (InvalidName | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName | NotFound | AdapterInactive | ServantNotActive | WrongPolicy e) 
		{
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		System.out.println("Closing Servers...");
	}

}
