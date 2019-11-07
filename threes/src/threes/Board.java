package threes;

import java.util.HashMap;
import java.util.Map;

public class Board {

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

    //bitshift for each cell and initialization.
    private static final long[][] bitShift = {{60, 56, 52, 48}, {44, 40, 36, 32}, {28, 24, 20, 16}, {12, 8, 4, 0}};

    private static final long[] maskCol = {0xf000f000f000f000L, 0x0f000f000f000f00L, 0x00f000f000f000f0L, 0x000f000f000f000fL};
    private static final long[] maskRow = {0xffff000000000000L, 0x0000ffff00000000L, 0x00000000ffff0000L, 0x000000000000ffffL};
    //endregion

    private int nextCard;
    private boolean multiNext = false;
    private long board = 0; //Per the idea in https://github.com/nneonneo/threes-ai, use a 64bit integer to store the entire board.
    private int maxCard = 3;
    private int size;
    private boolean[] nextPos = new boolean[4]; //nextPos ia a boolean array that tell if the cell is possible to be inserted in the next card.


    //region Constructors
    public Board(ImProc image){
        int[][] currentBoard = image.getBoard();
        size = currentBoard.length;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                board = (board << 4) + RMAP.get(currentBoard[i][j]);
                if (maxCard < currentBoard[i][i]) maxCard = RMAP.get(currentBoard[i][i]);
            }
        }
        this.nextCard = image.getNextCard();
    }

    public Board(Board b){
        board = b.board;
        nextCard = b.nextCard;
        maxCard = b.nextCard;
        size = b.size;
    }
    //endregion

    //region Accessors
    public void setNextCard(int card){
        nextCard = RMAP.get(card);
    }

    public int getNextCard(){
        return nextCard;
    }

    public void setMultiNext(boolean isMultiNext) {
        multiNext = isMultiNext;
    }

    //Return the max card, not index.
    public int getMaxCard(){
        findMaxCard();
        return MAP[maxCard];
    }

    public int getSize() {
        return this.size;
    }

    //endregion

    public int getCardIndex(int x, int y) {
         return (int) ((board & mask[x][y]) >> bitShift[x][y]);
    }

    public void printBoard() {
        int tmp;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size-1; j++) {
                tmp = (int) ((board & mask[i][j]) >> bitShift[i][j]);
                System.out.print(MAP[tmp] + ", ");
            }
            tmp = (int) ((board & mask[i][3]) >> bitShift[i][3]);
            System.out.println(MAP[tmp]);
        }
        System.out.println();
    }

    /*If swiping is doable, return 1, else return 0.
    parameter dir indicates the direction of swiping (0, 1, 2 ,3 for left, down, right, up respectively).
    */
    public boolean swipe(int dir) {
        boolean ret = false;
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

    public void insNext(ImProc image, int move) {
        int[] pos = {-1, -1};
        if (multiNext) {

        }
        if (move == 0) {
            pos[1] = 3;
            for (int i = 0; i < 4; i++) {
                if (nextPos[i] && image.findIns(3, i)) {
                        pos[0] = i;
                        break;
                }
            }
        } else if (move == 1) {
            pos[0] = 0;
            for (int i = 0; i < 4; i++) {
                if (nextPos[i] && image.findIns(i, 0)) {
                        pos[1] = i;
                        break;
                }
            }
        } else if (move == 2) {
            pos[1] = 0;
            for (int i = 0; i < 4; i++) {
                if (nextPos[i] && image.findIns(0, i)) {
                        pos[0] = i;
                        break;
                }
            }
        } else {
            pos[0] = 3;
            for (int i = 0; i < 4; i++) {
                if (nextPos[i] && image.findIns(i, 3)) {
                        pos[1] = i;
                        break;
                }
            }
        }
        insert(pos[0], pos[1]);
    }


    public void insert(int x, int y) {
        long tmp = nextCard;
        if (this.getCardIndex(x, y) != 0) {
            System.out.println("Insertion error!");
            System.out.printf("Cell %d %d is not empty!", x, y);
            System.exit(1);
        }
        board = board | tmp << bitShift[x][y];
    }

    private void resetNP() {
        for (int i = 0; i< size; i++){
            nextPos[i] = false;
        }
    }

    private void findMaxCard() {
        int tmp;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmp = (int) (board & mask[i][j]) >> bitShift[i][j];
                if (maxCard < tmp) {
                    maxCard = tmp;
                }
            }
        }
    }

    private boolean swipeLeft() {
        boolean ret = false;
        int fold;
        long[] cell = new long[size];
        for (int i = 0; i < size; i++) {
            fold = -1;
            cell[0] = (int) ((board & mask[i][0]) >> bitShift[i][0]);
            for (int j = 1; j < size; j++) {
                cell[j] = (int) ((board & mask[i][j]) >> bitShift[i][j]);
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
            }
            if (fold != -1) {
                ret = true;
                nextPos[i] = true;
                if (size - 1 - fold >= 0) System.arraycopy(cell, fold + 1, cell, fold, size - 1 - fold);
                cell[size-1] = 0;
                board = (board & ~maskRow[i]) |
                        (cell[0] << bitShift[i][0]) |
                        (cell[1] << bitShift[i][1]) |
                        (cell[2] << bitShift[i][2]) |
                        (cell[3] << bitShift[i][3]);
            } else if (cell[3] == 0) {
                nextPos[i] = true;
            }
        }
        return ret;
    }

    private boolean swipeDown() {
        boolean ret = false;
        int fold;
        long[] cell = new long[size];
        for (int i = 0; i < size; i++) {
            fold = -1;
            cell[size-1] = (int) ((board & mask[size-1][i]) >> bitShift[size-1][i]);
            for (int j = size-2; j >= 0; j--) {
                cell[j] = (int) ((board & mask[j][i]) >> bitShift[j][i]);
                if (fold == -1) {
                    if (cell[j] != 0 && cell[j + 1] == 0) {
                        cell[j + 1] = cell[j];
                        cell[j] = 0;
                        fold = j;
                    } else if (cell[j + 1] + cell[j] == 3 && cell[j] != 0) {
                        cell[j + 1] = 3;
                        cell[j] = 0;
                        fold = j;
                    } else if ((cell[j + 1] == cell[j]) && (cell[j] > 2)) {
                        cell[j + 1]++;
                        cell[j] = 0;
                        fold = j;
                    }
                }
            }
            if (fold != -1) {
                ret = true;
                nextPos[i] = true;
                System.arraycopy(cell, 0, cell, 1, fold);
                cell[0] = 0;
                board = (board & ~maskCol[i]) |
                        (cell[0] << bitShift[0][i]) |
                        (cell[1] << bitShift[1][i]) |
                        (cell[2] << bitShift[2][i]) |
                        (cell[3] << bitShift[3][i]);
            } else if (cell[0] == 0) {
                nextPos[i] = true;
            }
        }
        return ret;
    }

    private boolean swipeRight() {
        boolean ret = false;
        int fold;
        long[] cell = new long[size];
        for (int i = 0; i < size; i++) {
            fold = -1;
            cell[size-1] = (int) ((board & mask[i][size-1]) >> bitShift[i][size-1]);
            for (int j = size-2; j >= 0; j--) {
                cell[j] = (int) ((board & mask[i][j]) >> bitShift[i][j]);
                if (fold == -1) {
                    if (cell[j] != 0 && cell[j + 1] == 0) {
                        cell[j + 1] = cell[j];
                        cell[j] = 0;
                        fold = j;
                    } else if (cell[j + 1] + cell[j] == 3 && cell[j] != 0) {
                        cell[j + 1] = 3;
                        cell[j] = 0;
                        fold = j;
                    } else if ((cell[j + 1] == cell[j]) && (cell[j] > 2)) {
                        cell[j + 1]++;
                        cell[j] = 0;
                        fold = j;
                    }
                }
            }
            if (fold != -1) {
                ret = true;
                nextPos[i] = true;
                System.arraycopy(cell, 0, cell, 1, fold);
                cell[0] = 0;
                board = (board & ~maskRow[i]) |
                        (cell[0] << bitShift[i][0]) |
                        (cell[1] << bitShift[i][1]) |
                        (cell[2] << bitShift[i][2]) |
                        (cell[3] << bitShift[i][3]);
            } else if (cell[0] == 0) {
                nextPos[i] = true;
            }
        }
        return ret;
    }

    private boolean swipeUp() {
        boolean ret = false;
        int fold;
        long[] cell = new long[size];
        for (int i = 0; i < size; i++) {
            fold = -1;
            cell[0] = (int) ((board & mask[0][i]) >> bitShift[0][i]);
            for (int j = 1; j < size; j++) {
                cell[j] = (int) ((board & mask[j][i]) >> bitShift[j][i]);
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
            }
            if (fold != -1) {
                ret = true;
                nextPos[i] = true;
                if (size - 1 - fold >= 0) System.arraycopy(cell, fold + 1, cell, fold, size - 1 - fold);
                cell[size-1] = 0;
                board = (board & ~maskCol[i]) |
                        (cell[0] << bitShift[0][i]) |
                        (cell[1] << bitShift[1][i]) |
                        (cell[2] << bitShift[2][i]) |
                        (cell[3] << bitShift[3][i]);
            } else if (cell[3] == 0) {
                nextPos[i] = true;
            }
        }
        return ret;
    }

}