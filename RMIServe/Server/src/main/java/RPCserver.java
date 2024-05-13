
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import service.impl.RemoteInterfaceImpl;
import org.apache.xmlrpc.XmlRpcException;

import java.io.IOException;

public class RPCserver {
    public static void main(String[] args) throws XmlRpcException, IOException {
        try {
            // Create a web server on port 5006
            WebServer webServer = new WebServer(5006);

            // Create an XML-RPC server
            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

            // Create a handler mapping for the remote service implementation
            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("gameService", RemoteInterfaceImpl.class);

            // Set the handler mapping on the XML-RPC server
            xmlRpcServer.setHandlerMapping(phm);

            // Start the web server
            webServer.start();

            System.out.println("RPC Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
