package iaip_c4;

import java.util.ArrayList;
import java.util.Comparator;

public class GameLogic9 implements IGameLogic {
    private int playerID, opponentID;

    private GameBoard9 gameBoard;
    
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
        this.playerID = playerID;
        this.opponentID = playerID == 1 ? 2 : 1;

        gameBoard = new GameBoard9(columns, rows, playerID, opponentID);
    }

    /**
     * Checks if any of the two players have 4-in-a-row.
     * @return Winner enum
     */
    public Winner gameFinished()
    {
        return gameBoard.gameFinished();
    }

    /**
     * Notifies that a token/coin is put in the specified column of
     * the game board.
     * @param column The column where the coin is inserted.
     * @param playerID The ID of the current player.
     */
    public void insertCoin(int column, int playerID)
    {
        gameBoard = gameBoard.result(column, playerID);
    }

    /**
     * Calculates the next move  This is where you should
     * implement/call your heuristic evaluation functions etc.
     */
    public int decideNextMove()
    {
        System.out.println("Player " + playerID + " started calculating");
        ArrayList<Integer> availableActions = gameBoard.availableActions();

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        gameBoard.result(o2,playerID).utility().utility,
                        gameBoard.result(o1,playerID).utility().utility);
            }
        });

        long startTime = System.currentTimeMillis();

        Utility9 currentBest = new Utility9(Integer.MIN_VALUE, false);
        int actionToDecide = availableActions.get(0);
        int depth = 1;
        OUTERLOOP:while (System.currentTimeMillis() - startTime < 10000) {

            int alpha = Integer.MIN_VALUE;
            for (int action : availableActions) {

                Utility9 utility = minValue(gameBoard.result(action, playerID), depth, alpha, Integer.MAX_VALUE);
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

    private Utility9 maxValue(final GameBoard9 state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = state.utility();
        if(depth==0) return maybeTerminal;
        else if(maybeTerminal.isTerminal) return maybeTerminal;



        ArrayList<Integer> availableActions = state.availableActions();

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        state.result(o2,playerID).utility().utility,
                        state.result(o1,playerID).utility().utility);
            }
        });

        Utility9 value = new Utility9(Integer.MIN_VALUE, false);
        for (int action:availableActions)
        {
            Utility9 utilityValue = minValue(state.result(action, playerID), depth - 1,alpha,beta);
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
    private Utility9 minValue(final GameBoard9 state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = state.utility();
        if(depth==0) return maybeTerminal;
        else if(maybeTerminal.isTerminal) return maybeTerminal;

        ArrayList<Integer> availableActions = state.availableActions();

        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        state.result(o1,opponentID).utility().utility,
                        state.result(o2,opponentID).utility().utility);
            }
        });

        Utility9 value = new Utility9(Integer.MAX_VALUE,false);

        for (int action:availableActions) {
            Utility9 utilityValue = maxValue(state.result(action, opponentID), depth - 1,alpha,beta);
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
}