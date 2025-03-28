# Rock-Paper-Scissors Game with RMI and RPC

This project is a distributed **Rock-Paper-Scissors** game implemented using **Remote Method Invocation (RMI)** and **Remote Procedure Call (RPC)** communication methods. It demonstrates the use of distributed systems concepts to build a scalable and interactive application.

---

## **Features**

### **Game Features**
- **Rock-Paper-Scissors Game**:
  - Play a 3-round game against the server.
  - Tracks game results (win, lose, tie) and aggregates them into game history.
- **Game History**:
  - View detailed game history, including individual round results and overall game outcomes.
  - Clear game history when exiting.

### **Communication Methods**
- **RMI (Remote Method Invocation)**:
  - Uses Java RMI for remote method invocation.
  - Handles game logic and user management on the server side.
- **RPC (Remote Procedure Call)**:
  - Uses XML-RPC for remote procedure calls.
  - Provides the same functionality as RMI but with a different communication protocol.

### **Gateway Server**
- Acts as a mediator between clients and backend services.
- Dynamically routes requests based on the chosen communication method (RMI or RPC).
- Supports multiple concurrent client connections using a thread pool.

---

## **Technologies Used**
- **Java**: Core programming language for all modules.
- **Maven**: Build and dependency management tool.
- **RMI**: For remote method invocation.
- **XML-RPC**: For remote procedure calls.
- **Multithreading**: Used in the Gateway to handle multiple client connections concurrently.

---

## **How to Run**

### **1. Start the Backend Servers**
- **RMI Server**:
  - Navigate to the RMI server directory.
  - Run the RMI server:
    ```sh
    java -cp target/classes Server
    ```
- **RPC Server**:
  - Navigate to the RPC server directory.
  - Run the RPC server:
    ```sh
    java -cp target/classes RPCserver
    ```

### **2. Start the Gateway**
- Navigate to the Gateway directory.
- Run the Gateway server:
  ```sh
  java -cp target/classes Gateway

Run the client application:
java -cp target/classes Client
Modules
1. Client
Provides the client-side application for users to interact with the game.
Allows users to choose between RMI and RPC communication methods.
Supports game actions such as playing rounds, viewing game history, and exiting.
2. Gateway
Acts as a central server that handles client requests and forwards them to the appropriate backend service (RMI or RPC).
Manages multiple client connections using multithreading.
3. RMI Server
Implements the backend logic for the RMI communication method.
Manages user sessions, game logic, and game history.
4. RPC Server
Implements the backend logic for the RPC communication method using XML-RPC.
Provides the same functionality as the RMI server but with a different communication protocol.
Contributing
Contributions are welcome! Feel free to fork this repository and submit pull requests.

License
This project is licensed under the MIT License. ```
