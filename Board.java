/******************************************************************************
 * Compilation:  javac Board.java
 * ----------------------------------------------------------------------------
 * Program that outlines the Board object for "Solver.java" to use in the 
 * slider puzzle problem solving program. It creates a Board object from a 2D
 * array and computes the Hamming and Manhattan distances within the 
 * constructor (while also finding the blank tile). As a key component, the
 * program is also able to find the neighbors of the Board object.
 * 
 * Author:@Julian Ceja, @Dakota Jackson
 * ***************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;

public class Board {
  
  private int[][] tiles; // 2D int array representing the board
  private int size; // Size of n
  public int blankRow, blankCol; // Row and column position of the blank tile
  private int hamming; // Number of tiles in the wrong position
  private int manhattan; // Number of total moves needed to solve the puzzle
  
  /* Creates Board object from the 2D tiles array
   *  -Checks to make sure the board is both not null nor a 1x1 board
   *  -for loop finds the location of the blank tile (indicated by 0) for inversion process
   *  -also does [insert explanation after finding out]
   * 
   * @param 2D int array "tiles" representation of the Board
   */
  public Board( int[][] tiles ){
    if ( tiles == null ) throw new IllegalArgumentException();
    if ( tiles.length < 2 ) throw new IllegalArgumentException();
    
    // Finds n and creates a copy of the given 2D array
    size = tiles.length;
    this.tiles = new int[size][size];
    
    // Initializes hamming and manhattan distances
    hamming = 0;
    manhattan = 0;
    for ( int i = 0, x = 1; i < size; i++ ) {
      for ( int j = 0; j < size; j++, x++ ) {
        this.tiles[i][j] = tiles[i][j];
        
        // Finds blank tile and saves its position
        if ( tiles[i][j] == 0 ) {
          blankRow = i;
          blankCol = j;
        }
        else {
        
          // If a tile is out of place, increase hamming distance
          if ( tiles[i][j] != x ) hamming++;
        
          // Finds the correct position of a tile, if its out of place
          // it finds the number of moves needed to get it there
          int correctRow = ( tiles[i][j] - 1 ) / size;
          int correctCol = ( tiles[i][j] - 1 ) % size;
          if ( correctRow != i ) manhattan += Math.abs( i - correctRow );
          if ( correctCol != j ) manhattan += Math.abs( j - correctCol );
        }
      }
    }
  }
  
  /* toString() function to create a string representation of the Board object
   * -implements StringBuilder for creating the string 
   * -checks to see if the boardString has already been created before creating a new one
   *    -if created, saves time by just returning the already created one
   *    -if not created, runs a nested for loop to created the string representation of the board
   * 
   * @return String value "boardString" from created string representation of the board
   */
  public String toString() {
    StringBuilder board = new StringBuilder( size + "\n" );
    for ( int i = 0; i < size; i++ ) {
      for ( int j = 0; j < size; j++ ) {
        board.append( tiles[i][j] );
        if ( j < size - 1 ) board.append( " " );
      }
      if ( i < size - 1 ) board.append( "\n" );
    }
    return board.toString();
  }
  
  /* retrieves the int value at the locaction tiles[row][col]
   * -if statement checks to make sure input was valid within bounds of board size 
   *
   * @param An integer value "row" and an integer value "col" 
   * @return an integer value indicating the tile at the inputted location on the board
   */
  public int tileAt( int row, int col ) {
    if ( row < 0 || row >= size || col < 0 || col >= size ) throw new IllegalArgumentException();
    return tiles[row][col];
  }
  
  /* Returns the size of n
   * @return int value
   */
  public int size() {
    return size;
  }
  
  /* Returns the hamming distance
   * @return int value
   */
  public int hamming() {
    return hamming;
  }
  
  /* Returns the manhattan distance
   * @return int value
   */
  public int manhattan() {
    return manhattan;
  }
  
  /* Checks if board is the goal board
   * @return int value
   */
  public boolean isGoal() {
    return hamming == 0;
  }
  
  /* Compares to Board objects to see if they are equal
   * - Calls the toString function of both boards for the comparison
   * - Maintains the reflexive, symmetric, and transitive properties
   *
   * @param an Object (Board) to be compared to
   * @return Boolean value (dependant on two board's equalness)
   */
  @Override
  public boolean equals( Object y ) {
    if ( y == null ) return false;
    if ( y == this ) return true;
    if ( y.getClass() != getClass() ) return false;
    
    Board other = ( Board )y;
    if ( other.size() != size ) return false;
    if ( other.hamming() != hamming || other.manhattan() != manhattan )
      return false;
    
    for ( int i = 0; i < size; i++ ) {
      for ( int j = 0; j < size; j++ ) {
        if ( tiles[i][j] != other.tileAt( i, j ) ) return false;
      }
    }
    return true;
  }
  
  /* Finds the neighbors(!) of the current board 
   *   (!) neighbors = boards within 1 hamming distance of current board
   * 
   * @return Iterable Bag of Board
   */
  public Iterable<Board> neighbors() {
    Bag<Board> neighbors = new Bag<Board>();
    
    // Depending on the position of the blank tile
    // it finds the possible moves and adds the resulting neighboring Boards
    // to the bag
    if ( blankRow != size - 1 ) neighbors.add( moveBlankBottom() );
    if ( blankRow != 0 ) neighbors.add( moveBlankTop() );
    if ( blankCol != size - 1 ) neighbors.add( moveBlankRight() );
    if ( blankCol != 0 ) neighbors.add( moveBlankLeft() );
    
    return neighbors;
  }
  
  /* checks to see if the board is solvable using knownledge of inversions (!)
   *  (!) Inversions: when some i < j, but i comes after j on the board 
   *        -1. board is odd - solvable if inversions != odd
   *        -2. board is even - solvable if (inversions + row# location of blank square != even)
   * 
   * @return Boolean value indicating if board is solvable
   */
  public boolean isSolvable() {
      
    //calls toRowMajorOrder() function to create 1D array representation of current board
    int[] rowMajorOrder = toRowMajorOrder();
    int inversions = 0;
    
    /* for loop to check for inversion and keep count of how many there are 
     *  -improvements: doesnt run inner loop till it finds a tile out of place
     */
    for ( int i = 0; i < rowMajorOrder.length; i++ ) {
      //if ( rowMajorOrder[i] != i + 1 ) {
        for ( int j = i + 1; j < rowMajorOrder.length; j++ ) {
          if ( rowMajorOrder[i] > rowMajorOrder[j] ) {
            //StdOut.println( rowMajorOrder[i] + "-" + rowMajorOrder[j] );
            inversions++;
          }
        }
      //}
    }
    // If n is odd
    if ( size % 2 == 1 ) {
      return inversions % 2 == 0 ? true : false;
    }
    
    // If n is even
    return ( inversions + blankRow ) % 2 == 1 ? true : false;
  }
  
  /* creates a 1D array representing row major order of the board
   * @return int array 
   */
  private int[] toRowMajorOrder() {
    int[] rowMajorOrder = new int[size * size - 1];
    for ( int i = 0, x = 0; i < size; i++ ) {
      for ( int j = 0; j < size; j++ ) {
        if ( tiles[i][j] != 0 ) rowMajorOrder[x++] = tiles[i][j];
      }
    }
    return rowMajorOrder;
  }
  
  /* Creates a new Board resulting from swapping the current blank tile
   * with tile at its right
   * @return Board representing the "right" neighbor of the current Board
   */
  private Board moveBlankRight() {
    swapWithRightTile( blankRow, blankCol );
    Board neighbor = new Board( tiles );
    swapWithRightTile( blankRow, blankCol );
    return neighbor;
  }
  
  /* Creates a new Board resulting from swapping the current blank tile
   * with tile at its left
   * @return Board representing the "left" neighbor of the current Board
   */
  private Board moveBlankLeft() {
    swapWithLeftTile( blankRow, blankCol );
    Board neighbor = new Board( tiles );
    swapWithLeftTile( blankRow, blankCol );
    return neighbor;
  }
  
  /* Creates a new Board resulting from swapping the current blank tile
   * with tile underneath it
   * @return Board representing the "bottom" neighbor of the current Board
   */
  private Board moveBlankBottom() {
    swapWithBottomTile( blankRow, blankCol );
    Board neighbor = new Board( tiles );
    swapWithBottomTile( blankRow, blankCol );
    return neighbor;
  }
  
  /* Creates a new Board resulting from swapping the current blank tile
   * with tile on top of it
   * @return Board representing the "top" neighbor of the current Board
   */
  private Board moveBlankTop() {
    swapWithTopTile( blankRow, blankCol );
    Board neighbor = new Board( tiles );
    swapWithTopTile( blankRow, blankCol );
    return neighbor;
  }
  
  /* Swaps the given tile with the tile on the right
   * @param an integer value "row" and an integer value "col"
   */
  private void swapWithRightTile( int row, int col ) {
    int temp = tiles[row][col];
    tiles[row][col] = tiles[row][col + 1];
    tiles[row][col + 1] = temp;
  }
  
  /* Swaps the given tile with the tile on the left
   * @param an integer value "row" and an integer value "col"
   */
  private void swapWithLeftTile( int row, int col ) {
    int temp = tiles[row][col];
    tiles[row][col] = tiles[row][col - 1];
    tiles[row][col - 1] = temp;
  }
  
  /* Swaps the given tile with the tile underneath it
   * @param an integer value "row" and an integer value "col"
   */
  private void swapWithBottomTile( int row, int col ) {
    int temp = tiles[row][col];
    tiles[row][col] = tiles[row + 1][col];
    tiles[row + 1][col] = temp;
  }
  
  /* Swaps the given tile with the tile on top
   * @param an integer value "row" and an integer value "col"
   */
  private void swapWithTopTile( int row, int col ) {
    int temp = tiles[row][col];
    tiles[row][col] = tiles[row - 1][col];
    tiles[row - 1][col] = temp;
  }
  
  //returns the integer value representing the row of the blank square
  public int getBlankRow() {
    return blankRow;
  }
  
  //returns the integer value representing the column of the blank square
  public int getBlankCol() {
    return blankCol;
  }
  
  /* Test driver
   */
  public static void main( String[] args ) {
    int[][] a = {
      { 1, 2, 3 },
      { 4, 5, 6 },
      { 7, 0, 8 }
    };
    Board b = new Board( a );
    
    int[][] c = {
      { 1, 2, 3 },
      { 4, 5, 6 },
      { 7, 0, 8 }
    };
    
    Board d = new Board( c );
    
    StdOut.println( b.hashCode() );
    StdOut.println( d.hashCode() );
  }
  
}