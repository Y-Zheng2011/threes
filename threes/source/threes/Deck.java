package threes;

import java.util.*;

public class Deck{
    private static final int[] map = {3, 6, 12, 24, 48, 96, 192, 384, 768, 1536, 3072, 6144};
    private static final Map<Integer, Integer> rmap;
    static {
        rmap = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            rmap.put(map[i],i);
        }
    }

    private int[] deck = new int[3];
    private float[] probCards = new float[3];
    private int deckNum;


/*    public Deck(){
        Arrays.fill(deck,4);
    }*/

/*    public Deck(int maxCard){
        this.maxCard = maxCard;
        Arrays.fill(deck,4);
        deckNum = 12;
    }*/

    //Constructor for the beginning of the game(9 cards on the board when game starts, deckNum = 3).
    public Deck(int c1, int c2, int c3){
        deck[0] = 4 - c1;
        deck[1] = 4 - c2;
        deck[2] = 4 - c3;
        deckNum = 12- c1 - c2 - c3;
    }

    //Reset the deck.
    public void reset(){
        Arrays.fill(deck,4);
        deckNum = 12;
    }

    public void draw(int card) {
        deck[card-1]--;
        deckNum--;
    }

    //Calculate the probability of drawing a normal card from the current deck.
    public float calcNormProb(Board board, int card) {
        if (board.getMaxCard() < 48) {
            return deck[card - 1] / 1.0f / (deck[0] + deck[1] + deck[2]);    // If max card is less than 48, no bonus card.
        } else {
            return deck[card - 1] / 1.0f / (deck[0] + deck[1] + deck[2])*20/21;  //If max card is at least 48, there's a chance of 1/21 to draw a bonus card.
        }
    }

}