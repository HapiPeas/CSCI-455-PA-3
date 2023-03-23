// Name: Benson Li
// USC NetID: 5489569472
// CS 455 PA3
// Spring 2023

import java.util.Random;

/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
    /**
     * Representation Invariants
     * 1. Each cell of the 2D boolean array representing the minefield is always either 'true' or 'false'
     *      - This is always true in Java and doesn't need to be checked
     * 2. minefieldRows is always equal to minefield.length
     * 3. minefieldColumns is always equal to minefield[0].length
     */

    // Instance Variables
   private boolean[][] minefield;
   private int currentNumberMines;
   private final int minefieldRows;
   private final int minefieldColumns;
   
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will correspond to the number of 'true' values in mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
       minefieldRows = mineData.length;
       minefieldColumns = mineData[0].length;
       minefield = new boolean[minefieldRows][minefieldColumns];

      // Loop through 2D array and count number of mines (true values), assign to currentNumberMines
      for (int i = 0; i < minefieldRows; i++) {
          for (int j = 0; j < minefieldColumns; j++) {
              if (mineData[i][j]) {
                  currentNumberMines++;
              }
              minefield[i][j] = mineData[i][j];
          }
      }

      assert isValidMineField();

   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      minefield = new boolean[numRows][numCols];
      minefieldRows = numRows;
      minefieldColumns = numCols;
      currentNumberMines = numMines;

      assert isValidMineField();
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
       // Sets minefield to a new 2D, same-sized boolean array with no mines (default value is false)
      minefield = new boolean[minefieldRows][minefieldColumns];

      Random randomNumber = new Random();

      int count = 0;

      // Places mines in minefield until count is equal to number of given mines
      while (count < currentNumberMines) {
          // Obtain a new random number in range of valid rows
          int mineRow = randomNumber.nextInt(minefieldRows);
          // Obtain a new random number in range of valid columns
          int mineColumn = randomNumber.nextInt(minefieldColumns);

          if (mineRow != row) { // If not the same row as input row, place a mine and increment
              minefield[mineRow][mineColumn] = true;
              count++;
          }
          else if (mineColumn != col) { // If same row, but not same column, place a mine and increment
              minefield[mineRow][mineColumn] = true;
              count++;
          }
          else { // If same row and same column, continue to next loop without incrementing count
              continue;
          }
      }

       assert isValidMineField();

   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
       // Sets minefield to a new 2D, same-sized boolean array with no mines (default value is false)
       minefield = new boolean[minefieldRows][minefieldColumns];

       assert isValidMineField();
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int adjacentMines = 0;
      int rowLimit = 2;
      int columnLimit = 2;

      // Checks valid adjacent cells given current cell at [row][col]
       // Checks the three cells above current cell from left to right
       // Checks cell to the left of current cell, then cell to the right of current cell
       // Checks the three cells below current cell from left to right
      for (int i = -1; i < rowLimit; i++) {
          for (int j = -1; j < columnLimit; j++) {
              // Only checks if adjacent cell is a valid cell of minefield
              if ((inRange(row + i, col + j))) {
                  // Skip current cell
                  if ((i == 0) && (j == 0)) {
                      continue;
                  }
                  else {
                      adjacentMines += mineChecker(row + i, col + j);
                  }
              }
          }
      }

      assert isValidMineField();
      return adjacentMines;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      if ((row < 0) || (row >= minefieldRows)) {
          assert isValidMineField();
          return false;
      }
      if ((col < 0) || (col >= minefieldColumns)) {
          assert isValidMineField();
          return false;
      }
      assert isValidMineField();
      return true;
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return minefieldRows;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return minefieldColumns;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
       return minefield[row][col];
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.

    * @return - maximum number of mines allowed for minefield
    */
   public int numMines() {
      return currentNumberMines;
   }

    /**
     * Returns a string representation of the current state of MineField
     * A square with a mine is represented by "[T]" for true
     * A square without a mine represented by "[F]" for false
     *
     * @return - String representing the status of all squares in MineField
     */
   public String toString() {
       String stringMinefield = "";

       for (int i = 0; i < minefieldRows; i++) {
           for (int j = 0; j < minefieldColumns; j++) {
               if (minefield[i][j] == true) {
                   stringMinefield += ("[T]");
               }
               else {
                   stringMinefield += ("[F]");
               }
           }
           stringMinefield += "\n";
       }

       assert isValidMineField();
       return stringMinefield;
   }
   
   // Private Methods

    /**
     * Checks if a given a position of the minefield, in terms of the row and column values, is a mine
     *
     * @param row - row of the position to be checked in the minefield
     * @param column - column of the position to be checked in the minefield
     * @return -  1 if it is a mine,  0 if it is not a mine
     */
   private int mineChecker(int row, int column) {
       if (minefield[row][column]) {
           assert isValidMineField();
           return 1;
       }
       assert isValidMineField();
       return 0;
   }

    /**
     * Returns true iff the MineField object is in a valid state.
     * (See representation invariant comment for more details.)
     */
    private boolean isValidMineField() {
        return (minefieldRows == minefield.length) && (minefieldColumns == minefield[0].length);
    }
         
}

