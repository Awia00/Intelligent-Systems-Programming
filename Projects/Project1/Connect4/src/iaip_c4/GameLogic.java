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
        for (int i = 0; i < gameState.length; i++)
        {
            for (int j = 0; j < gameState[i].length; j++)
            {
                int winner = 0;
                if(CheckWin(gameState, i, j, playerID)) winner = playerID;
                if(CheckWin(gameState, i, j, opponentID)) winner = opponentID;
                if (winner != 0) {
                    return winner == 1 ? Winner.PLAYER1 : Winner.PLAYER2;
                }
            }
        }
        if(getPossibleActions(gameState).isEmpty()) return Winner.TIE;

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

    public int decideNextMove()
    {
        ArrayList<Integer> actions = getPossibleActions(gameState);
        int decision = -1;
        int value = Integer.MIN_VALUE;
        for (int action : actions)
        {
            int actionValue = minValue(result(gameState, action, playerID), 5);
            if (actionValue > value)
            {
                decision = action;
                value = actionValue;
            }
        }
        return decision;
    }

    private int[][] getCopyState(int[][] oldState)
    {
        int [][] state = new int[x][];
        for(int i = 0; i < oldState.length; i++)
            state[i] = oldState[i].clone();
        return state;
    }

    private int minValue(int[][] state, int depth)
    {
        if(isTerminal(state) || depth == 0) return utility(state);

        int value = Integer.MAX_VALUE;
        for (int action : getPossibleActions(state)) {
            value = Math.min(value, maxValue(result(state, action, opponentID), depth-1));
        }
        return value;
    }
    private int maxValue(int[][] state, int depth)
    {
        if(isTerminal(state) || depth == 0) return utility(state);
        int value = Integer.MIN_VALUE;
        for (int action : getPossibleActions(state)) {
            value = Math.max(value, minValue(result(state, action, playerID), depth-1));
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
        for (int i = 0; i < gameState.length; i++)
        {
            for (int j = 0; j < gameState[i].length; j++)
            {
                if(CheckWin(state, i,j, playerID)) return true;
                if(CheckWin(state, i,j, opponentID)) return true;
            }
        }
        if(getPossibleActions(state).isEmpty()) return true;
        return false;
    }
    private int utility(int[][] state)
    {
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[i].length; j++)
            {
                if(CheckWin(state, i, j, playerID)) return 1000;
                if(CheckWin(state, i, j, opponentID)) return -1000;
            }
        }
        if(getPossibleActions(state).isEmpty()) return 0;
        return heuristic(state);
    }

    private int heuristic(int[][] state)
    {
        int highestValueOwn = 0;
        int currentValueOwn = 0;
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[i].length; j++)
            {
                for (int offset = i; offset < state.length && offset<i+4; offset++)
                {
                    if(state[offset][j] == playerID)
                    {
                        currentValueOwn++;
                    }
                    else if (state[offset][j] == opponentID)
                    {
                        currentValueOwn = 0;
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
                highestValueOwn += currentValueOwn;
                currentValueOwn = 0;
                for (int offset = j; offset < state[i].length && offset<j+4; offset++)
                {
                    if(state[i][offset] == playerID)
                    {
                        currentValueOwn++;
                    }
                    else if (state[i][offset] == opponentID)
                    {
                        currentValueOwn = 0;
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
                highestValueOwn += currentValueOwn;
                currentValueOwn = 0;
                for (int offset = 0; i+offset < state.length && j+offset < state[i].length && offset<4; offset++)
                {
                    if(state[i+offset][j+offset] == playerID)
                    {
                        currentValueOwn++;
                    }
                    else if (state[i+offset][j+offset] == opponentID)
                    {
                        currentValueOwn = 0;
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
                highestValueOwn += currentValueOwn;
                currentValueOwn = 0;
                for (int offset = 0; i+offset < state.length && j-offset >= 0 && offset<4; offset++)
                {
                    if(state[i+offset][j-offset] == playerID)
                    {
                        currentValueOwn++;
                    }
                    else if (state[i+offset][j-offset] == opponentID)
                    {
                        currentValueOwn = 0;
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
                highestValueOwn += currentValueOwn;
                currentValueOwn = 0;
            }
        }
        int total = highestValueOwn;
        return total;
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
