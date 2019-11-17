package threes;

public class Threes {

    // Find move for the current board, search depth and deck.
    public static int findMove(Board board, int depth, Deck deck) {
        int move = -1;
        float average, score = -1000.0f;
        boolean shift, gameOver = false;
        Evaluator eval = new Evaluator();
        if (deck.getDeckNum() == 0) {
            deck.reset();
        }
        if (depth == 0) {
            for (int i = 0; i < 4; i++) {
                Board b = new Board(board);
                shift = b.swipe(i);
                gameOver = gameOver || shift;
                if (shift) {
                    average = eval.calcAvgScore(b, i);
                    if (score < average) {
                        score = average;
                        move = i;
                    }
                }
            }
            return move;
        } else {
            for (int i = 0; i < 4; i++) {
                Board b = new Board(board);
                shift = b.swipe(i);
                gameOver = gameOver || shift;
                if (shift) {
                    average = eval.calcAvgRec(b, deck, i, depth);
                    if (score < average) {
                        score = average;
                        move = i;
                    }
                }
            }
            return move;
        }
    }


    public static void run(String args) {

        ADBShell.screencap();
        ImProc image = new ImProc();
        image.init(args);

        int move = -1;
        Board board = new Board(image);
        Deck deck = new Deck(image.getBoard());
        board.printBoard();
//        Scanner scan = new Scanner(System.in);

        while (true) {
            long start = System.currentTimeMillis();
            move = Threes.findMove(board, 1, deck);
            if (move == -1) {
                System.out.println("No more moves!");
                break;
            }
            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
            board.swipe(move);
            ADBShell.swipe(move);
            ADBShell.screencap();

            image.reloadImage("screen.bmp");
            image.setMultiBonus(board.getMultiNext());
            if (board.getMultiNext() > 0) {
                image.setIsBonus(true);
            }
            board.insNext(image, move);
            board.printBoard();
            long end = System.currentTimeMillis();
            if (!image.isBonus()) {
                if (deck.isEmpty(board.getNextCard())) {
                    deck.reset();
                } else {
                    deck.draw(board.getNextCard());
                }
            }
            board.setNextCard(image.imProcNextCard());
            board.setMultiNext(image.multiBonus());
            System.out.printf("Next card: %d\n",board.getNextCard());
            System.out.println("Running time: " + (end - start) + " ms");
//            System.out.println("Continue?");
//            String w = scan.next();
//            System.out.println();
        }
    }
}
