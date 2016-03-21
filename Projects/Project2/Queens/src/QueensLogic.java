/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import net.sf.javabdd.*;

public class QueensLogic {
    private int size = 0;
    private int[][] board;


    public QueensLogic() {
       //constructor
    }

    public void initializeGame(int size) {
        this.size = size;
        this.board = new int[size][size];
    }

   
    public int[][] getGameBoard() {
        return board;
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;

        // put some logic here..

      
        return true;
    }
}
