package iaip_c4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic9 implements IGameLogic {
    private int playerID, opponentID;
    private final Map<GameBoard9, Utility9> utilityMap;
    private final Object lock = new Object();
    private int lastDepth = 2;

    private GameBoard9 gameBoard;
    
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
        System.out.print("Map size before: " + utilityMap.size());
        for (GameBoard9 gb : utilityMap.keySet()) {
            if (!gameBoard.isSubsetOf(gb)) {
                utilityMap.remove(gb);
            }
        }
        System.out.println(". after: " + utilityMap.size() + ".");
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

        MiniMaxRunnable9 runnable = new MiniMaxRunnable9(startTime, availableActions, lastDepth);

        Thread t = new Thread(runnable);

        t.start();

        synchronized (lock) {
            try {
                lock.wait(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.interrupt();
        }

        System.out.println("Time: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " s");
        System.out.println("Depth: " + (lastDepth = runnable.depth));
        return runnable.actionToDecide;
    }

    private Utility9 maxValue(final GameBoard9 state, int depth, int alpha, int beta)
    {
        Utility9 maybeTerminal = state.utility();
        if(depth==0 || maybeTerminal.isTerminal) return maybeTerminal;

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
            GameBoard9 newState = state.result(action, playerID);
            Utility9 utilityValue;
            if (utilityMap.containsKey(newState)) {
                utilityValue = utilityMap.get(newState);
            } else {
                utilityValue = minValue(newState, depth - 1, alpha, beta);
                if (utilityValue.isTerminal)
                    utilityMap.put(newState, utilityValue);
            }
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

    public class MiniMaxRunnable9 implements Runnable {
        private final long startTime;
        private final ArrayList<Integer> availableActions;
        public int actionToDecide;
        private Utility9 currentBest;
        public int depth;

        public MiniMaxRunnable9(long startTime, ArrayList<Integer> availableActions, int lastDepth) {
            this.startTime = startTime;
            this.availableActions = availableActions;
            currentBest = new Utility9(Integer.MIN_VALUE, false);
            actionToDecide = availableActions.get(0);
            depth = lastDepth - 2;
        }

        @Override
        public void run() {
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
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}