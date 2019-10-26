package threes;

public class Threes {

    /* Find move for the current board, search depth and deck.


     */
    public static int findMove(Board board, int depth, Deck deck) {
        int move = -1;
        float average, score = -1.0f;
        boolean shift, gameOver = false;
        Evaluator eval = new Evaluator();
        if (deck.getDeckNum() == 0) {
            deck.reset();
        }
        if (depth == 0) {
            for (int i = 0; i < 4; i++) {
                Board b = new Board(board);
                shift = b.swipe(i);
                gameOver = gameOver | shift;
                if (shift) {
                    average = eval.calcAvgScore(b, i);
                    if (score < average) {
                        score = average;
                        move = i;
                    }
                }
            }
            if (!gameOver) {
                System.out.println("Game is over!");
                System.out.println();
                return -1;
            } else {
                return move;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                Board b = new Board(board);
                shift = b.swipe(i);
                gameOver = gameOver | shift;
                if (shift) {
                    average = eval.calcAvgRec(b, deck, i, depth);
                    if (score < average) {
                        score = average;
                        move = i;
                    }
                }
            }
            if (!gameOver) {
                System.out.println("Game is over!");
                System.out.println();
                return -1;
            } else {
                return move;
            }
        }
    }
}
