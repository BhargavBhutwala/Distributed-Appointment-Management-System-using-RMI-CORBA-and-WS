package dsd;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HospInterface extends Remote
{

	public String getBookingSchedule(String id) throws RemoteException;

	public String bookAppoint(String id, String appointId, String appointType) throws RemoteException;

	public String cancelAppoint(String id, String appointId, String appointType) throws RemoteException;

	public String addAppoint(String id, String appointId, String appointType, String capacity) throws RemoteException;

	public String listAppointAvailability(String id, String appointType) throws RemoteException;

	public String removeAppoint(String id, String appointId, String appointType) throws RemoteException;

}
