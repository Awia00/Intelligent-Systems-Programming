package iaip_c4;

import java.util.ArrayList;

public class GameLogic9 implements IGameLogic {
    private int columns = 0;
    private int rows = 0;
    private int playerID, opponentID;

    private int[][] gameBoard;
    
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
    public void initializeGame(int columns, int rows, int playerID) {
        this.columns = columns;
        this.rows = rows;
        this.playerID = playerID;
        this.opponentID = playerID == 1 ? 2 : 1;

        gameBoard = new int[columns][rows];
    }

    /**
     * Checks if any of the two players have 4-in-a-row.
     * @return Winner enum
     */
    public Winner gameFinished()
    {
        for (int column = 0; column<columns; column++)
        {
            for(int row = 0; row < rows; row++)
            {
                switch (playerWonOn(column,row))
                {
                    case 1:
                        return Winner.PLAYER1;
                    case 2:
                        return Winner.PLAYER2;
                    default:
                        break;
                }
            }
        }

        if(availableActions().isEmpty())
        {
            return  Winner.TIE;
        }

        return Winner.NOT_FINISHED;
    }

    private int playerWonOn(int column, int row)
    {
        int playerIDOnPos = gameBoard[column][row];

        if(playerIDOnPos == 0)
        {
            return 0;
        }

        // Horizontal check
        if(column + 3 < columns)
        {
            if(gameBoard[column+1][row]==playerIDOnPos &&
                    gameBoard[column+2][row]==playerIDOnPos &&
                    gameBoard[column+3][row]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        // Vertical check
        if(row+3 < rows)
        {
            if(gameBoard[column][row+1]==playerIDOnPos &&
                    gameBoard[column][row+2]==playerIDOnPos &&
                    gameBoard[column][row+3]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        // Diagonal check
        if(row+3 < rows && column + 3 < columns)
        {
            if(gameBoard[column+1][row+1]==playerIDOnPos &&
                    gameBoard[column+2][row+2]==playerIDOnPos &&
                    gameBoard[column+3][row+3]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        if(row-3 >= 0 && column + 3 < columns)
        {
            if(gameBoard[column+1][row-1]==playerIDOnPos &&
                    gameBoard[column+2][row-2]==playerIDOnPos &&
                    gameBoard[column+3][row-3]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        return 0;
    }

    /**
     * Notifies that a token/coin is put in the specified column of
     * the game board.
     * @param column The column where the coin is inserted.
     * @param playerID The ID of the current player.
     */
    public void insertCoin(int column, int playerID)
    {
        int row = availableRowInColumn(column);
        gameBoard[column][row] = playerID;
    }

    private int availableRowInColumn(int column)
    {
        for(int i = 0; i < rows; i++)
        {
            if(gameBoard[column][i] == 0)
            {
                return i;
            }
        }
        return -1;
    }

    private ArrayList<Integer> availableActions()
    {
        int topRow = rows-1;
        ArrayList<Integer> returnList = new ArrayList<>();
        for(int i = 0; i < columns; i++)
        {
            if(gameBoard[i][topRow] == 0)
            {
                returnList.add(i);
            }
        }
        return returnList;
    }

    /**
     * Calculates the next move  This is where you should
     * implement/call your heuristic evaluation functions etc.
     */
    public int decideNextMove()
    {
        ArrayList<Integer> availableActions = availableActions();

        return availableActions.get(0);
    }

    



}
