package dsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client 
{

	static Registry registry;
	static HospInterface obj;
	static BufferedReader br;
	static Logger logger;
	static FileHandler fh;
	
	public static void main(String[] args) throws IOException, NotBoundException
	{
		// TODO Auto-generated method stub
		registry=LocateRegistry.getRegistry(2400);
		br=new BufferedReader(new InputStreamReader(System.in));
		while (true) 
		{
			System.out.println("Enter your Id:");
			String id=br.readLine().trim();
			obj=null;
			setLogger("C:\\Users\\Bhargav\\OneDrive\\Desktop\\CompAssignment1\\logs"+id+".txt",id);
			if(id.charAt(3)=='A')
			{
				createAdminObject(id.substring(0, 3));
				adminOption(id);
			}
			else if(id.charAt(3)=='P')
			{
				createAdminObject(id.substring(0, 3));
				patientOption(id);
			}
			if(id.equals("quit"))
				break;
		}

	}

	private static void patientOption(String id) throws IOException 
	{
		// TODO Auto-generated method stub
		System.out.println("1. Book Appointment ");
		System.out.println("2. List appointment schedule");
		System.out.println("3. Cancel appointment");
		System.out.println("Select Any above option:");
		String option = br.readLine().trim();
		if(option.equals("1"))
		{
			logger.info(id+" Book appointment");
			bookAppointment(id);
		}
		else if(option.equals("2"))
		{
			logger.info(obj.getBookingSchedule(id));
		}
		else if(option.equals("3"))
		{
			logger.info(id+" Cancel appointment for "+id);
			cancelAppointment(id);
		}
	}

	private static void adminOption(String id) throws IOException 
	{
		// TODO Auto-generated method stub
		System.out.println("1. Add Appointment ");
		System.out.println("2. Remove Appointment");
		System.out.println("3. List all Available Appointment");
		System.out.println("4. Book Appointment ");
		System.out.println("5. List appointment schedule");
		System.out.println("6. Cancel appointment");
		System.out.println("Select Any above option:");
		String option = br.readLine().trim();
		if(option.equals("1"))
		{
			logger.info(id+" Add appointment");
			addAppointment(id);
		}
		else if(option.equals("2"))
		{
			logger.info(id+" Remove appointment");
			removeAppointment(id);
		}
		else if(option.equals("3"))
		{
			logger.info(id+" List appointment availability");
			listAppointmentAvailability(id);
		}
		else if(option.equals("4"))
		{
			System.out.println("Enter patient id:");
			String patientId=br.readLine();
			logger.info(id+" Book appointment for "+patientId);
			bookAppointment(id);
		}
		else if(option.equals("5"))
		{
			System.out.println("Enter patient id:");
			String patientId=br.readLine();
			//logger.info(id+" Remove appointment");
			logger.info(obj.getBookingSchedule(patientId));
		}
		else if(option.equals("6"))
		{
			System.out.println("Enter patient id:");
			String patientId=br.readLine();
			logger.info(id+" Cancel appointment for "+patientId);
			cancelAppointment(id);
		}
	}

	private static void cancelAppointment(String id) throws IOException
	{
		// TODO Auto-generated method stub
		System.out.println("1. Appointment ID ");
		String appointId = br.readLine().trim();
		System.out.println("2. Appointment Type");
		String appointType = br.readLine().trim();
		logger.info(obj.cancelAppoint(id, appointId, appointType));
	}

	private static void bookAppointment(String id) throws IOException
	{
		// TODO Auto-generated method stub
		System.out.println("1. Appointment ID ");
		String appointId = br.readLine().trim();
		System.out.println("2. Appointment Type");
		String appointType = br.readLine().trim();
		logger.info(obj.bookAppoint(id, appointId, appointType));
	}

	private static void listAppointmentAvailability(String id) throws IOException 
	{
		// TODO Auto-generated method stub
		System.out.println("1. Appointment Type");
		String appointType = br.readLine().trim();
		logger.info(obj.listAppointAvailability(id, appointType));
	}

	private static void removeAppointment(String id) throws IOException
	{
		// TODO Auto-generated method stub
		System.out.println("1. Appointment ID ");
		String appointId = br.readLine().trim();
		System.out.println("2. Appointment Type");
		String appointType = br.readLine().trim();
		logger.info(obj.removeAppoint(id, appointId, appointType));
	}

	private static void addAppointment(String id) throws IOException 
	{
		// TODO Auto-generated method stub
		System.out.println("1. Appointment ID ");
		String appointId = br.readLine().trim();
		System.out.println("2. Appointment Type");
		String appointType = br.readLine().trim();
		System.out.println("3. Booking Capacity");
		String capacity = br.readLine().trim();
		logger.info(obj.addAppoint(id, appointId, appointType, capacity));
		
	}

	private static void createAdminObject(String server) throws AccessException, RemoteException, NotBoundException
	{
		// TODO Auto-generated method stub
		if(server.startsWith("MTL"))
		{
			obj= (HospInterface) registry.lookup("Montreal");
		}
		else if(server.startsWith("QUE"))
		{
			obj= (HospInterface) registry.lookup("Quebec");
		}
		else if(server.startsWith("SHE"))
		{
			obj=(HospInterface) registry.lookup("Sherbrook");
		}
	}

	private static void setLogger(String location, String id)
	{
		// TODO Auto-generated method stub
		try 
		{
			logger = Logger.getLogger(id);
			fh = new FileHandler(location, true);
			SimpleFormatter sf = new SimpleFormatter();
			fh.setFormatter(sf);
			logger.addHandler(fh);
		} 
		catch (Exception err)
		{
			logger.info("Couldn't Initiate Logger. Please check file permission");
		}
	}

}
