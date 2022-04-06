package Client;

import Implementation.HospInterface;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client {

    static BufferedReader br;
    static Logger logger;
    static FileHandler fh;
    static HospInterface obj;
    static URL url;

    public static void main(String[] args) {
        br=new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true)
            {
                System.out.println("Enter your Id:");
                String id=br.readLine().trim();
                obj=null;
                setLogger("/logs/"+id+".txt",id);
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setLogger(String loc, String id) {
        try
        {
            logger = Logger.getLogger(id);
            fh = new FileHandler(loc, true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
        }
        catch (Exception err)
        {
            logger.info("Couldn't Initiate Logger. Please check file permission");
        }
    }

    private static void patientOption(String id) throws IOException {
        System.out.println("1. Book Appointment ");
        System.out.println("2. List appointment schedule");
        System.out.println("3. Cancel appointment");
        System.out.println("4. Swap appointment");
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
        else if(option.equals("4"))
        {
            logger.info(id+" Swap appointment for "+id);
            swapAppointment(id);
        }
    }

    private static void swapAppointment(String id) throws IOException {
        System.out.println("1. New Appointment ID ");
        String newAppointId = br.readLine().trim();
        System.out.println("2. New Appointment Type");
        String newAppointType = br.readLine().trim();
        System.out.println("3. Old Appointment ID ");
        String oldAppointId = br.readLine().trim();
        System.out.println("4. Old Appointment Type");
        String oldAppointType = br.readLine().trim();
        logger.info(obj.swapAppoint(id, newAppointId, newAppointType,oldAppointId,oldAppointType));
    }

    private static void adminOption(String id) throws IOException {
        System.out.println("1. Add Appointment ");
        System.out.println("2. Remove Appointment");
        System.out.println("3. List all Available Appointment");
        System.out.println("4. Book Appointment ");
        System.out.println("5. List appointment schedule");
        System.out.println("6. Cancel appointment");
        System.out.println("7. Swap appointment");
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
            bookAppointment(patientId);
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
            cancelAppointment(patientId);
        }
        else if(option.equals("7"))
        {
            System.out.println("Enter patient id:");
            String patientId=br.readLine();
            logger.info(id+" Swap appointment for "+patientId);
            swapAppointment(patientId);
        }
    }

    private static void cancelAppointment(String id) throws IOException {
        System.out.println("1. Appointment ID ");
        String appointId = br.readLine().trim();
        System.out.println("2. Appointment Type");
        String appointType = br.readLine().trim();
        logger.info(obj.cancelAppoint(id, appointId, appointType));
    }

    private static void bookAppointment(String id) throws IOException {
        System.out.println("1. Appointment ID ");
        String appointId = br.readLine().trim();
        System.out.println("2. Appointment Type");
        String appointType = br.readLine().trim();
        logger.info(obj.bookAppoint(id, appointId, appointType));
    }

    private static void listAppointmentAvailability(String id) throws IOException {
        System.out.println("1. Appointment Type");
        String appointType = br.readLine().trim();
        logger.info(obj.listAppointAvailability(id, appointType));
    }

    private static void removeAppointment(String id) throws IOException {
        System.out.println("1. Appointment ID ");
        String appointId = br.readLine().trim();
        System.out.println("2. Appointment Type");
        String appointType = br.readLine().trim();
        logger.info(obj.removeAppoint(id, appointId, appointType));
    }

    private static void addAppointment(String id) throws IOException {
        System.out.println("1. Appointment ID ");
        String appointId = br.readLine().trim();
        System.out.println("2. Appointment Type");
        String appointType = br.readLine().trim();
        System.out.println("3. Booking Capacity");
        String capacity = br.readLine().trim();
        if (Integer.parseInt(capacity.trim())>=0)
        {
            logger.info(obj.addAppoint(id, appointId, appointType, capacity));
        }
        else {
            logger.info("Enter proper capacity");
        }
    }

    private static void createAdminObject(String server) throws MalformedURLException {
        QName qName=new QName("http://Implementation/","HospInterfaceServerService");
        if (server.trim().equals("MTL"))
            url=new URL("http://localhost:8080/Server/MTL?wsdl");
        else if (server.trim().equals("QUE"))
            url=new URL("http://localhost:8080/Server/QUE?wsdl");
        else if (server.trim().equals("SHE"))
            url=new URL("http://localhost:8080/Server/SHE?wsdl");
        Service service=Service.create(url,qName);
        obj=service.getPort(HospInterface.class);
    }
}
