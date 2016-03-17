package iaip_c4;

import java.util.ArrayList;
import java.util.Comparator;

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
        return gameFinished(gameBoard);
    }

    private int playerWonOn(int column, int row, int[][] state)
    {
        int playerIDOnPos = state[column][row];

        if(playerIDOnPos == 0)
        {
            return 0;
        }

        // Horizontal check
        if(column + 3 < columns)
        {
            if(state[column+1][row]==playerIDOnPos &&
                    state[column+2][row]==playerIDOnPos &&
                    state[column+3][row]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        // Vertical check
        if(row+3 < rows)
        {
            if(state[column][row+1]==playerIDOnPos &&
                    state[column][row+2]==playerIDOnPos &&
                    state[column][row+3]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        // Diagonal check
        if(row+3 < rows && column + 3 < columns)
        {
            if(state[column+1][row+1]==playerIDOnPos &&
                    state[column+2][row+2]==playerIDOnPos &&
                    state[column+3][row+3]==playerIDOnPos)
            {
                return playerIDOnPos;
            }
        }

        if(row-3 >= 0 && column + 3 < columns)
        {
            if(state[column+1][row-1]==playerIDOnPos &&
                    state[column+2][row-2]==playerIDOnPos &&
                    state[column+3][row-3]==playerIDOnPos)
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

    private int availableRowInColumn(int column, int[][] state)
    {
        for(int i = 0; i < rows; i++)
        {
            if(state[column][i] == 0)
            {
                return i;
            }
        }
        return -1;
    }


    private int availableRowInColumn(int column)
    {
        return availableRowInColumn(column, gameBoard);
    }

    private ArrayList<Integer> availableActions(int[][] state)
    {
        int topRow = rows-1;
        ArrayList<Integer> returnList = new ArrayList<>();
        for(int i = 0; i < columns; i++)
        {
            if(state[i][topRow] == 0)
            {
                returnList.add(i);
            }
        }
        return returnList;
    }

    private static int[][] getCopyState(int[][] oldState)
    {
        int [][] state = new int[oldState.length][];
        for(int i = 0; i < oldState.length; i++)
            state[i] = oldState[i].clone();
        return state;
    }

    /**
     * Calculates the next move  This is where you should
     * implement/call your heuristic evaluation functions etc.
     */
    public int decideNextMove()
    {
        System.out.println("Player " + playerID + " started calculating");
        ArrayList<Integer> availableActions = availableActions(gameBoard);



        long startTime = System.currentTimeMillis();

        int currentBest = Integer.MIN_VALUE;
        int actionToDecide = availableActions.get(0);
        int depth = 1;
        while (System.currentTimeMillis() - startTime < 10000) {
            int alpha = Integer.MIN_VALUE;
            for (int action : availableActions) {
                int utility = minValue(result(gameBoard, action, playerID), depth, alpha,Integer.MAX_VALUE);
                if (utility > currentBest)
                {
                    actionToDecide = action;
                    currentBest = utility;
                }
                alpha = Math.max(alpha, utility);
                if (currentBest == Integer.MAX_VALUE) return actionToDecide;
            }
            depth++;
        }
        System.out.println("Depth: " + depth);
        return actionToDecide;
    }

    private int heuristic(int[][] state)
    {
        return utilityForPlayer(state,playerID,opponentID) - utilityForPlayer(state,opponentID,playerID);
    }


    private int utilityForPlayer(int[][] state, int playerID, int opponentID)
    {
        int utility = 0;
        for(int i = 0; i < columns; i++)
        {
            for(int j = 0; j < rows; j++)
            {
                int localUtility = 1;

                for(int k = i; k < i+4 && i + 4 < columns; k++)
                {
                    if(state[k][j] == playerID)
                    {
                        localUtility *= 10;
                    }
                    else if(state[k][j] == opponentID)
                    {
                        localUtility = 0;
                        break;
                    }
                }

                utility += localUtility;
                localUtility = 1;

                for(int k = j; k < j+4 && j + 4 < rows; k++)
                {
                    if(state[i][k] == playerID)
                    {
                        localUtility *= 10;
                    }
                    else if(state[i][k] == opponentID)
                    {
                        localUtility = 0;
                        break;
                    }
                }

                utility += localUtility;
                localUtility = 1;

                // Diagonal check upwards
                for(int k = 0; k < 4 && j + 4 < rows && i + 4 < columns; k++)
                {
                    if(state[i+k][j+k] == playerID)
                    {
                        localUtility *= 10;
                    }
                    else if(state[i+k][j+k] == opponentID)
                    {
                        localUtility = 0;
                        break;
                    }
                }

                // Diagonal check downwards
                for(int k = 0; k < 4 && j - 4 > 0 && i + 4 < columns; k++)
                {
                    if(state[i+k][j-k] == playerID)
                    {
                        localUtility *= 10;
                    }
                    else if(state[i+k][j-k] == opponentID)
                    {
                        localUtility = 0;
                        break;
                    }
                }
            }
        }

        return  utility;
    }


    private int maxValue(final int[][] state, int depth, int alpha, int beta)
    {
        Winner winner = gameFinished(state);

        switch (winner)
        {
            case PLAYER1:
                if (playerID == 1)
                {
                    return Integer.MAX_VALUE;
                }
                return Integer.MIN_VALUE;
            case PLAYER2:
                if(playerID == 2)
                {
                    return Integer.MAX_VALUE;
                }
                return Integer.MIN_VALUE;
            case TIE:
                return 0;
            default:
                if (depth == 0)
                    return heuristic(state);
                break;
        }

        int utility = Integer.MIN_VALUE;

        ArrayList<Integer> availableActions = availableActions(state);

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(heuristic(result(state,o2,playerID)),heuristic(result(state,o1,playerID)));
            }
        });

        for (int action:availableActions) {
            utility = Math.max(utility, minValue(result(state, action, playerID), depth - 1,alpha,beta));
            if(utility >= beta)
            {
                return utility;
            }
            alpha = Math.max(alpha,utility);
        }
        return utility;
    }

    // Returns a utility value
    private int minValue(final int[][] state, int depth, int alpha, int beta)
    {
        Winner winner = gameFinished(state);

        switch (winner)
        {
            case PLAYER1:
                if (playerID == 1)
                {
                    return Integer.MAX_VALUE;
                }
                return Integer.MIN_VALUE;
            case PLAYER2:
                if(playerID == 2)
                {
                    return Integer.MAX_VALUE;
                }
                return Integer.MIN_VALUE;
            case TIE:
                return 0;
            default:
                if (depth == 0)
                    return heuristic(state);
                break;
        }


        int utility = Integer.MAX_VALUE;

        ArrayList<Integer> availableActions = availableActions(state);

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(heuristic(result(state,o1,opponentID)),heuristic(result(state,o2,opponentID)));
            }
        });

        for (int action:availableActions) {
            utility = Math.min(utility, maxValue(result(state, action, opponentID), depth - 1,alpha,beta));
            if(utility <= alpha)
            {
                return utility;
            }
            beta = Math.min(beta,utility);
        }
        return utility;
    }

    private int[][] result(int[][] state, int action, int playerID)
    {
        int[][] newState = getCopyState(state);
        int availableRow = availableRowInColumn(action,newState);

        newState[action][availableRow] = playerID;
        return newState;
    }

    public Winner gameFinished(int[][] state)
    {
        for (int column = 0; column<columns; column++)
        {
            for(int row = 0; row < rows; row++)
            {
                switch (playerWonOn(column,row,state))
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

        if(availableActions(state).isEmpty())
        {
            return  Winner.TIE;
        }

        return Winner.NOT_FINISHED;
    }


}
