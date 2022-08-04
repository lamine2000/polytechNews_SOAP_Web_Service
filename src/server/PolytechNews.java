package server;

import service.UserManager;
import javax.xml.ws.Endpoint;

public class PolytechNews {
    public static void main(String[] args) {
        String url = "http://localhost:1122/?wsdl";
        Endpoint.publish(url, new UserManager());
        System.out.println("Webservice is available on " +url);
    }
}
