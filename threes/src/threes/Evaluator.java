package threes;

public class Evaluator {

    private int countEmpty = 0;
    private int count1and2 = 0;
    private int countFold = 0;
//    private Board board;

    // Heuristic scoring settings
    private static float scoreLostPenalty = -2000.0f;
    private static float scoreEmptySpace = 3.0f;
    private static float scoreMaxCard = 10.0f;
    private static float scoreFold = 7.0f;
    private static float score1and2 = 1.0f;

//    public Evaluator(Board b) {
//        board = new Board(b);
//    }

    public void setHS(float[] h) {
        scoreLostPenalty = h[0];
        scoreEmptySpace = h[1];
        scoreMaxCard = h[2];
        scoreFold = h[3];
        score1and2 = h[4];
    }

    private void reset(){
        countEmpty = 0;
        countFold = 0;
        count1and2 = 0;
    }

    private void count(Board board) {
        reset();
        long b = board.getBoard();
        int[][] card = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                card[i][j] = board.getCardIndex(i,j);
                if (card[i][j] == 0) countEmpty++;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((card[i][j] == 1 & card[i][j+1] == 2) || (card[i][j] == 2 & card[i][j+1] == 1)) {
                    count1and2++;
                } else if (card[i][j] == card[i][j+1] && card[i][j] > 2) {
                    countFold++;
                }

                if ((card[i][j] == 1 & card[i+1][j] == 2) || (card[i][j] == 2 & card[i+1][j] == 1)) {
                    count1and2++;
                } else if (card[i][j] == card[i+1][j] && card[i][j] > 2) {
                    countFold++;
                }
            }

            if ((card[3][i] == 1 & card[3][i+1] == 2) || (card[3][i] == 2 & card[3][i+1] == 1)) {
                count1and2++;
            } else if (card[3][i] == card[3][i+1] && card[3][i] > 2) {
                countFold++;
            }

            if ((card[i][3] == 1 & card[i+1][3] == 2) || (card[i][3] == 2 & card[i+1][3] == 1)) {
                count1and2++;
            } else if (card[i][3] == card[i+1][3] && card[i][3] > 2) {
                countFold++;
            }
        }
    }

    public void print(Board b){
        count(b);
        System.out.println("Empty spaces: " + countEmpty);
        System.out.println("Foldable (not 1-2) pairs: " + countFold);
        System.out.println("Adjacent 1-2 pairs: " + count1and2);
        System.out.println("Current score: " + calcScore(b));
    }

    //Calculate heuristic score for the current board.
    public float calcScore(Board b){
        float score;
        count(b);
        score = countEmpty * scoreEmptySpace + countFold * scoreFold + count1and2 * score1and2 + b.getMaxCard() * scoreMaxCard;
        return score;
    }
}
