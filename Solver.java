/******************************************************************************
 * Compilation:  javac Solver.java
 * Dependencies: Board.java
 * ----------------------------------------------------------------------------
 * Program takes a formatted file specifying the 'n' size of the n*n 
 * slider puzzle (with 2 < n < 32,768) and the order of the tiles in the slider 
 * puzzle (0 - (n-1)). It then determines if the specified slider puzzle is a 
 * solvable puzzle and if it is, then determines the amount of moves it takes 
 * to solve and the path it takes to get to the solution (outputting the path).
 * 
 * Author:@Julian Ceja, @Dakota Jackson
 * ***************************************************************************/

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdRandom;

public class Solver {
  
  private SearchNode goal;
  private Stack<Board> solution;
  
  /* Method for solving the board from its initial state
   *  -While loop runs until current board.isGoal() == true
   *  -improvement made by only adding boards that have yet to be seen
   * 
   * @param a Board object representing the initial board
   */
  public Solver( Board initial ) {
      //checks to see if the initial board is solvable
    if ( !initial.isSolvable() ) throw new IllegalArgumentException();
    
  /*creates minimum priority queue for storing (and sorting by priority) 
   * SearchNode objects
   */
    MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>( new Comparator<SearchNode>() {
      public int compare( SearchNode x, SearchNode y ) {
        return x.priority() - y.priority();
      }
     }
    );
    
    //sets the initial searchNode (with null as initial "previous" variable)
    SearchNode current = new SearchNode( initial, 0, null );
    minPQ.insert( current );
    
    /* Loop for retrieving (and removing) the lowest priority SearchNode from 
     * the PQ (checking to see if it is at the Goal Board yet) and getting the
     * neighbors of the current board object to place in PQ.
     *  -Improvement: Will not add neighbor's "grandparent" equivalent neighbor
     */
    while ( !minPQ.isEmpty() ) {
      current = minPQ.delMin();
      if ( current.board.isGoal() ) {
        goal = current;
        break;
      }
      for ( Board n : current.board().neighbors() ) {
        if ( current.previous == null || !current.previous().board().equals( n ) ) 
          minPQ.insert( new SearchNode( n, current.movesMade() + 1, current ) );
      }
    }
  }
    
  //returns integer variable "moves"
  public int moves(){
    return goal.movesMade();
  }
  
  //returns an Iterable of board objects representing solution path taken
  public Iterable<Board> solution() {
    if ( solution != null ) return solution;
    solution = new Stack<Board>();
    while ( goal != null ) {
      solution.push( goal.board() );
      goal = goal.previous();
    }
    return solution;
  }
  
  /* Search node object that has 3 variables: 
   *  -Board object, integer variable for amount of moves made so far, and
   *   SearchNode object housing pointer to previous SearchNode
   * 
   */ 
  private class SearchNode {
    
    private Board board;
    private int movesMade;
    private SearchNode previous;
    
    //SearchNode constructor 
    SearchNode( Board board, int movesMade, SearchNode previous ) {
      this.board = board;
      this.movesMade = movesMade;
      this.previous = previous;
    }
    
    //returns int variable for priority
    public int priority() {
      return movesMade + board.manhattan();
    }
    
    //returns int variable for movesMade
    public int movesMade() {
      return movesMade;
    }
    //returns SearchNode object from which current SearchNode follows
    public SearchNode previous() {
      return previous;
    }
    
    //returns the Board object
    public Board board() {
      return board;
    }
  }
  
  //testing client 
  public static void main( String[] args ) {
    /*
    if ( args.length != 1 ) {
      StdOut.println( "Invalid arguments" );
      System.exit( 0 );
    }
    */
      
    Stopwatch timer = new Stopwatch();
      
   //Option 1: read from file
    In input = new In( "puzzle40.txt" );
    int n = input.readInt();
    int[][] tiles = new int[n][n];
    for ( int i = 0; i < n; i++ ) {
      for ( int j = 0; j < n; j++ ) {
        tiles[i][j] = input.readInt();
      }
    }
    
    //Option 2: RNG n input from 2 - 4 (for testing/debugging)
    /*
    //Uniform: returns random number from [a, b)
    int n = StdRandom.uniform(2,5);
    int testArray [] = new int [n*n];
    
    //make testArray elements unique from 0 - n-1
    for(int i = 0; i < testArray.length; i++) {
        testArray[i] = i;
    }  
    //shuffles the array
    StdRandom.shuffle(testArray);
    //StdOut.println(Arrays.toString(testArray)); 
    
    int[][] tiles = new int[n][n];
    
    //converts the 1D array to 2D for correct constructor input
    for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles.length; j++) {
         tiles[i][j] = testArray[(j*n) + i];   
        }
    }

    //StdOut.println(Arrays.deepToString(tiles));
    */
    
    Board board = new Board( tiles );
    if ( board.isSolvable() ) {
      Solver puzzle = new Solver( board );
      StdOut.println( puzzle.moves() + " moves" );
      for ( Board i : puzzle.solution() ) {
        StdOut.println( i );
      }
    }
    else StdOut.println( "No solution" );
    
    double time = timer.elapsedTime();
    StdOut.print("Program running time: " + time + "\n");
  }
}