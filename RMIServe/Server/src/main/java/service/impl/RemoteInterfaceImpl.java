package service.impl;

import service.RemoteInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RemoteInterfaceImpl extends UnicastRemoteObject implements RemoteInterface {
    private static HashMap<String,History> usersHistory = new HashMap<>();
    public RemoteInterfaceImpl() throws RemoteException {
    }
    private String chooseRandomChoice(){
        int randomChoice = (int)(Math.random()*3);
        String allChoices[] = {"pierre","papier","ciseaux"};
        return allChoices[randomChoice];

    }
    public int createUser(String userUID){
        History history = new History();
        usersHistory.put(userUID,history);
        return 1;
    }
    public String playRound(String userId, String playerChoice){
        System.out.println("heloo");
        // GENERATE SERVER CHOICE
        String serverChoice = this.chooseRandomChoice();
        System.out.println(serverChoice);
        System.out.println(playerChoice);
        String lowerPlayerChoice = playerChoice.toLowerCase();
        History currentUserHistory = usersHistory.get(userId);
        ArrayList<Integer> currentUserRounds = currentUserHistory.rounds; 
        // TIE CASE
        String roundResult;
        if (lowerPlayerChoice.equals(serverChoice)){
            currentUserRounds.add(0); // NEUTRAL SCORE => TIE
            roundResult = "TIE";
            System.out.println("enter tie ");
        }
        // USER LOSES CASE
        else if (lowerPlayerChoice.equals("pierre") && serverChoice.equals("papier")
                || lowerPlayerChoice.equals("ciseaux") && serverChoice.equals("pierre")
                || lowerPlayerChoice.equals("papier") && serverChoice.equals("ciseaux")
        )
        {    System.out.println("enter defeat ");
            currentUserRounds.add(-1);
            roundResult = "DEFEAT";
        }
        // USER WINS CASE
        else
        {
            System.out.println("enter win ");
            currentUserRounds.add(1);
            roundResult = "WIN";
        } 
        int totalRoundNumber = currentUserRounds.size();
        int roundNumber = totalRoundNumber % 3;
        // IF roundNumber == 0 => ROUND 1 ... == 1 => ROUND 2 else ROUND 3
        if (totalRoundNumber >= 3 && roundNumber == 0){
            int gameState = currentUserRounds.get(totalRoundNumber - 1) 
            + currentUserRounds.get(totalRoundNumber - 2)
            + currentUserRounds.get(totalRoundNumber - 3);
            if (gameState > 0){
                currentUserHistory.user_wins++;   
                roundResult+="\nUSER WINS GAME";
            }
            else if (gameState == 0){
                currentUserHistory.user_ties++;
                roundResult+="\nGAME TIE";
            }
            else
            {   roundResult+="\nUSER LOSES GAME";
                currentUserHistory.user_defeats++;   
            }
            }
        usersHistory.put(userId,currentUserHistory);
        System.out.println(roundResult);
        return roundResult;
    }   

    public String getUserGameHistory(String userId){
        History currentUserHistory = usersHistory.get(userId);
        ArrayList<Integer> currentUserRounds = currentUserHistory.rounds;
        int currentUserWins = currentUserHistory.user_wins;
        int currentUserDefeats = currentUserHistory.user_defeats;
        int currentUserTies = currentUserHistory.user_ties;
        String possibleOutcomes[] = {"DEFEAT","TIE","WIN"};
        String userGameHistory = 
        "Total Games :"+
        "\nWins:" + currentUserWins + 
        "\nTies:"+ currentUserTies + 
        "\nDefeats:" + currentUserDefeats;
        for(int round = 0 ; round<currentUserRounds.size();round++){
            if((round+1) % 3 == 0 && currentUserRounds.size() > 0){
                String gameStatus;
                int gameState = currentUserRounds.get(round - 2) 
                + currentUserRounds.get(round - 1) 
                + currentUserRounds.get(round);
                if (gameState > 0 )
                    gameStatus="WIN";
                else if (gameState < 0)
                    gameStatus="DEFEAT";
                else
                    gameStatus="TIE";
                
                userGameHistory+="\nGame " + (round+1) / 3 + ":" + gameStatus;
                userGameHistory+="\nRound 1 " + possibleOutcomes[currentUserRounds.get(round - 2) + 1];
                userGameHistory+="\nRound 2 " + possibleOutcomes[currentUserRounds.get(round - 1) + 1];
                userGameHistory+="\nRound 3 " + possibleOutcomes[currentUserRounds.get(round) + 1];
            }

        }
        return userGameHistory;

    }

    public void clearUserGameHistory(String userId){
        System.err.println("exit");
        usersHistory.remove(userId);
    }
}