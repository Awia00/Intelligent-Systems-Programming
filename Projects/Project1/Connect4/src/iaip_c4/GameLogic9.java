package iaip_c4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains Group 9's game logic for the Connect Four game.
 */
public class GameLogic9 implements IGameLogic {
    /**
     * The utility map is used to reduce the number of utility calculations necessary by saving result values.
     */
    private final Map<GameBoard9, Utility9> utilityMap;
    private final Object lock = new Object();

    private int playerID, opponentID;
    private int lastDepth = 2;
    private GameBoard9 gameBoard;

    /**
     * Required empty constructor, because the ShowGame class, uses reflection to construct the logic.
     *
     * Sets up the utility map.
     */
    public GameLogic9() {
        utilityMap = new ConcurrentHashMap<>();
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

        // This part cleans up the utility map, by removing all states that are no longer possible given the latest move.
        for (GameBoard9 gb : utilityMap.keySet()) {
            if (!gameBoard.isSubsetOf(gb)) {
                utilityMap.remove(gb);
            }
        }
    }

    /**
     * Calculates the next move  This is where you should
     * implement/call your heuristic evaluation functions etc.
     */
    public int decideNextMove()
    {
        System.out.println("Player " + playerID + " started calculating");
        ArrayList<Integer> availableActions = gameBoard.availableActions();

        // Order the available actions by heuristic in order to make better use of alpha-beta pruning.
        availableActions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(
                        gameBoard.result(o2,playerID).utility().utility,
                        gameBoard.result(o1,playerID).utility().utility);
            }
        });

        long startTime = System.currentTimeMillis();

        MiniMaxRunnable9 runnable = new MiniMaxRunnable9(startTime, availableActions, lastDepth);
        Thread t = new Thread(runnable);
        t.start();

        synchronized (lock) {
            try {
                // Run the runnable, that contains the alpha-beta algorithm, but wait for at most 30 seconds.
                lock.wait(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // If the time passes, we interrupt the thread, and return the value it has most recently calculated to be the "best".
            t.interrupt();
        }

        System.out.println("Time: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " s");
        System.out.println("Depth: " + (lastDepth = runnable.depth));
        return runnable.actionToDecide;
    }

    /**
     * Calculates the max value of a given state.
     *
     * If the depth drops to 0, a heuristic is returned, if the state is not a terminal state.
     * @param state The game board, with a given assignment.
     * @param depth The current depth, when this drops to 0, we don't search any further.
     * @param alpha The alpha value used to prune the search tree.
     * @param beta The beta value used to prune the search tree.
     * @return A utility value, or heuristic if the depth limit was reached.
     */
    private Utility9 maxValue(final GameBoard9 state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = state.utility();
        if(depth==0 || maybeTerminal.isTerminal) return maybeTerminal;

        ArrayList<Integer> availableActions = state.availableActions();

        // Order the available actions by the result of the heuristic function.
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
            GameBoard9 newState = state.result(action, playerID);
            Utility9 utilityValue;

            // If this state has been reached before, retrieve the calculated result
            if (utilityMap.containsKey(newState)) {
                utilityValue = utilityMap.get(newState);
            } else {
            // Otherwise calculate the result, and save it for later use, if it is a terminal state that was the result.
                utilityValue = minValue(newState, depth - 1, alpha, beta);
                if (utilityValue.isTerminal)
                    utilityMap.put(newState, utilityValue);
            }
            // Only save the best value, and prune the tree by alpha-beta.
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

    /**
     * Calculates the min value of a given state.
     *
     * If the depth drops to 0, a heuristic is returned, if the state is not a terminal state.
     * @param state The game board, with a given assignment.
     * @param depth The current depth, when this drops to 0, we don't search any further.
     * @param alpha The alpha value used to prune the search tree.
     * @param beta The beta value used to prune the search tree.
     * @return A utility value, or heuristic if the depth limit was reached.
     */
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
            GameBoard9 newState = state.result(action, opponentID);
            Utility9 utilityValue;
            if (utilityMap.containsKey(newState)) {
                utilityValue = utilityMap.get(newState);
            } else {
                utilityValue = maxValue(newState, depth - 1, alpha, beta);
                if (utilityValue.isTerminal)
                    utilityMap.put(newState, utilityValue);
            }
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

    /**
     * A runnable class, which implements the top level of the Alpha-Beta-Search algorithm.
     *
     * This is used to make a hard limit on the time the search algorithm can run before being stopped.
     */
    public class MiniMaxRunnable9 implements Runnable {
        private final long startTime;
        private final ArrayList<Integer> availableActions;
        public int actionToDecide;
        private Utility9 currentBest;
        public int depth;

        /**
         * Sets up the runnable for alpha-beta-search.
         * @param startTime The starting time of the search. Used to make a soft limit that can be exceeded, but if we finish an iteration in the iterative deepening search, we voluntarily returns the result of our current calculations.
         * @param availableActions A list of available actions.
         * @param lastDepth The depth that was reached by the last iteration.
         */
        public MiniMaxRunnable9(long startTime, ArrayList<Integer> availableActions, int lastDepth) {
            this.startTime = startTime;
            this.availableActions = availableActions;
            currentBest = new Utility9(Integer.MIN_VALUE, false);
            actionToDecide = availableActions.get(0);
            depth = lastDepth - 2; // Start the iterative deepening in the depth that was reached last time (two moves ago).
        }

        @Override
        public void run() {
            // As long as time permits.
            OUTERLOOP:while (System.currentTimeMillis() - startTime < 10000) {
                int alpha = Integer.MIN_VALUE;
                // Run alpha beta.
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
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}