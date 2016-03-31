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

        // put some logic here..
        bdd.restrictWith(mFactory.ithVar(getVariableFromCell(column, row)));

        // If this is the case, then we have placed a queen in a cell that was checked
        assert(!bdd.isZero());

        updateGameBoard();
      
        return true;
    }

    private int getVariableFromCell(int column, int row) {
        return column * size + row;
    }

    private void updateGameBoard() {
        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));

                BDD res = bdd.restrict(ithVar);

                if (res.isZero()) { // Unsatisfiable situation
                    board[column][row] = -1;
                }

                res.free();

                BDD nithVar = mFactory.nithVar(getVariableFromCell(column, row));
                res = bdd.restrict(nithVar);

                if (res.isZero()) { // Unsatisfiable situation
                    board[column][row] = 1;
                }

                res.free();
                ithVar.free();
                nithVar.free();
            }
        }


    }

    private BDD buildNQueenBDD(int size) {
        BDD bdd = mFactory.one();

        // Horizontal rules
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
            bdd.andWith(rowBDD);
        }

        // Vertical rules
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

        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                { // Upwards Diagonal
                    BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));
                    int i = 0;
                    while (column + i > 0 && row + i > 0) {
                        i--;
                    }

                    BDD t = mFactory.one();
                    for (int j = column + i, k = row + i; j < size && k < size; j++, k++) {
                        if (j == column && k == row) continue;
                        t.andWith(mFactory.nithVar(getVariableFromCell(j, k)));
                    }
                    ithVar.impWith(t);
                    bdd.andWith(ithVar); // Is this correct?
                }

                { // Downwards Diagonal
                    BDD ithVar = mFactory.ithVar(getVariableFromCell(column, row));

                    int i = 0;
                    while (column + i > 0 && row - i < size - 1) {
                        i--;
                    }

                    BDD t = mFactory.one();
                    for (int j = column + i, k = row - i; j < size && k >= 0; j++, k--) {
                        if (j == column && k == row) continue;
                        t.andWith(mFactory.nithVar(getVariableFromCell(j, k)));
                    }
                    ithVar.impWith(t);
                    bdd.andWith(ithVar);
                }
            }
        }

        return bdd;
    }
}
