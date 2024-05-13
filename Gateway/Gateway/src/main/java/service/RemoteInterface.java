package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    String playRound(String userId, String playerChoice) throws RemoteException;

    String getUserGameHistory(String userId) throws RemoteException;

    void clearUserGameHistory(String userId) throws RemoteException;

    String calculateGameResult(String userId) throws RemoteException;

    int createUser(String userUID) throws RemoteException;
}

