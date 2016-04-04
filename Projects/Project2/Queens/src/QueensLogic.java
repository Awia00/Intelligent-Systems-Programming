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
    private int size;
    private int[][] board;
    private BDDFactory mFactory;
    private BDD bdd;


    public QueensLogic() {
       //constructor
        mFactory = JFactory.init(2000000, 200000);
    }

    public void initializeGame(int size) {
        this.size = size;
        this.board = new int[size][size];

        mFactory.setVarNum(size * size);

        bdd = buildNQueenBDD(size);

        updateGameBoard();
    }

   
    public int[][] getGameBoard() {
        return board;
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;

        // This line restricts the variable on coloumn, row to be a queen in the BDD
        bdd.restrictWith(mFactory.ithVar(getVariableFromCell(column, row)));

        // If this is the case, then we have placed a queen in a cell that was checked
        assert(!bdd.isZero());

        // Update the gameboard according to the new BDD where (column,row) is restricted.
        updateGameBoard();
      
        return true;
    }

    // get the variable number of the given coloumn, row.
    private int getVariableFromCell(int column, int row) {
        return column * size + row;
    }

    private void updateGameBoard() {
        // Check to see if the game board is not satisfiable / this means that there was never a solution for example boards of size 2x2 and 3x3.
        if (bdd.isZero()) {
            for (int column = 0; column < size; column++) {
                Arrays.fill(board[column], -1);
            }
            return;
        }


        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                // make column,row true
                BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));
                // create new bdd where column row is restricted to true.
                BDD res = bdd.restrict(ithVar);

                // if the solution is not satisfiable then coloumn row can never have a queen,
                // and we mark it with x
                if (res.isZero()) { // Unsatisfiable situation
                    board[column][row] = -1;
                }

                // release memory
                res.free();

                // make column,row false
                BDD nithVar = mFactory.nithVar(getVariableFromCell(column, row));
                // create new bdd where column row is restricted to false.
                res = bdd.restrict(nithVar);

                // if the bdd is not satisfiable then we must have a queen on that spot,
                // since it may ever be false and we can therefore put a queen there.
                if (res.isZero()) { // Unsatisfiable situation
                    board[column][row] = 1;
                }

                // free memory
                res.free();
                ithVar.free();
                nithVar.free();
            }
        }


    }

    private BDD buildNQueenBDD(int size) {
        BDD bdd = mFactory.one();

        // Horizontal rules
        // if column, row is true, then every other variable in that row must be false.
        // There must be atleast one true in a row
        // false OR (column,row) and not column+1,row ... OR (column+1,row) and not column,row ...
        for (int row = 0; row < size; row++) {
            BDD rowBDD = mFactory.zero();
            for (int i = 0; i < size; i++) { // i is the queen variable
                BDD ithVar = mFactory.ithVar(getVariableFromCell(i, row));
                for (int j = 0; j < size; j++) { // j is the checked variable
                    if (i == j) continue;
                    ithVar.andWith(mFactory.nithVar(getVariableFromCell(j, row)));
                }
                rowBDD.orWith(ithVar);
            }
            // this is done for each row such that
            // BDD AND false OR (column,row+1) and not column+1,row+1 ...
            bdd.andWith(rowBDD);
        }

        // Vertical rules
        // same principle as the one above just with columns.
        for (int column = 0; column < size; column++) {
            BDD columnBDD = mFactory.zero();
            for (int i = 0; i < size; i++) {
                BDD ithVar = mFactory.ithVar(getVariableFromCell(column, i));
                for (int j = 0; j < size; j++) {
                    if (i == j) continue;
                    ithVar.andWith(mFactory.nithVar(getVariableFromCell(column, j)));
                }
                columnBDD.orWith(ithVar);
            }
            bdd.andWith(columnBDD);
        }

        // Diagonals
        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                { // Upwards Diagonal
                    BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));
                    int i = 0;
                    // find the first cell(lowest values) of the board in this upwards diagonal.
                    while (column + i > 0 && row + i > 0) {
                        i--;
                    }

                    BDD t = mFactory.one();
                    // every variable except column,row is false in this diagonal.
                    for (int j = column + i, k = row + i; j < size && k < size; j++, k++) {
                        if (j == column && k == row) continue;
                        t.andWith(mFactory.nithVar(getVariableFromCell(j, k)));
                    }
                    // column,row implies that every other variable in diagonal is false.
                    // so if column, row is false the rest of the bdd is true / the rest of the values does not matter.
                    ithVar.impWith(t);
                    // AND together with our BDD
                    bdd.andWith(ithVar); // Is this correct?
                }

                { // Downwards Diagonal
                    BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));

                    int i = 0;
                    // find the first cell(lowest values for column but highest for row) of the board in this downwards diagonal.
                    while (column + i > 0 && row - i < size - 1) {
                        i--;
                    }

                    BDD t = mFactory.one();
                    // every variable except column,row is false in this diagonal.
                    for (int j = column + i, k = row - i; j < size && k >= 0; j++, k--) {
                        if (j == column && k == row) continue;
                        t.andWith(mFactory.nithVar(getVariableFromCell(j, k)));
                    }
                    // column,row implies that every other variable in diagonal is false.
                    // so if column, row is false the rest of the bdd is true / the rest of the values does not matter.
                    ithVar.impWith(t);
                    // AND together with our BDD
                    bdd.andWith(ithVar);
                }
            }
        }

        return bdd;
    }
}
