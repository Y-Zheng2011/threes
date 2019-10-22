package threes;

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
    private long[][] MASK = {{0xf000000000000000L, 0x0f00000000000000L, 0x00f0000000000000L, 0x000f000000000000L},
            {0x0000f00000000000L, 0x00000f0000000000L, 0x000000f000000000L, 0x0000000f00000000L},
            {0x00000000f0000000L, 0x000000000f000000L, 0x0000000000f00000L, 0x00000000000f0000L},
            {0x000000000000f000L, 0x0000000000000f00L, 0x00000000000000f0L, 0x000000000000000fL}};


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

    public float calcScore(){
        return 0;
    }

    public void printBoard(){
        long tmp;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width-1; j++) {
                tmp = (board & MASK[i][j]) >> (60-i*16-j*4);
                System.out.print(map[(int) tmp] + ", ");
            }
            tmp = (board & MASK[i][3]) >> (60-i*16-12);
            System.out.println(map[(int) tmp]);
        }
    }

    public void move(){

    }

}