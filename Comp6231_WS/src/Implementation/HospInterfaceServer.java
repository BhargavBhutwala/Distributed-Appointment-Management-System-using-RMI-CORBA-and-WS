package Implementation;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Server.Montreal_Server;
import Server.Quebec_Server;
import Server.Sherbrook_Server;
import Structure.Montreal_Data;
import Structure.Quebec_Data;
import Structure.Sherbrook_Data;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService(endpointInterface = "Implementation.HospInterface")

public class HospInterfaceServer implements HospInterface
{

    String loc;
    String result;
    public Montreal_Data m_data;
    public Quebec_Data q_data;
    public Sherbrook_Data s_data;
    Logger logger;

    public HospInterfaceServer(){}


    public HospInterfaceServer(String loc)
    {
        super();
        // TODO Auto-generated constructor stub
        m_data=new Montreal_Data();
        q_data=new Quebec_Data();
        s_data=new Sherbrook_Data();
        this.loc=loc;
        setLogger("/logs/"+loc+".txt",loc);

        if(loc.equals("MTL"))
        {
            Montreal_Server ms=new Montreal_Server(this);
            Runnable r1=()->{
                ms.serverConnection(6000);
            };
            Thread t1=new Thread(r1);
            t1.start();
        }
        else if(loc.equals("QUE"))
        {
            Quebec_Server qs=new Quebec_Server(this);
            Runnable r1=()->{
                qs.serverConnection(3300);
            };
            Thread t1=new Thread(r1);
            t1.start();
        }
        else if(loc.equals("SHE"))
        {
            Sherbrook_Server ss=new Sherbrook_Server(this);
            Runnable r1=()->{
                ss.serverConnection(4200);
            };
            Thread t1=new Thread(r1);
            t1.start();
        }
        else
        {
            System.out.println("Error");
        }
    }

    private void setLogger(String location, String id)
    {
        // TODO Auto-generated method stub
        try
        {
            logger=Logger.getLogger(id);
            FileHandler fh = new FileHandler(location, true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    /**public static void main(String[] args)
     {
     // TODO Auto-generated method stub

     }**/

    @Override
    public String getBookingSchedule(String id)
    {
        // TODO Auto-generated method stub
        //String result="",msg="";
        logger.info("Appointment Booking schedule: "+id);
        StringBuilder str=new StringBuilder();
        if(id.substring(0, 3).equals("MTL"))
        {
            str.append(m_data.getBookingSchedule(id));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 3300, "scheduleAppointment"));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 4200, "scheduleAppointment"));
            logger.info("Booking schedule for: "+id+" is: "+ str);
            return str.toString().length() == 0 ? "No appointments" : str.toString().trim();
        }
        else if(id.substring(0, 3).equals("QUE"))
        {
            str.append(q_data.getBookingSchedule(id));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 6000, "scheduleAppointment"));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 4200, "scheduleAppointment"));
            logger.info("Booking schedule for: "+id+" is: "+str);
            return str.toString().length()==0?"No appointments":str.toString().trim();
        }
        else if(id.substring(0, 3).equals("SHE"))
        {
            str.append(s_data.getBookingSchedule(id));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 3300, "scheduleAppointment"));
            str.append(requestOnServer(id, "no appoint id", "no appoint type", "no capacity", 6000, "scheduleAppointment"));
            logger.info("Booking schedule for:"+id+"is: "+str);
            return str.toString().length() == 0 ? "No appointments" : str.toString().trim();
        }
        logger.info("Booking schedule for:"+id+"is: "+str);
        return str.toString().length() == 0 ? "No appointments" : str.toString().trim();
    }

    @Override
    public String bookAppoint(String id, String appointId, String appointType)
    {
        String msg="";
        // TODO Auto-generated method stub
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            StringBuilder str=new StringBuilder();
            if(!id.substring(0, 3).equals(appointId.substring(0, 3)))
            {
                if(id.substring(0, 3).equals("MTL"))
                {
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 3300, "countAppointment")+",");
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 4200, "countAppointment")+",");
                }
                else if(id.substring(0, 3).equals("QUE"))
                {
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 6000, "countAppointment")+",");
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 4200, "countAppointment")+",");
                }
                else if(id.substring(0, 3).equals("SHE"))
                {
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 3300, "countAppointment")+",");
                    str.append(requestOnServer(id, appointId, "no types", "no capacity", 6000, "countAppointment")+",");
                }
                String[] c=str.toString().trim().split(",");
                int count=0;
                for(int i=0;i<c.length;i++)
                {
                    count+=Integer.parseInt(c[i].trim());
                }
                if(count>=3)
                    result="Already booked 3 appointments outside your city";

            }
            if(id.substring(0, 3).equals(appointId.substring(0, 3)))
            {
                if(appointId.substring(0, 3).equals("MTL"))
                    msg=m_data.bookAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("QUE"))
                    msg=q_data.bookAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("SHE"))
                    msg=s_data.bookAppoint(id,appointId,appointType);
                return msg==""?"Error in booking appointment":msg;
            }
            else if(id.substring(0, 3).equals("MTL"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 6000, "bookAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else if(id.substring(0, 3).equals("QUE"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 3300, "bookAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else if(id.substring(0, 3).equals("SHE"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 4200, "bookAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else
                return "Invalid Id";

        }
        else
            return "Invalid appointment type";
    }

    @Override
    public String cancelAppoint(String id, String appointId, String appointType)
    {
        // TODO Auto-generated method stub
        String msg="";
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            if(appointId.substring(0, 3).trim().equals(id.substring(0, 3).trim()))
            {
                if(appointId.substring(0, 3).trim().equals("MTL"))
                    msg=m_data.removeAppoint(id, appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("QUE"))
                    msg=q_data.removeAppoint(id, appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("SHE"))
                    msg=s_data.removeAppoint(id, appointId, appointType);
                return msg==""?"Error in cancelling appointment":msg;
            }
            else if(id.substring(0, 3).equals("MTL"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 6000, "cancelAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else if(id.substring(0, 3).equals("QUE"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 3300, "cancelAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else if(id.substring(0, 3).equals("SHE"))
            {
                msg=requestOnServer(id, appointId, appointType, "no capacity", 4200, "cancelAppointment");
                return msg==""?"Error in booking appointment":msg;
            }
            else
                return "Invalid Id";

        }
        else
            return "Invalid appointment type";
    }

    @Override
    public String addAppoint(String id, String appointId, String appointType, String capacity)
    {
        // TODO Auto-generated method stub
        logger.info("Admin id:"+id+" has added appointment of id"+appointId+" of type:"+appointType);
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            if(appointId.substring(0, 3).trim().equals(id.substring(0, 3).trim()))
            {
                String result="";
                if(appointId.substring(0, 3).trim().equals("MTL"))
                    result=m_data.addAppoint(appointId, appointType, capacity);
                else if(appointId.substring(0, 3).trim().equals("QUE"))
                    result=q_data.addAppoint(appointId, appointType, capacity);
                else if(appointId.substring(0, 3).trim().equals("SHE"))
                    result=s_data.addAppoint(appointId, appointType, capacity);
                logger.info("Add appointment:"+result);
                return result;
            }
            else
                return "Enter valid appointment id";
        }
        else
            return "Enter valid appointment type";
    }

    @Override
    public String listAppointAvailability(String id, String appointType)
    {
        // TODO Auto-generated method stub
        logger.info("Admin id:"+id+" list all appointments of type:"+appointType);
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            String result="";
            if(id.substring(0, 3).equals("MTL"))
            {
                result=m_data.retriveAppointment(appointType);
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",3300,"listAppointment");
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",4200,"listAppointment");
                logger.info(result.equals("")?"no data found":result);
                return result.trim().equals("")?"no data found":result;
            }
            else if(id.substring(0, 3).equals("QUE"))
            {
                result=q_data.retriveAppointment(appointType);
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",6000,"listAppointment");
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",4200,"listAppointment");
                logger.info(result.equals("")?"no data found":result);
                return result.trim().equals("")?"no data found":result;
            }
            else if(id.substring(0, 3).equals("SHE"))
            {
                result=s_data.retriveAppointment(appointType);
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",6000,"listAppointment");
                result=result+requestOnServer(id,"no appoint id",appointType,"no capacity",3300,"listAppointment");
                logger.info(result.equals("")?"no data found":result);
                return result.trim().equals("")?"no data found":result;
            }
            else
                return "Id is not valid";
        }
        else
            return "Invalid appointment type";
    }

    private String requestOnServer(String id, String appointId, String appointType, String capacity, int port,
                                   String string)
    {
        // TODO Auto-generated method stub
        DatagramSocket ds=null;
        try
        {
            ds=new DatagramSocket();
            String data=id+","+appointId+","+appointType+","+capacity+","+string;
            DatagramPacket dp=new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName("localhost"), port);
            ds.send(dp);
            byte[] b=new byte[65535];
            DatagramPacket msg=new DatagramPacket(b, b.length);
            ds.receive(msg);
            String output=new String(msg.getData());
            return output;
        }
        catch (Exception e)
        {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        finally {
            ds.close();
        }
        return "";
    }

    @Override
    public String removeAppoint(String id, String appointId, String appointType)
    {
        // TODO Auto-generated method stub
        logger.info("Admin id:"+id+" has removed appointment of id"+appointId+" of type:"+appointType);
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            if(appointId.substring(0, 3).trim().equals(id.substring(0, 3).trim()))
            {
                String result="";
                if(appointId.substring(0, 3).trim().equals("MTL"))
                    result=m_data.removeAppoint(appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("QUE"))
                    result=q_data.removeAppoint(appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("SHE"))
                    result=s_data.removeAppoint(appointId, appointType);
                logger.info("Add appointment:"+result);
                return result;
            }
            else
                return "Enter valid appointment id";
        }
        else{
            return "Enter valid appointment type";
        }
    }
    public boolean checkExistingAppoint(String id, String appointId, String appointType)
    {
        boolean result=false,msg=false;
        String data="";
        if(appointType.equals("physician")||appointType.equals("surgeon")||appointType.equals("dental"))
        {
            if(appointId.substring(0, 3).trim().equals(id.substring(0, 3).trim()))
            {
                if(appointId.substring(0, 3).trim().equals("MTL"))
                    msg=m_data.getAppoint(id, appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("QUE"))
                    msg=q_data.getAppoint(id, appointId, appointType);
                else if(appointId.substring(0, 3).trim().equals("SHE"))
                    msg=s_data.getAppoint(id, appointId, appointType);
                result=msg==false?msg:true;
            }
            else if(appointId.substring(0, 3).equals("MTL"))
            {
                data=requestOnServer(id, appointId, appointType, "no capacity", 6000, "existingAppointment");
                return data.equals("No")? false:true;
            }
            else if(appointId.substring(0, 3).equals("QUE"))
            {
                data=requestOnServer(id, appointId, appointType, "no capacity", 3300, "existingAppointment");
                return data.equals("No")? false:true;
            }
            else if(appointId.substring(0, 3).equals("SHE"))
            {
                data=requestOnServer(id, appointId, appointType, "no capacity", 4200, "existingAppointment");
                return data.equals("No")? false:true;
            }
            else
                return false;
        }
        else
            return false;
        return result;

    }

    @Override
    public String swapAppoint(String id, String newAppointId, String newAppointType, String oldAppointId,
                              String oldAppointType)
    {
        // TODO Auto-generated method stub
        boolean existanceFlag=checkExistingAppoint(id, oldAppointId, oldAppointType);
        if(existanceFlag==false)
            return "---Error---";
        if(id.trim().substring(0, 3).equals(newAppointId.trim().substring(0, 3)))
        {
            boolean bookFlag=swapAppointBooking(id,newAppointId,newAppointType);
            if(bookFlag)
            {
                boolean cancelFlag=swapCancelAppoint(id,oldAppointId,oldAppointType);
                return cancelFlag ? id+" : Appointment swap was successful": id+" : Appointment swap failed";
            }
            else
                return "Unable to book new appointment";
        }
        else if(!id.trim().substring(0, 3).equals(newAppointId.trim().substring(0, 3)) && !id.trim().substring(0, 3).equals(oldAppointId.trim().substring(0, 3)))
        {
            if(newAppointId.trim().substring(6, newAppointId.length()).equals(oldAppointType.trim().substring(6, oldAppointId.length())))
            {
                boolean bookFlag=swapAppointBooking(id,newAppointId,newAppointType);
                if(bookFlag)
                {
                    boolean cancelFlag=swapCancelAppoint(id,oldAppointId,oldAppointType);
                    return cancelFlag ? id+" : Appointment swap was successful": id+" : Appointment swap failed";
                }
                else
                    return "Unable to book new appointment";
            }
            else
            {
                boolean flag=checkMaxLimit(id,newAppointId);
                if(flag)
                    return "Cannot book appointment, already reached maximum limit";
                else
                {
                    boolean bookFlag=swapAppointBooking(id,newAppointId,newAppointType);
                    if(bookFlag)
                    {
                        boolean cancelFlag=swapCancelAppoint(id,oldAppointId,oldAppointType);
                        return cancelFlag ? id+" : Appointment swap was successful": id+" : Appointment swap failed";
                    }
                    else
                        return "Unable to book new appointment";
                }
            }
        }
        return "Error";
    }

    private boolean checkMaxLimit(String id, String appointId) {
        // TODO Auto-generated method stub
        StringBuilder str=new StringBuilder();
        if(!id.substring(0, 3).trim().equals(appointId.substring(0, 3).trim()))
        {
            if(id.substring(0, 3).trim().equals("MTL"))
            {
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 3300, "countAppointment")+",");
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 4200, "countAppointment")+",");
            }
            else if(id.substring(0, 3).trim().equals("QUE"))
            {
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 6000, "countAppointment")+",");
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 4200, "countAppointment")+",");
            }
            if(id.substring(0, 3).trim().equals("SHE"))
            {
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 3300, "countAppointment")+",");
                str.append(requestOnServer(id, appointId, "No Type", "No capacity", 6000, "countAppointment")+",");
            }
            String[] split=str.toString().trim().split(",");
            int total=0;
            for (int i = 0; i < split.length; i++)
            {
                total+=Integer.parseInt(split[i].trim());
            }
            if (total>=3)
            {
                return true;
            }
        }
        return false;
    }

    private boolean swapCancelAppoint(String id, String appointId, String appointType) {
        // TODO Auto-generated method stub
        if(appointType.trim().equals("physician")||appointType.trim().equals("surgeon")||appointType.trim().equals("dental"))
        {
            if(id.substring(0, 3).trim().equals(appointId.substring(0, 3).trim()))
            {
                String msg="";
                if(appointId.substring(0, 3).equals("MTL"))
                    msg=m_data.removeAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("QUE"))
                    msg=q_data.removeAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("SHE"))
                    msg=s_data.removeAppoint(id,appointId,appointType);
                return msg.trim().isEmpty()? false:true;
            }
            else if(appointId.substring(0, 3).trim().equals("MTL"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 6000, "cancelAppointment");
                return msg.trim().isEmpty()? false:true;
            }
            else if(appointId.substring(0, 3).trim().equals("QUE"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 3300, "cancelAppointment");
                return msg.trim().isEmpty()? false:true;
            }
            else if(appointId.substring(0, 3).trim().equals("SHE"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 4200, "cancelAppointment");
                return msg.trim().isEmpty()? false:true;
            }
            else
                return false;

        }
        else
            return false;
    }

    private boolean swapAppointBooking(String id, String appointId, String appointType) {
        // TODO Auto-generated method stub
        if(appointType.trim().equals("physician")||appointType.trim().equals("surgeon")||appointType.trim().equals("dental"))
        {
            if(id.substring(0, 3).trim().equals(appointId.substring(0, 3).trim()))
            {
                String msg="";
                if(appointId.substring(0, 3).equals("MTL"))
                    msg=m_data.bookAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("QUE"))
                    msg=q_data.bookAppoint(id,appointId,appointType);
                else if(appointId.substring(0, 3).equals("SHE"))
                    msg=s_data.bookAppoint(id,appointId,appointType);
                return !msg.trim().isEmpty()&&msg.contains("has booked the appointment")? true:false;
            }
            else if(appointId.substring(0, 3).trim().equals("MTL"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 6000, "bookAppointment");
                return !msg.trim().isEmpty()&&msg.contains("has booked the appointment")? true:false;
            }
            else if(appointId.substring(0, 3).trim().equals("QUE"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 3300, "bookAppointment");
                return !msg.trim().isEmpty()&&msg.contains("has booked the appointment")? true:false;
            }
            else if(appointId.substring(0, 3).trim().equals("SHE"))
            {
                String msg="";
                msg=requestOnServer(id, appointId, appointType, "no capacity", 4200, "bookAppointment");
                return !msg.trim().isEmpty()&&msg.contains("has booked the appointment")? true:false;
            }
            else
                return false;

        }
        else
            return false;
    }

}

