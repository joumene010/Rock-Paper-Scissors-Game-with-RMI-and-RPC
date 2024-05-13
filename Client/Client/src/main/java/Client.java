import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 5001); 
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
   
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Choose communication method: RMI or RPC?");
            String communicationMethod = userInputReader.readLine();
            writer.println(communicationMethod);
            writer.flush();

    
            if ("RMI".equalsIgnoreCase(communicationMethod)||"RPC".equalsIgnoreCase(communicationMethod)) {
         while (true) {
            System.out.println("Choose an option: Play, View Game History, Exit");
            String clientChoice = userInputReader.readLine();
            writer.println(clientChoice);
            writer.flush();
        
            if ("Play".equalsIgnoreCase(clientChoice)) {

                for (int round = 1; round <= 3; round++) {
                    System.out.println("Enter your choice for Round " + round + ": Pierre, Papier, Ciseaux");
                    String clientChoiceRound = userInputReader.readLine();
                    writer.println(clientChoiceRound);
                    writer.flush();
                    
                    String serverResponse = reader.readLine();
                    System.out.println(serverResponse);
                    if(round==3){
                    serverResponse = reader.readLine();
                    System.out.println(serverResponse);
                    }

                }
            } else if("View Game History".equalsIgnoreCase(clientChoice)) {
               
                int numberOfLines = Integer.parseInt(reader.readLine());
                for(int i = 0 ; i <= numberOfLines ; i++) {
                    System.out.println(reader.readLine());
                }
            } 
        
           
           
        }
            } else {
                System.out.println("Invalid communication method. Exiting.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }    
