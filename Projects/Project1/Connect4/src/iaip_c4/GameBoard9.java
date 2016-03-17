package iaip_c4;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard9 {
    private final int[][] state;
    private final int playerID, opponentID;
    private final int columns, rows;

    public GameBoard9(int[][] state, int playerID, int opponentID) {
        this.state = state;
        this.columns = state.length;
        this.rows = state[0].length;
        this.playerID = playerID;
        this.opponentID = opponentID;
    }

    public GameBoard9(int columns, int rows, int playerID, int opponentID) {
        this.state = new int[columns][rows];
        this.playerID = playerID;
        this.opponentID = opponentID;
        this.columns = columns;
        this.rows = rows;
    }

    private int heuristic()
    {
        int utilityOpponent = 0;
        int utilityPlayer = 0;
        for(int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                utilityOpponent += heuristicForPlayer(opponentID,playerID, i, j);
                utilityPlayer += heuristicForPlayer(playerID,opponentID,i, j);
            }
        }

        return  utilityPlayer - utilityOpponent;

    }

    public GameBoard9 result(int action, int playerID)
    {
        int[][] newState = getCopyState();
        int availableRow = availableRowInColumn(action);

        newState[action][availableRow] = playerID;
        return new GameBoard9(newState, playerID, opponentID);
    }

    public IGameLogic.Winner gameFinished()
    {
        for (int column = 0; column<columns; column++)
        {
            for(int row = 0; row < rows; row++)
            {
                switch (playerWonOn(column,row))
                {
                    case 1:
                        return IGameLogic.Winner.PLAYER1;
                    case 2:
                        return IGameLogic.Winner.PLAYER2;
                    default:
                        break;
                }
            }
        }

        if(availableActions().isEmpty())
        {
            return  IGameLogic.Winner.TIE;
        }

        return IGameLogic.Winner.NOT_FINISHED;
    }

    private int playerWonOn(int column, int row)
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

    public Utility9 utility()
    {
        IGameLogic.Winner winner = gameFinished();

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
                return new Utility9(heuristic(),false);
        }
    }

    private int heuristicForPlayer(int player, int opponent, int i, int j)
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

    public int availableRowInColumn(int column)
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

    public ArrayList<Integer> availableActions()
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

    private int[][] getCopyState()
    {
        int [][] newState = new int[state.length][];
        for(int i = 0; i < state.length; i++)
            newState[i] = state[i].clone();
        return newState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameBoard9 that = (GameBoard9) o;

        return Arrays.deepEquals(state, that.state);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }
}
