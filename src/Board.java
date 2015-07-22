import java.util.Iterator;

public class Board {
    private int[] board;
    private int[] goalBoard;
    private int boardDimension;
    private int size;


    public Board(int[][] blocks) {
        if(blocks==null) throw new NullPointerException("blocks cannot be null");
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
        goalBoard[size - 1] = 0;
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
        int sumDistance = 0;
        for (int i = 0; i < size; i++) {
            if (board[i] != goalBoard[i] && board[i] != 0) {
                sumDistance += currentManhattanDistance(i);
            }
        }
        return sumDistance;    }                 // sum of Manhattan distances between blocks and goal

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
        //find zero index
        int zeroIndex = findzero(board);
        //If zero is in the first row, swap two first two in second row
        if (zeroIndex < boardDimension) {
            swap2D(board2D, 1, 0, 1, 1);
        } else {        //else, swap two adjacent in first row
            swap2D(board2D,0,0,0,1);
        }
        return new Board(board2D);
    }

    private void swap2D(int[][] board2D, int i, int j, int r, int s) {
        int temp = board2D[r][s];
        board2D[r][s] = board2D[i][j];
        board2D[i][j] = temp;
    }

    private void swap(int[] board, int i, int j) {
        int temp = board[j];
        board[j] = board[i];
        board[i] = temp;
    }

    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass())
            return false;
        return areBoardsEqual(y);
    }        // does this board equal y?

    private boolean areBoardsEqual(Object y) {
        Board that = (Board) y;
        for (int i = 0; i < size; i++) {
            if (this.board[i] != that.board[i])
                return false;
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        //To find all of the neighbors, we perform the following:
        //1-Find the index of 0
        int zeroIndex = findzero(board);
        assert zeroIndex < size && zeroIndex >= 0;
        int leftIndex = zeroIndex - 1;
        int rightIndex = zeroIndex + 1;
        int aboveIndex = zeroIndex - boardDimension;
        int belowIndex = zeroIndex + boardDimension;
        int[][] tempBlocks;
        //2-If 0 is in the top middle, we swap with left,right,down, and add these to neighbors
        //3-If 0 is in the top left, we swap with right,down, and add these to neighbors
        //4-If 0 is in the top right, we swap with left,down, and add these to neighbors
        if (zeroIndex < boardDimension) {//Top
            swap(board, zeroIndex, belowIndex);
            //create new 2D array with new neighbor
            tempBlocks = oneDtoTwoD(board, boardDimension);
            //create new Board made out of neighbor
            //enque neighbor
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, belowIndex);
            if (zeroIndex == 0) {//left
                swap(board, zeroIndex, rightIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, rightIndex);
            } else if (zeroIndex == boardDimension - 1) {//right
                //swap left
                swap(board, zeroIndex, leftIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, leftIndex);
            } else {//middle
                swap(board, zeroIndex, rightIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, rightIndex);
                swap(board, zeroIndex, leftIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, leftIndex);
            }
        }
        //5-If 0 is in the bottom middle, we wap with left,right,up, and add these to neighbors
        //6-If 0 is in the bottom left,we swap with right,up, and add these to neighbors
        //7-If 0 is in the bottom right, we swap with left,up, and add these to neighbors
        else if (zeroIndex > size - boardDimension - 1) {//Bottom
            swap(board, zeroIndex, aboveIndex);
            tempBlocks = oneDtoTwoD(board, boardDimension);
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, aboveIndex);
            if (zeroIndex == size - boardDimension) {//left
                swap(board, zeroIndex, rightIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, rightIndex);
            }else if (zeroIndex == size - 1) {//right
                swap(board, zeroIndex, leftIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, leftIndex);
            } else {//middle
                swap(board, zeroIndex, leftIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, leftIndex);
                swap(board, zeroIndex, rightIndex);
                tempBlocks = oneDtoTwoD(board, boardDimension);
                neighbors.enqueue(new Board(tempBlocks));
                swap(board, zeroIndex, rightIndex);
            }
        }//8-If 0 is in the middle area, we swap with left,right,up,down, and add these to neighbors
        //9-If 0 is in the middle left, we swap with right,up,down, and add these to neighbors
        //10-If 0 is in the middle right, we swap with left,up,down, and add these to neighbors
        else {//Middle
            swap(board, zeroIndex, leftIndex);
            tempBlocks = oneDtoTwoD(board, boardDimension);
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, leftIndex);
            swap(board, zeroIndex, rightIndex);
            tempBlocks = oneDtoTwoD(board, boardDimension);
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, rightIndex);
            swap(board, zeroIndex, aboveIndex);
            tempBlocks = oneDtoTwoD(board, boardDimension);
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, aboveIndex);
            swap(board, zeroIndex, belowIndex);
            tempBlocks = oneDtoTwoD(board, boardDimension);
            neighbors.enqueue(new Board(tempBlocks));
            swap(board, zeroIndex, belowIndex);
        }
        return neighbors;
    }     // all neighboring boards

    private int findzero(int[] board) {
        for (int i = 0; i < size; i++) {
            if (board[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int[][] oneDtoTwoD(int[] board, int boardDimension) {
        int[][] temp = new int[boardDimension][boardDimension];
        int idx = 0;
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                temp[i][j] = board[idx++];
            }
        }
        return temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        String dimensionString = boardDimension + "\n";
        s.append(dimensionString);
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

        int[][] blocks2 = new int[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
        int[][] blocks3 = new int[][]{
                {1, 0},
                {2, 3},
        };
        int[][] blocks4 = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,15,14,0},
        };//unsolvable

        int[][] blocks5 = new int[][]{
                {5, 8, 7},
                {1, 4, 6},
                {3, 0, 2}
        };
        Board board1 = new Board(blocks);
        Board board2 = new Board(blocks2);
        Board board3 = new Board(blocks3);
        Board board4 = new Board(blocks4);
        Board board5 = new Board(blocks5);


        //Print board1 and goalBoard
        System.out.println(board1);
        System.out.println("board1 is: "+board1);
        System.out.println("The hamming distance of board1 is: " + board1.hamming());
        System.out.println("The Manhattan distance is: " + board1.manhattan());

        System.out.println(board2);
        System.out.println("board2 is: "+board2);
        System.out.println("The hamming distance of board2 is: " + board2.hamming());
        System.out.println("The Manhattan distance of board2 is: " + board2.manhattan());

        System.out.println(board3);
        System.out.println("board2 is: "+board3);
        System.out.println("The hamming distance of board3 is: " + board3.hamming());
        System.out.println("The Manhattan distance of board3 is: " + board3.manhattan());

        System.out.println(board4);
        System.out.println("board1 is: "+board4);
        System.out.println("The hamming distance of board4 is: " + board4.hamming());
        System.out.println("The Manhattan distance is: " + board4.manhattan());

        System.out.println(board5);
        System.out.println("board1 is: "+board4);
        System.out.println("The hamming distance of board4 is: " + board5.hamming());
        System.out.println("The Manhattan distance is (should be 17): " + board5.manhattan());

        //Test twin()
        System.out.println("The twin of board1 is:\n " + board1.twin());
        System.out.println("The twin of board2 is:\n" + board2.twin());
        System.out.println("The twin of board3 is:\n " + board3.twin());
        System.out.println("The twin of board4 is:\n " + board4.twin());

        //Test equals
        System.out.println("Are these boards equal? (Should be false): " + board1.equals(board2));
        System.out.println("Are these boards equal? (Should be false): " + board2.equals(board3));
        System.out.println("Are these boards equal? (Should be false): " + board3.equals(board4));

        //Test iterator for neighbors
        System.out.println("This should print all neighbors of board1");
        Iterable<Board> neighborsB1 = board1.neighbors();
        for (Board neighbor : neighborsB1) {
            System.out.println(neighbor);
        }

        System.out.println("This should print all neighbors of board2");
        Iterable<Board> neighborsB2 = board2.neighbors();
        for (Board neighbor : neighborsB2) {
            System.out.println(neighbor);
        }

        System.out.println("This should print all neighbors of board3");
        Iterable<Board> neighborsB3 = board3.neighbors();
        for (Board neighbor : neighborsB3) {
            System.out.println(neighbor);
        }
        System.out.println("This should print all neighbors of board4");
        Iterable<Board> neighborsB4 = board4.neighbors();
        for (Board neighbor : neighborsB4) {
            System.out.println(neighbor);
        }

    } // unit tests (not graded)

}