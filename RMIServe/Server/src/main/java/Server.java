
import service.RemoteInterface;
import service.impl.RemoteInterfaceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

    public class Server {
        public static void main(String[] args) {


            try {
                // Create and export the RMI registry
                try {
                    LocateRegistry.createRegistry(5005);
                    System.out.println("RMI registry started on port 5005");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Create the remote object
                RemoteInterface remoteObject = new RemoteInterfaceImpl();

                // Bind the remote object to the registry
                Naming.rebind("rmi://localhost:5005/MyRemoteServer", remoteObject);

                System.out.println("Server is ready!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
