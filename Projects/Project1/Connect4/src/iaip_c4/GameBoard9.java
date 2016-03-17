package iaip_c4;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard9 {
    private final int[][] state;
    private final int playerID, opponentID;
    private final int columns, rows;

    private GameBoard9(int[][] state, int playerID, int opponentID) {
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

    private int getRowPoints(int column, int row, int playerID) {
        if (column >= 0 && column + 4 < columns) {
            // If the bottom row is in the bottom, or if it has a token below it.
            if (row == 0 || (state[column][row - 1] != 0 && state[column + 4][row - 1] != 0)) {
                if (state[column][row] == 0 &&
                        state[column + 1][row] == playerID &&
                        state[column + 2][row] == playerID &&
                        state[column + 3][row] == playerID &&
                        state[column + 4][row] == 0)
                    return Integer.MAX_VALUE;
            }
        }

        int result = 1;
        if (column + 3 < columns) {
            for (int i = 0; i < 4; i++) {
                if (state[column + i][row] == playerID) result *= 10;
                else if (state[column + i][row] != 0) return 0; // Opponent blocked.
            }
        }

        return result;
    }

    private int getColumnPoints(int column, int row, int playerID) {
        int result = 1;
        if (row + 3 < rows) {
            for (int i = 0; i < 4; i++) {
                if (state[column][row + i] == playerID) result *= 10;
                else if (state[column][row + i] != 0) return 0; // Opponent blocked.
            }
        }
        return result;
    }

    private int getUpwardsDiagonalPoints(int column, int row, int playerID) {
        if (column + 4 < columns && row + 4 < rows) {
            // If the bottom row is in the bottom, or if it has a token below it.
            if ((row == 0 || state[column][row - 1] != 0) && state[column + 4][row + 3] != 0) {
                if (state[column][row] == 0 &&
                        state[column + 1][row + 1] == playerID &&
                        state[column + 2][row + 2] == playerID &&
                        state[column + 3][row + 3] == playerID &&
                        state[column + 4][row + 4] == 0)
                    return Integer.MAX_VALUE;
            }
        }

        int result = 1;
        if (column + 3 < columns && row + 3 < rows) {
            for (int i = 0; i < 4; i++) {
                if (state[column + i][row + i] == playerID) result *= 10;
                else if (state[column + i][row + i] != 0) return 0; // Opponent blocked.
            }
        }
        return result;
    }

    private int getDownwardsDiagonalPoints(int column, int row, int playerID){
        if (column + 4 < columns && row - 4 >= 0) {
            // If the bottom row is in the bottom, or if it has a token below it.
            if ((row - 4 == 0 || state[column + 4][row - 5] != 0) && state[column][row - 1] != 0) {
                if (state[column][row] == 0 &&
                        state[column + 1][row - 1] == playerID &&
                        state[column + 2][row - 2] == playerID &&
                        state[column + 3][row - 3] == playerID &&
                        state[column + 4][row - 4] == 0)
                    return Integer.MAX_VALUE;
            }
        }

        int result = 1;
        if (column + 3 < columns && row - 3 >= 0) {
            for (int i = 0; i < 4; i++) {
                if (state[column + i][row - i] == playerID) result *= 10;
                else if (state[column + i][row - i] != 0) return 0; // Opponent blocked.
            }
        }
        return result;
    }

    private int heuristic()
    {
        int result = 0;
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                result += getRowPoints(column, row, playerID);
                result += getColumnPoints(column, row, playerID);
                result += getUpwardsDiagonalPoints(column, row, playerID);
                result += getDownwardsDiagonalPoints(column, row, playerID);

                result -= getRowPoints(column, row, opponentID);
                result -= getColumnPoints(column, row, opponentID);
                result -= getUpwardsDiagonalPoints(column, row, opponentID);
                result -= getDownwardsDiagonalPoints(column, row, opponentID);
            }
        }
        return result;
    }

    public GameBoard9 result(int action, int playerID)
    {
        int[][] newState = getCopyState();
        int availableRow = availableRowInColumn(action);

        newState[action][availableRow] = playerID;
        return new GameBoard9(newState, this.playerID, opponentID);
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

    private int availableRowInColumn(int column)
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

    public boolean isSubsetOf(GameBoard9 gameBoard) {
        columnLoop:
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (state[col][row] != 0) {
                    if (state[col][row]!= gameBoard.state[col][row]) return false;
                } else {
                    // This is when we have reached the first available
                    // row in a column in the current assignment. Everything
                    // above this is also unassigned, and can therefore be
                    // ignored.
                    continue columnLoop;
                }
            }
        }
        return true;
    }
}
