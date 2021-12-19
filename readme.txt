/* *****************************************************************************
 *  Name: Julian Ceja
 *  NetID:    
 *  Precept:  
 *
 *  Partner Name: Dakota Jackson
 *  Partner NetID:      
 *  Partner Precept:    
 *
 *  Hours to complete assignment (optional):
 *
 **************************************************************************** */

Programming Assignment 4: Slider Puzzle


/* *****************************************************************************
 *  Explain briefly how you represented the Board data type.
 **************************************************************************** */
We created a Board object from user input with a given nxn 2D int array. We calculated
the manhattan and hamming distances in the constructor and stored those in variables.
We also store the location of the blank tile with two int variables, one for its row
and one for its column.



/* *****************************************************************************
 *  Explain briefly how you represented a search node
 *  (board + number of moves + previous search node).
 **************************************************************************** */
We created an inner class inside the Board class. We gave it reference to its previous
search node. We also gave it the number of moves made to get to that search node and
a priority for that search node which is moves + manhattan distance.




/* *****************************************************************************
 *  Explain briefly how you detected unsolvable puzzles.
 *
 *  What is the order of growth of the running time of your isSolvable()
 *  method in the worst case as function of the board size n? Recall that with
 *  order-of-growth notation, you should discard leading coefficients and
 *  lower-order terms, e.g., n log n or n^3.
 **************************************************************************** */

Description:
We first look at the board in row major order and count the number if inversions.
If n is odd and the number of inversions is odd then the board is unsolvable. If n
is even and the number of inversions plus the row of the blank tile is even then the
board is unsolvable.


Order of growth of running time:
We first convert the 2D array into a 1D array which is n^2. We then count the number
of inversions by checking for every number, all the numbers to its right are less. This
takes ( n^2 - 2 )( n^2 - 1 ) / 2 which ~n^4 /2 . This would lead to a time complexity of
n^2 + n^4 / 4 which simplifies to O( n^4 ).


/* *****************************************************************************
 *  For each of the following instances, give the minimum number of moves to
 *  solve the instance (as reported by your program). Also, give the amount
 *  of time your program takes with both the Hamming and Manhattan priority
 *  functions. If your program can't solve the instance in a reasonable
 *  amount of time (say, 5 minutes) or memory, indicate that instead. Note
 *  that your program may be able to solve puzzle[xx].txt even if it can't
 *  solve puzzle[yy].txt and xx > yy.
 **************************************************************************** */


                 min number          seconds
     instance     of moves     Hamming          Manhattan
   ------------  ----------   ----------        ----------
   puzzle28.txt   28 moves    ~0.901 seconds  	~0.234 seconds
   puzzle30.txt   30 moves    ~1.838 seconds	~0.261 seconds
   puzzle32.txt   32 moves    Can't Solve      	~0.806 seconds
   puzzle34.txt   34 moves    Can't Solve	~0.469 seconds
   puzzle36.txt   36 moves    Can't Solve      	~2.376 seconds
   puzzle38.txt   38 moves    Can't Solve	~1.883 seconds
   puzzle40.txt   40 moves    Can't Solve      	~0.659 seconds
   puzzle42.txt   42 moves    Can't Sovle       ~1.756 seconds



/* *****************************************************************************
 *  If you wanted to solve random 4-by-4 or 5-by-5 puzzles, which
 *  would you prefer: a faster computer (say, 2x as fast), more memory
 *  (say 2x as much), a better priority queue (say, 2x as fast),
 *  or a better priority function (say, one on the order of improvement
 *  from Hamming to Manhattan)? Why?
 **************************************************************************** */
A better priority function because a better ( more efficient ) algorithm will
perform better than a super computer.





/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
Using the hamming function will result in a drastic decrease in performance.
The RNG part of the test client for Solver.java is limited to an n size capped at
four due to performance issues with completely random numbers ( thus a random amount
of minimum moves which can be extremely high ).


/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
N / A




/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */
The Solver would not find the goal board for certain puzzles and it would run
until the Dr. Java crashed. There was a bug in our SearchNode class that was destroying
our performance due to us overlooking a variable that was not initialized correctly.


/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
Julian contributed by outlining the API and calculating the manhattan and hamming distances.
Dakota contributed by finding an efficient way to calculate the number of inversions in the board
and correctly implemented the isSolvable method. We both worked together on the overarching algorithm
to solve the 8-puzzle. We were on call and screen sharing throughout 90% of the project and contributed
equally to the solution.






/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */
We learned a great deal by putting into use the information that was presented to us
during the class. We also got to experience what is like to work as a team to effectively
balance ideas between each other.