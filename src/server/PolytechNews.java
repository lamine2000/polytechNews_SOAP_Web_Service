package server;

import service.UserManager;
import javax.xml.ws.Endpoint;

public class PolytechNews {
    public static void main(String[] args) {
        String url = "http://194.163.171.45:1122/?wsdl";
        Endpoint.publish(url, new UserManager());
        System.out.println("Webservice is available on " +url);
    }
}
