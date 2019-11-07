package threes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ADBShell.screencap();

        ImProc image = new ImProc();
        image.init();

        Scanner scan = new Scanner(System.in);

        int move = -1;
        Board board = new Board(image.getBoard(), image.getNextTile());
        Deck deck = new Deck(image.getBoard());
        board.printBoard();

        while (true) {
            long start = System.currentTimeMillis();
            move = Threes.findMove(board, 0, deck);
            if (move == -1) break;
            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
            board.swipe(move);
            ADBShell.swipe(move);
//            TimeUnit.MILLISECONDS.sleep(700);
            ADBShell.screencap();
//            TimeUnit.MILLISECONDS.sleep(50);
            image.reloadImage();
            board.insNext(image, move);
            board.printBoard();
            long end = System.currentTimeMillis();
//            TimeUnit.MILLISECONDS.sleep(2000);
            board.setNextCard(image.getNextTile());
            System.out.printf("Next card: %d\n",board.getNextCard());
            System.out.println("Running time: " + (end - start) + " ms");
//            System.out.println("Continue?");
//            String w = scan.next();
            System.out.println();
        }
    }
}
