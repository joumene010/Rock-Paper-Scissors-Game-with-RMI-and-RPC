import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import service.RemoteInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;
public class ClientHandler implements Runnable {
    private RemoteInterface rmiRemoteObject;
    private XmlRpcClient xmlrpcRemoteObject;  

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    private RemoteInterface initRmiRemoteObject() {
        try {
              
            return (RemoteInterface) Naming.lookup("rmi://localhost:5005/MyRemoteServer");
        } catch (Exception e) {
            throw new RuntimeException("Error initializing RMI RemoteInterface: " + e.getMessage(), e);
        }
    }



    private XmlRpcClient initXmlRpcRemoteObject() {
        try {
   
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://localhost:5006/xmlrpc")); 

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            return client;
        } catch (MalformedURLException e) {
           
            System.out.println("MalformedURLException during initializing XML-RPC client: " + e.getMessage());
            return null;
        }
    }


    @Override
    public void run() {
        String clientUID = null;

        try (
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            
        ) {
            
            String communicationMethod = reader.readLine();
            
            if ("RMI".equalsIgnoreCase(communicationMethod)) {
                this.rmiRemoteObject = initRmiRemoteObject();
                if (clientUID == null){
                    clientUID = UUID.randomUUID().toString();
                    createUser(clientUID);
                }
                handleRmiCommunication(reader, writer, clientUID);

            } else if ("RPC".equalsIgnoreCase(communicationMethod)) {
                this.xmlrpcRemoteObject = initXmlRpcRemoteObject();
                if (clientUID == null){
                    clientUID = UUID.randomUUID().toString();
                    createUserRPC(clientUID);
                }
                handleRpcCommunication(reader, writer, clientUID);

            } else {
                writer.println("Invalid communication method. Exiting.");
                writer.flush();
            }
        } catch (IOException e) {
          
            System.out.println("IOException in client handler: " + e.getMessage());
        } catch (Exception e) {
          
            System.out.println("Exception in client handler: " + e.getMessage());
        } finally {
            try {
                // Close the client socket when the client disconnects
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket);
            } catch (IOException e) {
                // Handle IOException during socket close, e.g., log it
                System.out.println("IOException while closing client socket: " + e.getMessage());
            }
        }
    }

    private void handleRmiCommunication(BufferedReader reader, PrintWriter writer , String clientUID) {
        while (true) {

            String clientChoice = readInput(reader);
            
            if ("Play".equalsIgnoreCase(clientChoice)) {
                playGame(reader, writer , clientUID);
            } else if ("View Game History".equalsIgnoreCase(clientChoice)) {
                viewHistory(writer, clientUID);
            } else if ("Exit".equalsIgnoreCase(clientChoice)) {
                 clearUserGameHistory(clientUID);
            } 
        }
    }

    private void handleRpcCommunication(BufferedReader reader, PrintWriter writer, String clientUID) {
        while (true) {

            String clientChoice = readInput(reader);
            if ("Play".equalsIgnoreCase(clientChoice)) {
                System.out.println("Playing game");
                playGameRPC(reader, writer, clientUID);
            } else if ("View Game History".equalsIgnoreCase(clientChoice)) {
                System.out.println("history");
                viewHistoryRPC(writer, clientUID);
            } else if ("Exit".equalsIgnoreCase(clientChoice)) {
                clearUserGameHistoryRPC(clientUID);
                
            } 
        }
    }
    private void createUser(String clientUID){
        try{
        rmiRemoteObject.createUser(clientUID);
        }catch (IOException e) {
            System.out.println("IOException during playGame: " + e.getMessage());
        }
    }
    
    private void createUserRPC(String clientUID){
        Object[] params = new Object[]{clientUID};
        try{
        int exec = (int)xmlrpcRemoteObject.execute("gameService.createUser", params);
        }catch (XmlRpcException e) {
            System.out.println("Exception during playGame: " + e.getMessage());
        }
    }
    private void playGame(BufferedReader reader, PrintWriter writer, String clientUID) {
        try {
            for (int round = 1; round <= 3; round++){
              
            String playerChoice = readInput(reader);
            System.out.println("works");
            String response = rmiRemoteObject.playRound(clientUID, playerChoice);
            writer.println(response);
            writer.flush();

            }
           
        } catch (IOException e) {
            System.out.println("IOException during playGame: " + e.getMessage());
        }
    }
    
    private int countLines(String history){
        int line_number = 0;
        for(int i = 0 ; i < history.length() ; i++){
            if(history.charAt(i)=='\n'){
                line_number++;
            }
        }
        return line_number;
    }

    private void viewHistory(PrintWriter writer , String clientUID) {
        try {
            String response = rmiRemoteObject.getUserGameHistory(clientUID);
            int numberOfLines = countLines(response);
            writer.println(numberOfLines);
            writer.flush();
            writer.println(response);
            writer.flush();
        } catch (IOException e) {
            System.out.println("IOException during viewHistory: " + e.getMessage());
        }
    }private void clearUserGameHistory(String clientUID) {
        try {
           rmiRemoteObject.clearUserGameHistory(clientUID);
           System.out.println("exit");
          
        } catch (IOException e) {
            System.out.println("IOException during viewHistory: " + e.getMessage());
        }
    }


    private void playGameRPC(BufferedReader reader, PrintWriter writer, String clientUID) {
        try {
            System.out.println("Playing game RPC");
            if (xmlrpcRemoteObject == null) {
                // Handle the situation when there is a problem connecting to the XML-RPC server
                writer.println("Error connecting to XML-RPC server. Exiting.");
                writer.flush();
                return;
            }
        for (int round = 1; round <= 3; round++) {
            Object[] params = new Object[]{clientUID, readInput(reader)};
            String response = (String) xmlrpcRemoteObject.execute("gameService.playRound", params);
            writer.println(response);
            writer.flush();
            
        }
        } catch (XmlRpcException e) {
            System.out.println("Exception during playGame: " + e.getMessage());
        }
    }
    
    private void viewHistoryRPC(PrintWriter writer, String clientUID) {
        try {
            
            Object[] params = new Object[]{clientUID};
            String response = (String)  xmlrpcRemoteObject.execute("gameService.getUserGameHistory", params);
            int numberOfLines = countLines(response);
            writer.println(numberOfLines);
            writer.flush();
            writer.println(response);
            writer.flush();
        } catch ( XmlRpcException e) {
            // Handle exceptions, e.g., log them
            System.out.println("Exception during viewHistory: " + e.getMessage());
        }
    }

    private void clearUserGameHistoryRPC(String clientUID) {
        try {
            Object[] params = new Object[]{clientUID};
             xmlrpcRemoteObject.execute("gameService.clearUserGameHistory", params);
            System.out.println("exit");
        } catch (XmlRpcException e) {
            // Handle exceptions, e.g., log them
            System.out.println("Exception during clearUserGameHistory: " + e.getMessage());
        }
    }

 
    private String readInput(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {

            System.out.println("IOException during readInput: " + e.getMessage());
            return ""; 
        }
    }

    private void closeClientSocket() {
        try {
            // Close the client socket when the client disconnects
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket);
        } catch (IOException e) {
            handleException("IOException while closing client socket: " + e.getMessage(), e);
        }
    }

    private void handleException(String message, Exception e) {
        // Handle exceptions, e.g., log it
        System.out.println(message);
        e.printStackTrace();
    }
}
