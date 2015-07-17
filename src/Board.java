


public class Board {
    private int[] board;
    private int[] goalBoard;
    private int boardDimension;
    private int size;


    public Board(int[][] blocks) {
        checkBlocksIsSquare(blocks);
        buildBoards(blocks);
    }           // construct a board from an N-by-N array of blocks

    private void buildBoards(int[][] blocks) {
        boardDimension = blocks.length;
        size = boardDimension * boardDimension;
        board = new int[size];
        goalBoard = new int[size];
        int boardIndex = 0;
        for (int i = 0; i < boardDimension; i ++) {
            for (int j = 0; j < boardDimension; j++) {
                board[boardIndex + j] = blocks[i][j];
                goalBoard[boardIndex + j] = boardIndex + j + 1;
            }
            boardIndex += boardDimension;
        }
    }

    private void checkBlocksIsSquare(int[][] blocks) {
        if (blocks.length != blocks[0].length)
            throw new UnsupportedOperationException("blocks array must be square");
    }
    // (where blocks[i][j] = block in row i, column j)

    public int dimension() {
        return getDimension();
    }           // board dimension N

    private int getDimension() {
        return boardDimension;
    }

    public int hamming() {
        return hammingDistance();
    }                   // number of blocks out of place

    private int hammingDistance() {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            if (board[i] != goalBoard[i] && board[i] != 0) {
                counter++;
            }
        }
        return counter;
    }

    public int manhattan() {
        return manhattanDistance();
    }                 // sum of Manhattan distances between blocks and goal

    private int manhattanDistance() {
        int sumDistance = 0;
        for (int i = 0; i < size; i++) {
            if (board[i] != goalBoard[i] && board[i] != 0) {
                sumDistance += currentManhattanDistance(i);
            }
        }
        return sumDistance;
    }

    private int currentManhattanDistance(int i) {
        int desiredIdx = board[i] - 1;
        int difference = (i < desiredIdx) ? (desiredIdx - i) : (i - desiredIdx);
        int vertical = difference / boardDimension;
        int horizontal = difference % boardDimension;
        return vertical+horizontal;
    }

    public boolean isGoal() {
        return isPuzzleSolved();
    }                // is this board the goal board?

    private boolean isPuzzleSolved() {
        for (int i = 0; i < size; i++) {
            if (board[i] != goalBoard[i]) {
                return false;
            }
        }
        return true;
    }

    public Board twin() {
        //finds the twin on first row
        return getTwinBoard();

    }                    // a board that is obtained by exchanging two adjacent blocks in the same row

    private Board getTwinBoard() {
        int counter = 0;
        int[][] board2D = new int[boardDimension][boardDimension];
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                board2D[i][j] = board[counter++];
            }
        }
        if (board[0] == 0) {
            swap(board2D, 0, 1,0,2);
        } else if (board[1] == 0) {
            swap(board2D, 0, 0, 0, 2);
        } else {
            swap(board2D, 0, 0, 0, 1);
        }
        return new Board(board2D);
    }

    private void swap(int[][] board2D, int i, int j,int r,int s) {
        int temp = board2D[r][s];
        board2D[r][s] = board2D[i][j];
        board2D[i][j] = temp;
    }


    public boolean equals(Object y) {
        return false;
    }        // does this board equal y?

    public Iterable<Board> neighbors() {
        return null;
    }     // all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        String dimensionString = boardDimension + "\n";
        s.append(dimensionString);
        int size = boardDimension * boardDimension;
        int boardIndex = 0;
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                s.append(String.format("%2d ", board[boardIndex + j]));
            }
            s.append("\n");
            boardIndex += boardDimension;
        }
        return s.toString();
    }               // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        // create initial board from file
//        In in = new In(args[0]);
//        int N = in.readInt();
//        int[][] blocks = new int[N][N];
//        for (int i = 0; i < N; i++)
//            for (int j = 0; j < N; j++)
//                blocks[i][j] = in.readInt();
//        int[][] blocks = new int[][]{
//                {0, 1, 3},
//                {4, 2, 5},
//                {7, 8, 6}
//        };
        int[][] blocks = new int[][]{
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
        };
        Board initial = new Board(blocks);

        //Print board and goalBoard
        System.out.println(initial);
        System.out.println("The hamming distance is: " + initial.hamming());
        System.out.println("The Manhattan distance is: " + initial.manhattan());
    } // unit tests (not graded)
}