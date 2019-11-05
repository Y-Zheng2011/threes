package threes;

public class Evaluator {

    private int countEmpty = 0;
    private int count1and2 = 0;
    private int countFold = 0;

    // Heuristic scoring settings
    private static float scoreLostPenalty = -20000.0f;
    private static float scoreEmptySpace = 7.0f;
    private static float scoreMaxCard = 15.0f;
    private static float scoreFold = 4.0f;
    private static float score1and2 = 3.0f;


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
        int[][] card = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                card[i][j] = board.getCardIndex(i,j);
                if (card[i][j] == 0) countEmpty++;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (card[i][j] == 1 || card[i][j] == 2) {
                    count1and2++;
                } else if ((card[i][j] == card[i][j+1] && card[i][j] > 2) || (card[i][j] == 1 & card[i][j+1] == 2) || (card[i][j] == 2 & card[i][j+1] == 1)) {
                    countFold++;
                }
                if ((card[i][j] == card[i+1][j] && card[i][j] > 2) || (card[i][j] == 1 & card[i+1][j] == 2) || (card[i][j] == 2 & card[i+1][j] == 1)) {
                    countFold++;
                }
            }

            if (card[3][i] == 1 || card[3][i] == 2) {
                count1and2++;
            } else if ((card[3][i] == card[3][i+1] && card[3][i] > 2) || (card[3][i] == 1 & card[3][i+1] == 2) || (card[3][i] == 2 & card[3][i+1] == 1)) {
                countFold++;
            }

            if (card[i][3] == 1 || card[i][3] == 2) {
                count1and2++;
            } else if ((card[i][3] == card[i+1][3] && card[i][3] > 2) || (card[i][3] == 1 & card[i+1][3] == 2) || (card[i][3] == 2 & card[i+1][3] == 1)) {
                countFold++;
            }
        }
    }


    //Calculate heuristic score for the current board.
    public float calcScore(Board b){
        float score;
        count(b);
        score = countEmpty * scoreEmptySpace + countFold * scoreFold - count1and2 * score1and2 + b.getMaxCard() * scoreMaxCard;
        return score;
    }

    //Calculate the average score for the board when search reaches the max depth
    public float calcAvgScore(Board b, int move) {
        int counter = 0, size = b.getSize()-1;
        float avg = 0.0f;
        if (move == 0) {
            for (int i = 0; i <= size; i++) {
                if (b.getCardIndex(i, size) == 0) {
                    Board p = new Board(b);
                    p.insert(i, size);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 1) {
            for (int i = 0; i <= size; i++) {
                if (b.getCardIndex(0, i) == 0) {
                    Board p = new Board(b);
                    p.insert(0, i);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 2) {
            for (int i = 0; i < size; i++) {
                if (b.getCardIndex(i, 0) == 0) {
                    Board p = new Board(b);
                    p.insert(i, 0);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 3) {
            for (int i = 0; i < size; i++) {
                if (b.getCardIndex(size, i) == 0) {
                    Board p = new Board(b);
                    p.insert(size, i);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else return avg;
    }

    //Calculate the average score for the board with recursion
    public float calcAvgRec(Board board, Deck deck, int move, int depth) {
        int counter = 0, size = board.getSize()-1, m;
        float avg = 0.0f;
        if (move == 0) {
            for (int i = 0; i <= size; i++) {
                if (board.getCardIndex(i, size) == 0) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(board);
                            p.insert(i, size);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            p.swipe(m);
                            avg = avg + d.calcNormProb(p.getMaxCard(), j) * calcScore(p);
                        }
                    }
                    counter++;
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 1) {
            for (int i = 0; i <= size; i++) {
                if (board.getCardIndex(0, i) == 0) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(board);
                            p.insert(0, i);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            p.swipe(m);
                            avg = avg + d.calcNormProb(p.getMaxCard(), j) * calcScore(p);
                        }
                    }
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 2) {
            for (int i = 0; i < size; i++) {
                if (board.getCardIndex(i, 0) == 0) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(board);
                            p.insert(i, 0);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            p.swipe(m);
                            avg = avg + d.calcNormProb(p.getMaxCard(), j) * calcScore(p);
                        }
                    }
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 3) {
            for (int i = 0; i < size; i++) {
                if (board.getCardIndex(size, i) == 0) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(board);
                            p.insert(size, i);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            p.swipe(m);
                            avg = avg + d.calcNormProb(p.getMaxCard(), j) * calcScore(p);
                        }
                    }
                }
            }
            avg = avg / counter;
            return avg;
        } else return avg;
    }
}
