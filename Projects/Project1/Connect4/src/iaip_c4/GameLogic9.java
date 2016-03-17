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

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        utility(result(gameBoard,o2,playerID)).utility,
                        utility(result(gameBoard,o1,playerID)).utility);
            }
        });

        long startTime = System.currentTimeMillis();

        Utility9 currentBest = new Utility9(Integer.MIN_VALUE, false);
        int actionToDecide = availableActions.get(0);
        int depth = 1;
        OUTERLOOP:while (System.currentTimeMillis() - startTime < 10000) {

            int alpha = Integer.MIN_VALUE;
            for (int action : availableActions) {

                Utility9 utility = minValue(result(gameBoard, action, playerID), depth, alpha, Integer.MAX_VALUE);
                if (utility.utility > currentBest.utility)
                {
                    actionToDecide = action;
                    currentBest = utility;
                }
                alpha = Math.max(alpha, utility.utility);
                if (currentBest.utility == Integer.MAX_VALUE) break OUTERLOOP;
            }
            if(currentBest.isTerminal)
            {
                break;
            }
            depth++;
        }
        System.out.println("Depth: " + depth);
        return actionToDecide;
    }

    private int heuristic(int[][] state)
    {
        int utilityOpponent = 0;
        int utilityPlayer = 0;
        for(int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                utilityOpponent += utilityForPlayer(state,opponentID,playerID, i, j);
                utilityPlayer += utilityForPlayer(state,playerID,opponentID,i, j);
            }
        }

        return  utilityPlayer - utilityOpponent;

    }
    private Utility9 utility(int[][] state)
    {
        Winner winner = gameFinished(state);

        switch (winner)
        {
            case PLAYER1:
                if (playerID == 1)
                {
                    return new Utility9(Integer.MAX_VALUE,true);
                }
                return new Utility9(Integer.MIN_VALUE,true);
            case PLAYER2:
                if(playerID == 2)
                {
                    return new Utility9(Integer.MAX_VALUE,true);
                }
                return new Utility9(Integer.MIN_VALUE,true);
            case TIE:
                return new Utility9(0,true);
            default:
                return new Utility9(heuristic(state),false);
        }
    }

    private int utilityForPlayer(int[][] state, int player, int opponent, int i, int j)
    {
        int utility = 0;
        int localUtility = 1;

        for(int k = i; k < i+4 && i + 4 < columns; k++)
        {
            if(state[k][j] == player)
            {
                localUtility *= 10;
            }
            else if(state[k][j] == opponent)
            {
                localUtility = 0;
                break;
            }
        }

        utility += localUtility;
        localUtility = 1;

        for(int k = j; k < j+4 && j + 4 < rows; k++)
        {
            if(state[i][k] == player)
            {
                localUtility *= 10;
            }
            else if(state[i][k] == opponent)
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
            if(state[i+k][j+k] == player)
            {
                localUtility *= 10;
            }
            else if(state[i+k][j+k] == opponent)
            {
                localUtility = 0;
                break;
            }
        }
        utility += localUtility;
        localUtility = 1;

        // Diagonal check downwards
        for(int k = 0; k < 4 && j - 4 > 0 && i + 4 < columns; k++)
        {
            if(state[i+k][j-k] == player)
            {
                localUtility *= 10;
            }
            else if(state[i+k][j-k] == opponent)
            {
                localUtility = 0;
                break;
            }
        }
        utility += localUtility;
        return  utility;
    }


    private Utility9 maxValue(final int[][] state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = utility(state);
        if(depth==0) return maybeTerminal;
        else if(maybeTerminal.isTerminal) return maybeTerminal;



        ArrayList<Integer> availableActions = availableActions(state);

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        utility(result(state,o2,playerID)).utility,
                        utility(result(state,o1,playerID)).utility);
            }
        });

        Utility9 value = new Utility9(Integer.MIN_VALUE, false);
        for (int action:availableActions)
        {
            Utility9 utilityValue = minValue(result(state, action, playerID), depth - 1,alpha,beta);
            value = Utility9.max(value, utilityValue);
            if(value.utility >= beta)
            {
                return value;
            }
            alpha = Math.max(alpha,value.utility);

            if(value.isTerminal && value.utility == Integer.MAX_VALUE)
            {
                return value;
            }
        }
        return value;
    }

    // Returns a utility value
    private Utility9 minValue(final int[][] state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = utility(state);
        if(depth==0) return maybeTerminal;
        else if(maybeTerminal.isTerminal) return maybeTerminal;

        ArrayList<Integer> availableActions = availableActions(state);

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        utility(result(state,o1,opponentID)).utility,
                        utility(result(state,o2,opponentID)).utility);
            }
        });

        Utility9 value = new Utility9(Integer.MAX_VALUE,false);

        for (int action:availableActions) {
            Utility9 utilityValue = maxValue(result(state, action, opponentID), depth - 1,alpha,beta);
            value = Utility9.min(value, utilityValue);
            if(value.utility <= alpha)
            {
                return value;
            }
            beta = Math.min(beta,value.utility);

            if(value.isTerminal && value.utility == Integer.MIN_VALUE)
            {
                return value;
            }
        }
        return value;
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