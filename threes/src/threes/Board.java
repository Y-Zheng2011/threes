package threes;

public class Board{
    // Heuristic scoring settings
    static float scoreLostPenalty = -200000.0f;
    static float scoreEmptySpace = 200.0f;

    private int nextCard = 0;
    private long board; //Per the idea in https://github.com/nneonneo/threes-ai, use a 64bit integer to store the entire board.
    private int maxCard = 0;

    public Board(int[][] cur){

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



    public void move(){

    }

}