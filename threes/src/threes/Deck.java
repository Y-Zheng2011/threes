package threes;

import java.util.*;

public class Deck {

    //deck stores the number of each cards and prob stores teh probability of drawing each card from the current deck (Index: Card= 0 : 1, 1 : 2, 2: 3).
    private int[] deck = new int[3];
    private float[] prob = new float[3];
    private int deckNum;


    public Deck(Deck d){
        System.arraycopy(d.deck, 0, deck, 0, d.deck.length);
        System.arraycopy(d.prob, 0, prob, 0, d.prob.length);
        deckNum = d.deckNum;
    }

    //Constructor for the beginning of the game(9 cards on the board when game starts, deckNum = 3).
    public Deck(int[][] b){
        int c1 = 0, c2 = 0, c3 = 0;
        for (int i = 0; i < b.length; i++){
            for (int j = 0; j < b.length; j++){
                if (b[i][j] == 1) {
                    c1++;
                } else if (b[i][j] == 2) {
                    c2++;
                } else if (b[i][j] == 3) {
                    c3++;
                }
            }
        }
        deckNum = 12- c1 - c2 - c3;
        deck[0] = 4 - c1;
        deck[1] = 4 - c2;
        deck[2] = 4 - c3;
        prob[0] = 1.0f * deck[0]/deckNum;
        prob[1] = 1.0f * deck[1]/deckNum;
        prob[2] = 1.0f * deck[2]/deckNum;
    }

    public int getDeckNum(){
        return deckNum;
    }

    //Reset the deck.
    public void reset(){
        Arrays.fill(deck,4);
        Arrays.fill(prob,1.0f/3);
        deckNum = 12;
    }

    public boolean isEmpty(int c){
       return deck[c-1] == 0;
    }

    public void draw(int card) {
        if (deck[card-1] > 0){
            deck[card-1]--;
            deckNum--;
            prob[card-1] = 1.0f * deck[card-1]/deckNum;
        }
    }

    //Calculate the probability of drawing a normal card from the current deck.
    public float calcNormProb(int max, int card) {
        if (max < 48) {
            return prob[card-1];    // If max card is less than 48, no bonus card.
        } else {
            return prob[card-1] * 20/21;  //If max card is at least 48, there's a chance of 1/21 to draw a bonus card.
        }
    }

}