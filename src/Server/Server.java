package Server;

import Implementation.HospInterfaceServer;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        HospInterfaceServer montreal=new HospInterfaceServer("MTL");
        HospInterfaceServer quebec=new HospInterfaceServer("QUE");
        HospInterfaceServer sherbrook=new HospInterfaceServer("SHE");

        Endpoint montrealEp=Endpoint.publish("http://localhost:8080/Server/MTL",montreal);
        Endpoint quebecEp=Endpoint.publish("http://localhost:8080/Server/QUE",quebec);
        Endpoint sherbrookEp=Endpoint.publish("http://localhost:8080/Server/SHE",sherbrook);

        System.out.println("Montreal server started:"+montrealEp.isPublished());
        System.out.println("Quebec server started:"+quebecEp.isPublished());
        System.out.println("Sherbrook server started:"+sherbrookEp.isPublished());
    }
}
