package com.company;

import iaip_c4.*;

public class Main {

    public static void main(String[] args) {
        IGameLogic gameLogic = new GameLogic();
        gameLogic.initializeGame(4,4,1);
    }
}
