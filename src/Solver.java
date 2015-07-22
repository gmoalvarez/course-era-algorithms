import java.util.Comparator;
public class Solver {

    private MinPQ<SearchNode> puzzlePQ;
    private MinPQ<SearchNode> twinPQ;
    private boolean isSolved;
    private int moves;
    private Stack<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int numberOfMoves;
        SearchNode previous;
        int manhattan;

        @Override
        public int compareTo(SearchNode o) {
            int thisManhattan = this.manhattan;
            int thisMoves = this.numberOfMoves;
            int thatManhattan = o.manhattan;
            int thatMoves = o.numberOfMoves;
            int thisPriority = thisManhattan + thisMoves;
            int thatPriority = thatManhattan + thatMoves;
            return thisPriority - thatPriority;
        }
    }

    public Solver(Board initial) {
        isSolved = false;
        moves = -1;
        if (initial == null) throw new NullPointerException("Cannot be null");
        //Create search node out of initial board
        SearchNode currentNode = new SearchNode();
        //Initialize number of moves to 0, null previous, manhattan and hamming
        currentNode.board = initial;
        currentNode.numberOfMoves = 0;
        currentNode.previous = null;
        currentNode.manhattan = initial.manhattan();
        //Create twin board
        Board twinBoard = initial.twin();
        //Create search node out of twin board
        SearchNode twinNode = new SearchNode();
        twinNode.board = twinBoard;
        twinNode.numberOfMoves = 0;
        twinNode.previous = null;
        twinNode.manhattan = twinNode.board.manhattan();
        //insert the initial search node(initial board, 0 numberOfMoves,null previous searchNode into Priority Queue(min)
        puzzlePQ = new MinPQ<>();
        puzzlePQ.insert(currentNode);
        //repeat the previous step for the twin board
        twinPQ = new MinPQ<>();
        twinPQ.insert(twinNode);
        //begin loop until puzzle or twin is solved

        while (!currentNode.board.isGoal() && !twinNode.board.isGoal()) {
            //delete from the priority queue the search node with the minimum priority
            currentNode = puzzlePQ.delMin();
            //insert onto the priority queue all neighboring search nodes
            for (Board neighborBoard : currentNode.board.neighbors()) {
                //if neighbor is the same as previous board, skip it
                //*critical optimization*check to make sure this board is not the
                //same as previous board before placing on priority queue
                if (currentNode.previous != null && neighborBoard.equals(currentNode.previous.board))
                    continue;
                //otherwise...
                //create a new search node
                SearchNode tempNode = new SearchNode();
                //insert board into search node
                tempNode.board = neighborBoard;
                //insert number of moves to get to this search node (previous +1)
                tempNode.numberOfMoves = currentNode.numberOfMoves + 1;
                //previous should be currentNode
                tempNode.previous = currentNode;
                //calculate hamming or manhattan distance
                tempNode.manhattan = tempNode.board.manhattan();
                //place search node into priority queue
                puzzlePQ.insert(tempNode);
            }

            //repeat the above step for twin Node
            twinNode = twinPQ.delMin();
            for (Board twinNeighbor : twinNode.board.neighbors()) {
                //if neighbor is the same as previous board, skip it
                //*critical optimization*check to make sure this board is not the
                //same as previous board before placing on priority queue
                if (twinNode.previous != null && twinNeighbor.equals(twinNode.previous.board))
                    continue;
                //otherwise...
                //create a new search node
                SearchNode tempTwin = new SearchNode();
                //insert board into search node
                tempTwin.board = twinNeighbor;
                //insert number of moves to get to this search node (previous +1)
                tempTwin.numberOfMoves = twinNode.numberOfMoves + 1;
                //previous should be currentNode
                tempTwin.previous = twinNode;
                //calculate hamming or manhattan distance
                tempTwin.manhattan = tempTwin.board.manhattan();
                //place search node into priority queue
                twinPQ.insert(tempTwin);
            }
        }
//        //check to see if it was the initial or twin board that led to a solution

        if (currentNode.board.isGoal()) {
            isSolved = true;
            moves = currentNode.numberOfMoves;
            solution = new Stack<>();
            while (currentNode != null) {
                solution.push(currentNode.board);
                currentNode = currentNode.previous;
            }
        } else if (twinNode.board.isGoal()) {
            isSolved = false;
        } else {
            StdOut.println("The impossible occurred");
        }

    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return isSolved;
    }            // is the initial board solvable?

    public int moves() {
        if (isSolvable())
            return moves;
        return -1;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solution;
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
// create initial board from file
//        int[][] blocks = new int[][]{
//                {8, 1, 3},
//                {4, 0, 2},
//                {7, 6, 5}
//        };
//
//        int[][] blocks2 = new int[][]{
//                {0, 1, 3},
//                {4, 2, 5},
//                {7, 8, 6}
//        };//solved in 4 moves
//        int[][] blocks3 = new int[][]{
//                {1, 0},
//                {2, 3},
//        };
//        int[][] blocks4 = new int[][]{
//                {1,2,3,4},
//                {5,6,7,8},
//                {9,10,11,12},
//                {13,15,14,0},
//        };//unsolvable
//
//        Board board1 = new Board(blocks);
//        Board board2 = new Board(blocks2);
//        Board board3 = new Board(blocks3);
//        Board board4 = new Board(blocks4);
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)
}