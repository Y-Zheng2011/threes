package threes;

public class Evaluator {

    private int countEmpty = 0;
    private int count1and2 = 0;
    private int countFold = 0;

    // Heuristic scoring settings
    private static float PenLost = -2000.0f;
    private static float scoreEmptySpace = 7.0f;
    private static float scoreMaxCard = 9.0f;
    private static float scoreFold = 4.0f;
    private static float pen1and2 = -7.0f;
    private static float penOffEdge = -5.0f;


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
        score = countEmpty * scoreEmptySpace + countFold * scoreFold + count1and2 * pen1and2 + b.getMaxCard() * scoreMaxCard + b.getMaxPos() * penOffEdge;
        return score;
    }

    //Calculate the average score for the board when search reaches the max depth
    public float calcAvgScore(Board b, int move) {
        int counter = 0, size = b.getSize();
        float avg = 0.0f;
        if (move == 0) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
                    Board p = new Board(b);
                    p.insert(i, size-1);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 1) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
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
                if (b.getNextPos(i)) {
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
                if (b.getNextPos(i)) {
                    Board p = new Board(b);
                    p.insert(size-1, i);
                    counter++;
                    avg = avg + calcScore(p);
                }
            }
            avg = avg / counter;
            return avg;
        } else return avg;
    }

    //Calculate the average score for the board with recursion
    public float calcAvgRec(Board b, Deck deck, int move, int depth) {
        int counter = 0, size = b.getSize(), m;
        float avg = 0.0f;
        if (move == 0) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(b);
                            p.insert(i, size-1);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            if (m != -1) {
                                p.swipe(m);
                                avg = avg + d.calcNormProb(j) * calcScore(p);
                            } else {
                                avg = avg + PenLost;
                            }
                        }
                    }
                    counter++;
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 1) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(b);
                            p.insert(0, i);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            if (m != -1) {
                                p.swipe(m);
                                avg = avg + d.calcNormProb(j) * calcScore(p);
                            } else {
                                avg = avg + PenLost;
                            }
                        }
                    }
                    counter++;
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 2) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(b);
                            p.insert(i, 0);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            if (m != -1) {
                                p.swipe(m);
                                avg = avg + d.calcNormProb(j) * calcScore(p);
                            } else {
                                avg = avg + PenLost;
                            }
                        }
                    }
                    counter++;
                }
            }
            avg = avg / counter;
            return avg;
        } else if (move == 3) {
            for (int i = 0; i < size; i++) {
                if (b.getNextPos(i)) {
                    for (int j = 1; j < 4; j++){
                        Deck d = new Deck(deck);
                        if (!d.isEmpty(j)){
                            d.draw(j);
                            Board p = new Board(b);
                            p.insert(size-1, i);
                            p.setNextCard(j);
                            m = Threes.findMove(p, depth - 1, d);
                            if (m != -1) {
                                p.swipe(m);
                                avg = avg + d.calcNormProb(j) * calcScore(p);
                            } else {
                                avg = avg + PenLost;
                            }
                        }
                    }
                    counter++;
                }
            }
            avg = avg / counter;
            return avg;
        } else return avg;
    }
}
