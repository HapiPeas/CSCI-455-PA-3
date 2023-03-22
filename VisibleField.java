// Name: Benson Li
// USC NetID: 5489569472
// CS 455 PA3
// Spring 2023


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield). Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to action the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this opened square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   // Instance Variables
   private int[][] visibleField;
   private boolean[][] recursiveTracker;
   private  MineField mineField;
   private int numberMinesTotal;
   private int numberMineGuesses;
   private boolean coveredMine;

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the locations covered, no mines guessed, and the game
      not over.

      @param mineField  the minefield to use for this VisibleField
    */
   public VisibleField(MineField mineField) {
      // Instance variable as a reference to original mineField
      this.mineField = mineField;
      visibleField = new int[mineField.numRows()][mineField.numCols()];
      recursiveTracker = new boolean[getMineField().numRows()][mineField.numCols()];
      numberMinesTotal = mineField.numMines();
      coveredMine = true;

      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            visibleField[i][j] = COVERED;
         }
      }

   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      numberMinesTotal = mineField.numMines();
      numberMineGuesses = 0;
      coveredMine = true;

      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            recursiveTracker[i][j] = false;
         }
      }

      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            visibleField[i][j] = COVERED;
         }
      }

   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return mineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return visibleField[row][col];
   }

   
   /**
      Returns the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value will
      be negative if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return numberMinesTotal - numberMineGuesses;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      if (visibleField[row][col] == COVERED) {
         visibleField[row][col] = MINE_GUESS;
         numberMineGuesses++;
      }
      else if (visibleField[row][col] == MINE_GUESS) {
         visibleField[row][col] = QUESTION;
         numberMineGuesses--;
      }
      else {
         visibleField[row][col] = COVERED;
      }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      // Continue iff the selected square is still covered
      if (!this.isUncovered(row, col)) {
         // Case 1: Square to be uncovered is a mine (Game Loss)
         if (mineField.hasMine(row, col)) {
            visibleField[row][col] = EXPLODED_MINE;
            coveredMine = false;
            this.postGameStatusLoss();
            return false;
         }
         // Case 2: Square to be uncovered has at least one adjacent mine (Possible Game Win)
         else if (mineField.numAdjacentMines(row, col) > 0) {
            visibleField[row][col] = mineField.numAdjacentMines(row, col);
            if (this.isGameOver()) {
               this.postGameStatusWin();
            }
            return true;
         }
         // Case 3: Square to be uncovered is an empty square (Possible Game Win)
         else {
            dfsRecursion(row, col);
            if (this.isGameOver()) {
               this.postGameStatusWin();
            }
            return true;
         }
      }
      // Do nothing if square is already uncovered
      return true;
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game has ended
    */
   public boolean isGameOver() {
      // Case 1: A mine was uncovered (Game Loss)
      if (!coveredMine) {
         return true;
      }
      else {
         // Case 2: A non-mine square is still covered (Game continues)
         for (int i = 0; i < mineField.numRows(); i++) {
            for (int j = 0; j < mineField.numCols(); j++) {
               if ((!mineField.hasMine(i, j)) && (!this.isUncovered(i, j))) {
                  return false;
               }
            }
         }
         // Case 3: All non-mine squares are uncovered (Game win)
         return true;
      }
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      // Checks if square at (row, col) is in an uncovered states
      return (visibleField[row][col] != COVERED) && (visibleField[row][col] != MINE_GUESS)
              && (visibleField[row][col] != QUESTION);
   }

   /**
    * Returns a string representation of the current state of VisibleField
    * Most squares are represented with "[ ]" and a single character (number or symbol) to denote the type of square
    * An uncovered mine is displayed with "[BOOM]"
    *
    * @return - String representing the status of all squares in VisibleField (covered or uncovered)
    */
   public String toString() {
      String stringVisibleField = "";

      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            if (visibleField[i][j] == COVERED) {
               stringVisibleField += ("[ ]");
            }
            else if (visibleField[i][j] == MINE_GUESS) {
               stringVisibleField += ("[*]");
            }
            else if (visibleField[i][j] == QUESTION) {
               stringVisibleField += ("[?]");
            }
            else if (visibleField[i][j] == EXPLODED_MINE) {
               stringVisibleField += ("[BOOM]");
            }
            else if (mineField.numAdjacentMines(i,j) > 0) {
               stringVisibleField += ("[" + mineField.numAdjacentMines(i,j) + "]");
            }
            else if (visibleField[i][j] == MINE) {
               stringVisibleField += ("[M]");
            }
            else if (visibleField[i][j] == INCORRECT_GUESS) {
               stringVisibleField += ("[X]");
            }
            else {
               stringVisibleField += ("[-]"); // Empty
            }
         }
         stringVisibleField += "\n";
      }

      return stringVisibleField;
   }
 
   // Private Methods

   /**
    * On a game loss (uncovering a mine location), change status of all
    * (1) incorrectly marked mine locations to INCORRECT_GUESS (integer value 10)
    * (2) all mine locations that were not marked to MINE (integer value 9)
    */
   private void postGameStatusLoss() {
      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j <mineField.numCols(); j++) {
            // Case 1: Square is marked as being a mine BUT is not a mine
            if ((visibleField[i][j] == MINE_GUESS) && (!mineField.hasMine(i,j))) {
               visibleField[i][j] = INCORRECT_GUESS;
            }
            else if ((visibleField[i][j] != MINE_GUESS) && (!this.isUncovered(i,j)) && (mineField.hasMine(i,j))) {
               visibleField[i][j] = MINE;
            }
         }
      }
   }

   /**
    * On a game win (uncovering all non-mine locations), change status of all
    * (1) all mine locations that were not marked to MINE_GUESS (integer value -2)
    * There will not be any incorrectly marked squares
    */
   private void postGameStatusWin() {
      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            if ((visibleField[i][j] != MINE_GUESS) && (!this.isUncovered(i,j)) && (mineField.hasMine(i,j))) {
               visibleField[i][j] = MINE_GUESS;
            }
         }
      }
   }

   /**
    * Applies a recursive depth-first-search (DFS) algorithm in a graph (2D array)
    * Given a starting position in VisibleField represented by [row, col], recursively searches through the 2D array
    * for all adjacent empty squares until a border is reached
    *
    * The recursion searches in clockwise order starting from the square north of the given square, including diagonals
    *
    * A border can be either the bounds of the 2D array used by VisibleField or a square that is adjacent to a mine
    * If the method reaches the bounds of the 2D array, it returns from the stack
    * If the method reaches a square that is adjacent to a mine, it will uncover the square and then return from the
    * stack
    *
    * @param row - The row of VisibleField and MineField that the position is located at
    * @param col - The column of VisibleField and MineField that the position is located at
    */
   private void dfsRecursion(int row, int col) {
      // If the given location is within range of array and is an empty square
      if ((!mineField.inRange(row, col)) || (recursiveTracker[row][col])) {
         return;
      }
      else if (visibleField[row][col] == MINE_GUESS || visibleField[row][col] == QUESTION) {
         return;
      }
      else if ((mineField.numAdjacentMines(row, col) > 0) && (mineField.numAdjacentMines(row, col) < 9)) {
         visibleField[row][col] = mineField.numAdjacentMines(row, col);
         recursiveTracker[row][col] = true;
         return;
      }
      else {
            visibleField[row][col] = 0;
            recursiveTracker[row][col] = true;
      }


      dfsRecursion(row - 1, col); // Move up
      dfsRecursion(row - 1, col + 1); // Move up and right
      dfsRecursion(row, col + 1); // Move right
      dfsRecursion(row + 1, col + 1); // Move down and right
      dfsRecursion(row + 1, col); // Move down
      dfsRecursion(row + 1, col - 1); // Move down and left
      dfsRecursion(row, col - 1); // Move left
      dfsRecursion(row - 1, col - 1); // Move up and left
   }
}
