package iaip_c4;

import java.util.Random;

public class GameLogic implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    int[][] gameState;
    
    public GameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        gameState = new int[x][y];
        //TODO Write your implementation for this method
    }
	
    public Winner gameFinished() {
        for (int i = 0; i < gameState.length; i++)
        {
            for (int j = 0; j < gameState[i].length; j++)
            {
                if(CheckWin(i,j,1)) return Winner.PLAYER1;
                if(CheckWin(i,j,2)) return Winner.PLAYER2;
            }
        }
        for (int i = 0; i < gameState.length ;i++)
        {
            if(getEmptyRowOnColumn(i) != -1) return Winner.NOT_FINISHED;
        }
        return Winner.TIE;
    }


    public void insertCoin(int column, int playerID) {
        gameState[column][getEmptyRowOnColumn(column)] = playerID;
    }

    public int decideNextMove() {
        Random rdm = new Random();
        int move;
        do{
            move = rdm.nextInt(x);
        }
        while(getEmptyRowOnColumn(move) == -1);
        return move;
    }



    private int getEmptyRowOnColumn(int column)
    {
        for (int i = 0; i<gameState[column].length ;i++)
        {
            if(gameState[column][i] == 0) return i;
        }
        return -1;
    }

    private boolean CheckWin(int i, int j, int playerId)
    {
        if(j+3<=y) // row
        {
            if(gameState[i][j]==playerId && gameState[i][j+1]==playerId && gameState[i][j+2]==playerId && gameState[i][j+3]==playerId)
            {
                return true;
            }
        }
        if(i+3<=x) // column
        {
            if(gameState[i][j]==playerId && gameState[i+1][j]==playerId && gameState[i+2][j]==playerId && gameState[i+3][j]==playerId)
            {
                return true;
            }
            if(j+3<=y) // diagonal upwards
            {
                if(gameState[i][j]==playerId && gameState[i+1][j+1]==playerId && gameState[i+2][j+2]==playerId && gameState[i+3][j+3]==playerId)
                {
                    return true;
                }
            }
            if(j-3>=0) // diagonal downwards
            {
                if(gameState[i][j]==playerId && gameState[i+1][j-1]==playerId && gameState[i+2][j-2]==playerId && gameState[i+3][j-3]==playerId)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
