package threes;

import org.jetbrains.annotations.Contract;

import java.util.*;

public class Board{

    //Build a BiMap for between the actual value and stored indices.
    private static final int[] map = {0, 1, 2, 3, 6, 12, 24, 48, 96, 192, 384, 768, 1536, 3072, 6144};
    private static final Map<Integer, Integer> rmap;
    static {
        rmap = new HashMap<>();
        for (int i = 0; i < map.length; i++) {
            rmap.put(map[i],i);
        }
    }

    //Masks for acquiring each cell
    private static final long[][] mask = {{0xf000000000000000L, 0x0f00000000000000L, 0x00f0000000000000L, 0x000f000000000000L},
            {0x0000f00000000000L, 0x00000f0000000000L, 0x000000f000000000L, 0x0000000f00000000L},
            {0x00000000f0000000L, 0x000000000f000000L, 0x0000000000f00000L, 0x00000000000f0000L},
            {0x000000000000f000L, 0x0000000000000f00L, 0x00000000000000f0L, 0x000000000000000fL}};

    //bitshift for each cell and initialization.
    private static final long[][] bitShift = {{60, 56, 52, 48}, {44, 40, 36, 32}, {28, 24, 20, 16}, {12, 8, 4, 0}};

//    private static final long[] maskCol = {0xf000f000f000f000L, 0x0f000f000f000f00L, 0x00f000f000f000f0L, 0x000f000f000f000fL};
//    private static final long[] colShift = {};
//    private static final long[] maskRow = {0xffff000000000000L, 0x0000ffff00000000L, 0x00000000ffff0000L, 0x000000000000ffffL};
//    private static final long[] rowShift = {12, 8, 4, 0};


    // Heuristic scoring settings
    static float scoreLostPenalty = -200000.0f;
    static float scoreEmptySpace = 200.0f;

    private int nextCard;
    private long board = 0; //Per the idea in https://github.com/nneonneo/threes-ai, use a 64bit integer to store the entire board.
    private int maxCard = 0;
    private int height;
    private int width;

    // When the game start, input the board (as a 4 by 4 matrix) and the next card manually.
    public Board(int[][] currentBoard, int nextCard){
        height = currentBoard.length;
        width = currentBoard[0].length;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                board = (board << 4) + rmap.get(currentBoard[i][j]);
                if (maxCard < currentBoard[i][i]) maxCard = rmap.get(currentBoard[i][i]);
            }
        }
        this.nextCard = nextCard;
    }

    public Board(Board b, int nextCard){
        this.board = b.getBoard();
        this.nextCard = nextCard;
    }

    public void setNextCard(int card){
        nextCard = card;
    }

    public int getNextCard(){
        return nextCard;
    }

    public void setMaxCard(int maxCard){
        this.maxCard = maxCard;
    }

    public int getMaxCard(){
        return  maxCard;
    }

    public long getBoard(){
        return this.board;
    }

    public float calcScore(){
        return 0;
    }

    public void printBoard(){
        long tmp;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width-1; j++) {
                tmp = (board & mask[i][j]) >> bitShift[i][j];
                System.out.print(map[(int) tmp] + ", ");
            }
            tmp = (board & mask[i][3]) >> bitShift[i][3];
            System.out.println(map[(int) tmp]);
        }
    }

    private int swipeLeft(){
        int ret = 0;
        int[] cell = new int[width];
        for (int i = 0; i < width; i++){
            cell[0] = (int) ((board & mask[i][0]) >> bitShift[i][0]);
            for (int j = 1; j < width; j++){
                cell[j] = (int) ((board & mask[i][j]) >> bitShift[i][j]);
                if (cell[j-1] == 0) {
                    cell[j-1] = cell[j];
                    cell[j] = 0;
                    ret = 1;
                } else if (cell[j-1] + cell[j] == 3 ) {
                    cell[j-1] = 3;
                    cell[j] = 0;
                    ret = 1;
                } else if ((cell[j-1] == cell[j]) && (cell[j] > 2)) {
                    cell[j-1]++;
                    cell[j] = 0;
                    ret = 1;
                }
            }
            if (ret == 1) {
                board = (board & ~(mask[i][0]|mask[i][1]|mask[i][2]|mask[i][3])) |
                        (cell[0] << bitShift[i][0]) |
                        (cell[1] << bitShift[i][1]) |
                        (cell[2] << bitShift[i][2]) |
                        (cell[3] << bitShift[i][3]);
            }
        }
        return ret;
    }

    /*If swiping is doable, return 1, else return 0.
    parameter dir indicates the direction of swiping (0, 1, 2 ,3 for left, down, right, up respectively).
    */
    public int swipe(int dir){
        int ret;
        ret = swipeLeft();
        return ret;
    }

}