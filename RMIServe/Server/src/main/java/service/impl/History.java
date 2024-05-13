package service.impl;

import java.util.ArrayList;

public class History {
    public History(){
        user_defeats = 0;
        user_ties = 0;
        user_wins = 0;
        rounds = new ArrayList<>();
    }
    ArrayList<Integer> rounds;
    // -1 : LOSS
    // 0 : DRAW
    // 1 : WIN
    // [-1,0,1,1,1,1] GAME 1/ GAME 2
    int user_wins;
    int user_defeats;
    int user_ties;
}
