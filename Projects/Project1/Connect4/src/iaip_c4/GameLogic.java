package iaip_c4;

import java.util.ArrayList;

public class GameLogic implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    private int opponentID;
    int[][] gameState;
    
    public GameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        opponentID = playerID == 1 ? 2 : 1;
        gameState = new int[x][y];
        //TODO Write your implementation for this method
    }
	
    public Winner gameFinished() {
        return findWinner(gameState);
    }

    public Winner findWinner(int[][] state)
    {
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[i].length; j++)
            {
                int winner = 0;
                if(CheckWin(state, i, j, playerID)) winner = playerID;
                if(CheckWin(state, i, j, opponentID)) winner = opponentID;
                if (winner != 0) {
                    return winner == 1 ? Winner.PLAYER1 : Winner.PLAYER2;
                }
            }
        }
        if(getPossibleActions(state).isEmpty()) return Winner.TIE;

        return Winner.NOT_FINISHED;
    }


    public void insertCoin(int column, int playerID) {
        gameState = result(gameState, column, playerID);
    }

    private int[][] result(int[][] state, int action, int playerID)
    {
        int[][] newState = getCopyState(state);
        newState[action][getEmptyRowOnColumn(state, action)] = playerID;
        return newState;
    }
    private int[][] getCopyState(int[][] oldState)
    {
        int [][] state = new int[x][];
        for(int i = 0; i < oldState.length; i++)
            state[i] = oldState[i].clone();
        return state;
    }

    public int decideNextMove()
    {
        maxValue(gameState, Integer.MIN_VALUE, Integer.MAX_VALUE, 9,true);
        int decision = bestMove;
        bestMove = -1;
        return decision;
    }
    private int bestMove = -1;

    private int maxValue(int[][] state, int alpha, int beta, int depth, boolean isRoot)
    {
        if(isTerminal(state) || depth == 0) return utility(state);

        int value = Integer.MIN_VALUE;
        for (int action : getPossibleActions(state)) {
            int moveValue = minValue(result(state, action, playerID), alpha, beta, depth-1, false);
            if(moveValue > value )
            {
                value = moveValue;
                if(isRoot)
                {
                    bestMove = action;
                }
            }
            if(value >= beta) return value;
            alpha = Math.max(value,alpha);
        }
        return value;
    }
    private int minValue(int[][] state, int alpha, int beta, int depth, boolean isRoot)
    {
        if(isTerminal(state) || depth == 0) return utility(state);

        int value = Integer.MAX_VALUE;
        for (int action : getPossibleActions(state)) {
            value = Math.min(value, maxValue(result(state, action, opponentID), alpha, beta, depth-1, false));

            if(value <= alpha) return value;
            beta = Math.min(value,beta);
        }

        return value;
    }


    private ArrayList<Integer> getPossibleActions(int[][] state)
    {
        int i = 0;
        ArrayList<Integer> actionColumns = new ArrayList<>();
        while(i < x)
        {
            if (getEmptyRowOnColumn(state, i) != -1) actionColumns.add(i);
            i++;
        }
        return actionColumns;
    }

    private boolean isTerminal(int[][] state)
    {
        if(findWinner(state) == Winner.NOT_FINISHED) return false;
        return true;
    }
    private int utility(int[][] state)
    {
        Winner winner = findWinner(state);
        switch (winner) {
            case NOT_FINISHED:
                return heuristic(state);
            case TIE:
                return 0;
            case PLAYER1:
                if (playerID==1)
                    return Integer.MAX_VALUE-1;
                else
                    return Integer.MIN_VALUE+1;
            case PLAYER2:
                if (playerID==1)
                    return Integer.MIN_VALUE+1;
                else
                    return Integer.MAX_VALUE-1;
            default:
                return heuristic(state);
        }
    }

    private int heuristic(int[][] state)
    {
        int ownValue = 0;
        int opponentValue = 0;
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[i].length; j++)
            {
                ownValue += playerHeuristic(state, i, j, playerID, opponentID);
                opponentValue += playerHeuristic(state, i, j, opponentID, playerID);
            }
        }
        int total = ownValue-opponentValue;
        return total;
    }

    private int playerHeuristic(int[][] state, int i, int j, int playerID, int opponentID)
    {
        int highestValue = 0;
        int currentValue = 1;

        for (int offset = i; offset < state.length && offset<i+3; offset++)
        {
            if(state[offset][j] == playerID)
            {
                currentValue*=10;
            }
            else if (state[offset][j] == opponentID)
            {
                currentValue = 0;
                break;
            }
        }
        highestValue += currentValue;
        currentValue = 1;
        for (int offset = j; offset < state[i].length && offset<j+3; offset++)
        {
            if(state[i][offset] == playerID)
            {
                currentValue*=10;
            }
            else if (state[i][offset] == opponentID)
            {
                currentValue = 0;
                break;
            }
        }
        highestValue += currentValue;
        currentValue = 1;
        for (int offset = 0; i+offset < state.length && j+offset < state[i].length && offset<3; offset++)
        {
            if(state[i+offset][j+offset] == playerID)
            {
                currentValue*=10;
            }
            else if (state[i+offset][j+offset] == opponentID)
            {
                currentValue = 0;
                break;
            }
        }
        highestValue += currentValue;
        currentValue = 1;
        for (int offset = 0; i+offset < state.length && j-3 >= 0 && offset<3; offset++)
        {
            if(state[i+offset][j-offset] == playerID)
            {
                currentValue*=2;
            }
            else if (state[i+offset][j-offset] == opponentID)
            {
                currentValue = 0;
                break;
            }
        }
        highestValue += currentValue;
        return highestValue;
    }

    private int getEmptyRowOnColumn(int[][] state, int column)
    {
        for (int i = 0; i < state[column].length ;i++)
        {
            if(state[column][i] == 0) return i;
        }
        return -1; // no possible row
    }

    private boolean CheckWin(int[][] state, int i, int j, int playerId)
    {
        if(j+3<y) // row
        {
            if(state[i][j]==playerId && state[i][j+1]==playerId && state[i][j+2]==playerId && state[i][j+3]==playerId)
            {
                return true;
            }
        }
        if(i+3<x) // column
        {
            if(state[i][j]==playerId && state[i+1][j]==playerId && state[i+2][j]==playerId && state[i+3][j]==playerId)
            {
                return true;
            }
            if(j+3<y) // diagonal upwards
            {
                if(state[i][j]==playerId && state[i+1][j+1]==playerId && state[i+2][j+2]==playerId && state[i+3][j+3]==playerId)
                {
                    return true;
                }
            }
            if(j-3>=0) // diagonal downwards
            {
                if(state[i][j]==playerId && state[i+1][j-1]==playerId && state[i+2][j-2]==playerId && state[i+3][j-3]==playerId)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
