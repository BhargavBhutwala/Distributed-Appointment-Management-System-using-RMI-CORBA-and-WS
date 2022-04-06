package dsd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Montreal_Server 
{

	HospInterfaceServer obj=null;
	Logger logger;
	public Montreal_Server(HospInterfaceServer aThis)
	{
		// TODO Auto-generated constructor stub
		this.obj=aThis;
	}
	public void serverConnection(int port)
	{
		setLogger("\logs\\MTL.txt","MTL");
		logger.info("Montreal server started...");
		DatagramSocket ds=null;
		while (true)
		{
			try 
			{
				ds=new DatagramSocket(port);
				byte[] receive = new byte[65535];
				DatagramPacket dp = new DatagramPacket(receive, receive.length);
				ds.receive(dp);
				byte[] data = dp.getData();
				String[] receiveData = new String(data).split(",");
				logger.info("Receive Data : " + new String(data));
				logger.info("Operation Performed " + receiveData[receiveData.length - 1].trim());
				if(receiveData[receiveData.length-1].trim().equals("listAppointment"))
				{
					String str=obj.m_data.retriveAppointment(receiveData[2]);
					logger.info("Message to patient:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("addAppointment"))
				{
					String str=obj.m_data.addAppoint(receiveData[1], receiveData[2], receiveData[3]);
					logger.info("Message to admin:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("bookAppointment"))
				{
					String str=obj.m_data.bookAppoint(receiveData[0], receiveData[1], receiveData[2]);
					logger.info("Message to patient:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("cancelAppointment"))
				{
					String str=obj.m_data.removeAppoint(receiveData[0], receiveData[1], receiveData[2]);
					logger.info("Message to patient:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("scheduleAppointment"))
				{
					String str=obj.m_data.getBookingSchedule(receiveData[0]);
					logger.info("Message to patient:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("countAppointment"))
				{
					String str=obj.m_data.getBookingCount(receiveData[0], receiveData[1]);
					logger.info("Message to patient:"+str);
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else if(receiveData[receiveData.length-1].trim().equals("existingAppointment"))
				{
					boolean b=obj.m_data.getAppoint(receiveData[0], receiveData[1], receiveData[2]);
					logger.info("Message to patient:"+b);
					String str=b==false?"No":"Yes";
					DatagramPacket dp2=new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
					ds.send(dp2);
				}
				else
					logger.info("Error");
				receive=new byte[65535];
				data=new byte[65535];
				
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
			finally
			{
				ds.close();
			}
		}
	}
	private void setLogger(String location, String id) 
	{
		// TODO Auto-generated method stub
		try 
		{
			logger = Logger.getLogger(id);
			FileHandler fh = new FileHandler(location, true);
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
