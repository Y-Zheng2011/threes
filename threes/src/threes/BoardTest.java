package threes;

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
    private static final long[][] mask = {{0xf000000000000000L, 0x0f00000000000000L, 0x00f0000000000000L, 0x000f000000000000L},
            {0x0000f00000000000L, 0x00000f0000000000L, 0x000000f000000000L, 0x0000000f00000000L},
            {0x00000000f0000000L, 0x000000000f000000L, 0x0000000000f00000L, 0x00000000000f0000L},
            {0x000000000000f000L, 0x0000000000000f00L, 0x00000000000000f0L, 0x000000000000000fL}};

    //bitShift for each cell and initialization.
    private static final long[][] bitShift = {{60, 56, 52, 48}, {44, 40, 36, 32}, {28, 24, 20, 16}, {12, 8, 4, 0}};

    private static final long[] maskCol = {0xf000f000f000f000L, 0x0f000f000f000f00L, 0x00f000f000f000f0L, 0x000f000f000f000fL};
    private static final long[] maskRow = {0xffff000000000000L, 0x0000ffff00000000L, 0x00000000ffff0000L, 0x000000000000ffffL};
    //endregion

    private class Threes {
        private short[] result = new short[65536];
    }


    private int nextCard;
    private int multiNext = 0;
    private long board = 0; //Per the idea in https://github.com/nneonneo/threes-ai, use a 64bit integer to store the entire board.
    private int maxCard = 3;
    private int maxPos = 1; //If max card is on the edge, maxPos = 0, else maxPos = 1.
    private int size;
    private boolean[] nextPos = new boolean[4]; //nextPos ia a boolean array that tell if the cell is possible to be inserted in the next card.


    //region Constructors
    public BoardTest(ImProc image){
        int[][] currentBoard = image.getBoard();
        size = currentBoard.length;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                board = (board << 4) + RMAP.get(currentBoard[i][j]);
                if (maxCard < currentBoard[i][i]) maxCard = RMAP.get(currentBoard[i][i]);
            }
        }
        this.nextCard = image.imProcNextCard();
    }

    public BoardTest(int[][] currentBoard, int nc){
        size = currentBoard.length;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                board = (board << 4) + currentBoard[i][j];
            }
        }
        this.nextCard = nc;
    }


    public BoardTest(BoardTest b){
        board = b.board;
        nextCard = b.nextCard;
        maxCard = b.nextCard;
        size = b.size;
        multiNext = b.multiNext;
    }
    //endregion


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

    public void printBoard() {
        int tmp;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size-1; j++) {
                tmp = (int) ((board & mask[i][j]) >> bitShift[i][j]);
                System.out.print(tmp + ", ");
            }
            tmp = (int) ((board & mask[i][3]) >> bitShift[i][3]);
            System.out.println(tmp);
        }
        System.out.println();
    }

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
    }
}
