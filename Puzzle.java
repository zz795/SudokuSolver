import java.io.*;
import java.util.*;

public class Puzzle {
    public static final int DIM = 9;
    public static final int SUBGRID_DIM = 3;
    private int[][] values;
    private boolean[][] valIsFixed;
    private boolean[][][] subgridHasValue;

    private boolean[][] isInRow;
    private boolean[][] isInCol;

    
    
    /* 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Puzzle() {
        this.values = new int[DIM][DIM];
        this.valIsFixed = new boolean[DIM][DIM];
        
        // Note that the third dimension of the array has a length
        // of DIM + 1, because we want to be able to use the possible
        // values (1 through 9) as indices.
        this.subgridHasValue = new boolean[SUBGRID_DIM][SUBGRID_DIM][DIM + 1];        

        this.isInRow = new boolean[DIM][DIM+1];
        this.isInCol = new boolean[DIM][DIM+1];
    }

    /*
     * determine if it's save to place a number
     */
    public boolean isSafe(int row, int col, int value){
        return (this.valIsFixed[row][col] == false && this.isInCol[col][value] == false && this.isInRow[row][value] == false && this.subgridHasValue[row/SUBGRID_DIM][col/SUBGRID_DIM][value] == false);
    }

    /*
     * This is the key recursive-backtracking method.
     * Returns true if a solution has been found, and false otherwise.
     */
    private boolean solveRB(int n) {
        if (n==81) {
            return true;
        }
        if (this.valIsFixed[n/9][n%9]) {
            if (this.solveRB(n+1)) {
                return true;
            }
        } else {
            for (int val = 1; val<10; val++) {
                if (this.isSafe(n/9, n%9, val)) {
                    this.placeVal(val, n/9, n%9);
                    if (this.solveRB(n+1)) {
                        return true;
                    }
                    this.removeVal(val, n/9, n%9);
                }
            }
        }
        return false;
    }
    
    /*
     * public "wrapper" method for the private solve() method above.
     * Makes the initial call to that method, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    /*
     * place the specified value in the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.values[row][col] = val;
        this.subgridHasValue[row/SUBGRID_DIM][col/SUBGRID_DIM][val] = true;

        this.isInCol[col][val] = true;
        this.isInRow[row][val] = true;
    }
    
    /*
     * remove the specified value from the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.values[row][col] = 0;
        this.subgridHasValue[row/SUBGRID_DIM][col/SUBGRID_DIM][val] = false;
        
        this.isInCol[col][val] = false;
        this.isInRow[row][val] = false;

    }
    
    /*
     * Reads in a puzzle specification from the specified Scanner,
     * and uses it to initialize the state of the puzzle.  The
     * specification should consist of one line for each row, with the
     * values in the row specified as digits separated by spaces.  A
     * value of 0 should be used to indicate an empty cell.
     */ 
    public void readFrom(Scanner input) {
        for (int r = 0; r < DIM; r++) {
            for (int c = 0; c < DIM; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
    
    /*
     * Displays the current state of the puzzle.
     * You should not change this method.
     */
    public void display() {
        for (int r = 0; r < DIM; r++) {
            printRowSeparator();
            for (int c = 0; c < DIM; c++) {
                System.out.print("|");
                if (this.values[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.values[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        printRowSeparator();
    }
    
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < DIM; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }
}
