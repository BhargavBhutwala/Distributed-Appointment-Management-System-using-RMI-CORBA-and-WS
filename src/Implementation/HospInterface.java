package Implementation;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.IOException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface HospInterface {
    public String addAppoint(String id, String appointId, String appointType, String capacity) throws IOException;

    public String removeAppoint(String id, String appointId, String appointType) throws IOException;

    public String listAppointAvailability(String id, String appointType) throws IOException;

    public String bookAppoint(String id, String appointId, String appointType) throws IOException;

    public String cancelAppoint(String id, String appointId, String appointType) throws IOException;

    public String getBookingSchedule(String id) throws IOException;

    public String swapAppoint(String id, String newAppointId, String newAppointType, String oldAppointId, String oldAppointType) throws IOException;

}
