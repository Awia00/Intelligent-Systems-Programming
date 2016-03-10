package iaip_c4;

public class GameLogic9 implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    
    public GameLogic9() {
        //TODO Write your implementation for this method
    }

    /**
     * Creates a new empty game board of the specified dimensions
     * and indicates the ID of the player.
     * This method will be called from the main function.
     * @param columns The number of columns in the game board
     * @param rows The number of rows in the game board
     * @param playerID 1 = blue (player1), 2 = red (player2)
     */
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        //TODO Write your implementation for this method
    }

    /**
     * Checks if any of the two players have 4-in-a-row.
     * @return Winner enum
     */
    public Winner gameFinished() {
        //TODO Write your implementation for this method
        return Winner.NOT_FINISHED;
    }

    /**
     * Notifies that a token/coin is put in the specified column of
     * the game board.
     * @param column The column where the coin is inserted.
     * @param playerID The ID of the current player.
     */
    public void insertCoin(int column, int playerID) {
        //TODO Write your implementation for this method	
    }

    /**
     * Calculates the next move  This is where you should
     * implement/call your heuristic evaluation functions etc.
     */
    public int decideNextMove() {
        //TODO Write your implementation for this method
        return 0;
    }

}
