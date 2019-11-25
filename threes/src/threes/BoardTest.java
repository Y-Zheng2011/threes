package threes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BoardTest {
    //region Constants
    //Build a BiMap between the actual value and stored indices.
    private static final int[] MAP = {0, 1, 2, 3, 6, 12, 24, 48, 96, 192, 384, 768, 1536, 3072, 6144};
    private static final Map<Integer, Integer> RMAP;
    static {
        RMAP = new HashMap<>();
        for (int i = 0; i < MAP.length; i++) {
            RMAP.put(MAP[i], i);
        }
    }

    //Masks for acquiring each cell
    private static final long[][] maskCell = {{0xf000000000000000L, 0x0f00000000000000L, 0x00f0000000000000L, 0x000f000000000000L},
            {0x0000f00000000000L, 0x00000f0000000000L, 0x000000f000000000L, 0x0000000f00000000L},
            {0x00000000f0000000L, 0x000000000f000000L, 0x0000000000f00000L, 0x00000000000f0000L},
            {0x000000000000f000L, 0x0000000000000f00L, 0x00000000000000f0L, 0x000000000000000fL}};

    //bitShift for each cell and initialization.
    private static final long[][] cellShift = {{60, 56, 52, 48}, {44, 40, 36, 32}, {28, 24, 20, 16}, {12, 8, 4, 0}};

    private static final long[] maskCol = {0xf000f000f000f000L, 0x0f000f000f000f00L, 0x00f000f000f000f0L, 0x000f000f000f000fL};
    private static final long[] maskRow = {0xffff000000000000L, 0x0000ffff00000000L, 0x00000000ffff0000L, 0x000000000000ffffL};
    private static final int[] rowShift = {12, 8, 4, 0};
    //endregion


    private static int[] resultLeft = new int[65536];
    private static int[] resultRight = new int[65536];



    private int nextCard;
    private int multiNext = 0;
    private long board = 0; //Per the idea in https://github.com/nneonneo/threes-ai, use a 64bit integer to store the entire board.
    private int maxCard = 3;
    private int maxPos = 1; //If max card is on the edge, maxPos = 0, else maxPos = 1.
    private boolean[] nextPos = new boolean[4]; //nextPos ia a boolean array that tell if the cell is possible to be inserted in the next card.


    //region Constructors
    public BoardTest(ImProc image){
        int[][] currentBoard = image.getBoard();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                board = (board << 4) + RMAP.get(currentBoard[i][j]);
                if (maxCard < currentBoard[i][i]) maxCard = RMAP.get(currentBoard[i][i]);
            }
        }
        this.nextCard = image.imProcNextCard();
        Arrays.fill(resultLeft, -1);
        Arrays.fill(resultRight, -1);
    }

    public BoardTest(int[][] currentBoard, int nc){
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                board = (board << 4) + currentBoard[i][j];
            }
        }
        this.nextCard = nc;
    }


    public BoardTest(BoardTest b){
        board = b.board;
        nextCard = b.nextCard;
        maxCard = b.nextCard;
        multiNext = b.multiNext;
    }
    //endregion




    public void printBoard() {
        int tmp;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                tmp = (int) ((board & maskCell[i][j]) >> cellShift[i][j]);
                System.out.print(tmp + ", ");
            }
            tmp = (int) ((board & maskCell[i][3]) >> cellShift[i][3]);
            System.out.println(tmp);
        }
        System.out.println();
    }


    public boolean swipe(int dir) {
        boolean ret;
        resetNP();
        if (dir == 0){
            ret = swipeLeft();
        } else if (dir == 1){
            ret = swipeDown();
        } else if (dir == 2){
            ret = swipeRight();
        } else {
            ret = swipeUp();
        }
        return ret;
    }

    private void resetNP() {
        for (int i = 0; i< 4; i++){
            nextPos[i] = false;
        }
    }

    private boolean foldLeft(int i) {
        boolean ret = false;
        short row = (short) ((board & maskRow[i]) >> rowShift[i]);
        int r = resultLeft[row];
        if (r != -1) {
            if (r != row) {
                board = (board & ~maskRow[i]) | (r << rowShift[i]);
                nextPos[i] = true;
                return true;
            } else {
                return false;
            }
        } else {
            int fold = -1;
            short[] cell = new short[4];
            cell[0] = (short) ((board & maskCell[i][0]) >> cellShift[i][0]);
            for (int j = 1; j < 4; j++) {
                cell[j] = (short) ((board & maskCell[i][j]) >> cellShift[i][j]);
                if (fold == -1) {
                    if (cell[j] != 0 && cell[j - 1] == 0) {
                        cell[j - 1] = cell[j];
                        cell[j] = 0;
                        fold = j;
                    } else if (cell[j - 1] + cell[j] == 3 && cell[j] != 0) {
                        cell[j - 1] = 3;
                        cell[j] = 0;
                        fold = j;
                    } else if ((cell[j - 1] == cell[j]) && (cell[j] > 2)) {
                        cell[j - 1]++;
                        cell[j] = 0;
                        fold = j;
                    }
                }
                if (fold != -1) {
                    ret = true;
                    nextPos[i] = true;
                    System.arraycopy(cell, fold + 1, cell, fold, 3 - fold);
                    cell[3] = 0;
                    r = (short) (cell[0] << 12 | cell[1] << 8 | cell[2] << 4 | cell[3]) ;
                    resultLeft[row] = r;
                    board = (board & ~maskRow[i]) | (r << rowShift[i]);
                } else if (cell[3] == 0) {
                    nextPos[i] = true;
                    resultLeft[row] = row;
                }
            }
        }
        return ret;
    }

    private boolean foldRight(int i) {
        boolean ret = false;
        short row = (short) ((board & maskRow[i]) >> rowShift[i]);
        int r = resultRight[row];
        if (r != -1) {
            if (r != row) {
                board = (board & ~maskRow[i]) | (r << rowShift[i]);
                nextPos[i] = true;
                return true;
            } else {
                return false;
            }
        } else {
            int fold = -1;
            short[] cell = new short[4];
            cell[0] = (short) ((board & maskCell[i][0]) >> cellShift[i][0]);
            for (int j = 1; j < 4; j++) {
                cell[j] = (short) ((board & maskCell[i][j]) >> cellShift[i][j]);
                if (fold == -1) {
                    if (cell[j] != 0 && cell[j - 1] == 0) {
                        cell[j - 1] = cell[j];
                        cell[j] = 0;
                        fold = j;
                    } else if (cell[j - 1] + cell[j] == 3 && cell[j] != 0) {
                        cell[j - 1] = 3;
                        cell[j] = 0;
                        fold = j;
                    } else if ((cell[j - 1] == cell[j]) && (cell[j] > 2)) {
                        cell[j - 1]++;
                        cell[j] = 0;
                        fold = j;
                    }
                }
                if (fold != -1) {
                    ret = true;
                    nextPos[i] = true;
                    System.arraycopy(cell, fold + 1, cell, fold, 3 - fold);
                    cell[3] = 0;
                    r = (short) (cell[0] << 12 | cell[1] << 8 | cell[2] << 4 | cell[3]) ;
                    resultRight[row] = r;
                    board = (board & ~maskRow[i]) | (r << rowShift[i]);
                } else if (cell[3] == 0) {
                    nextPos[i] = true;
                    resultRight[row] = row;
                }
            }
        }
        return ret;
    }

    private boolean swipeLeft() {
        return foldLeft(0) | foldLeft(1) | foldLeft(2) | foldLeft(3);
    }

    private boolean swipeRight() {
        return foldRight(0) | foldLeft(1) | foldLeft(2) | foldLeft(3);
    }



    private boolean swipeDown() {
        transpose();
        boolean ret = foldLeft(0) | foldLeft(1) | foldLeft(2) | foldLeft(3);
        transpose();
        return ret;
    }

    private boolean swipeUp() {

    }

    private void transpose() {
        long r1,r2,r3,l1,l2,l3;
        l1 = board & 0x0f0000f0000f0000L;
        l2 = board & 0x00f0000f00000000L;
        l3 = board & 0x000f000000000000L;
        r1 = board & 0x0000f0000f0000f0L;
        r2 = board & 0x00000000f0000f00L;
        r3 = board & 0x000000000000f000L;
        board = (board & 0xf0000f0000f0000fL) | (l1 >> 12) | (l2 >> 24) | (l3 >> 36);
        board = board | (r1 << 12) | (r2 << 24) | (r3 << 36);
    }

/*
    private void mirror() {
        long r3,r1,l1,l3;
        r3 = board & 0xF000F000F000F000L;
        r1 = board & 0x0F000F000F000F00L;
        l1 = board & 0x00F000F000F000F0L;
        l3 = board & 0x000F000F000F000FL;
        board = r3 >> 12 | r1 >> 4 | l3 << 12 | l1 << 4;
    }
*/

    public static void main(String[] args) {
        int[][] b = {{0, 1, 2, 3},
                {4, 5, 6, 7},
                {0, 1, 2, 3},
                {4, 5, 6, 7}};
        int nc = 2;
        BoardTest board = new BoardTest(b, nc);
        board.printBoard();
        board.transpose();
        board.printBoard();
        board.transpose();
        board.mirror();
        board.printBoard();
    }
}
