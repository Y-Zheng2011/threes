package threes;

public class Evaluator {

    private Board board;
    private int nextCard;

    // Heuristic scoring settings
    static float scoreLostPenalty = -200000.0f;
    static float scoreEmptySpace = 200.0f;
    static float scoreMaxCard = 200.0f;
    static float scoreFold = 300.0f;

    public Evaluator(Board b) {
        board = new Board(b);
        nextCard = board.getNextCard();
    }

    public void setHS(float[] h) {
        scoreLostPenalty = h[0];
        scoreEmptySpace = h[1];
        scoreMaxCard = h[2];
        scoreFold = h[3];
    }

    private int countEmpty() {
        int count = 0;
        long c, b = board.getBoard();
        for (int i = 0; i <16; i++) {
            b = b >> 4;
            c = b & 0xFL;
            if (c == 0) count++;
        }
        return count;
    }

    private int countFold() {
        int count = 0;
        int[][] card = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                card[i][j] = board.getCardIndex(i,j);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((card[i][j] == 1 & card[i][j+1] == 2) || (card[i][j] == 2 & card[i][j+1] == 1)) {
                    count++;
                } else if (card[i][j] == card[i][j+1])

                if ((card[i][j] == 1 & card[i+1][j] == 2) || (card[i][j] == 2 & card[i+1][j] == 1)) {
                    count++;
                }
            }
        }
    }

    //Calculate heuristic score for the current board.
    public float calcScore(){
        float score = 0;

        return score;
    }
}
