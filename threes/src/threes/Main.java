package threes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ADBShell.screencap();

        ImProc imProc = new ImProc();
        imProc.init();

        Scanner scan = new Scanner(System.in);

        int move = -1;
        int[] nextPos;
        Board board = new Board(imProc.getBoard(), imProc.getNextTile());
        Deck deck = new Deck(imProc.getBoard());
        board.printBoard();

        while (true) {
            long start = System.currentTimeMillis();
            move = Threes.findMove(board, 0, deck);
            if (move == -1) break;
            board.swipe(move);
            ADBShell.swipe(move);
            ADBShell.screencap();
            nextPos = imProc.nextPos(move);
            board.insCard(board.getNextCard(),nextPos[0], nextPos[1]);
            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
            board.printBoard();
            long end = System.currentTimeMillis();
            System.out.println("Running time: " + (end - start) + " ms");
            System.out.println();
            board.setNextCard(imProc.getNextTile());
        }
    }
}
