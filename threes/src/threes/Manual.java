package threes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Manual {

    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);

        int move = -1;
        int[][] b = {{2, 0, 0, 0},
                {6, 0, 0, 0},
                {3, 0, 0, 1},
                {2, 12, 1, 0}};
        int nc = 2;
        Board board = new Board(b, nc);
        Deck deck = new Deck(b);
        board.printBoard();

        ImProc image = new ImProc();
        image.reloadImage();


        move = Threes.findMove(board, 0, deck);
        board.swipe(move);
        board.insNext(image, move);
        System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
        board.printBoard();
        board.setNextCard(image.getNextTile());
        System.out.printf("Next card: %d\n",board.getNextCard());

/*        while (true) {
            move = Threes.findMove(board, 0, deck);
            if (move == -1) break;
            board.swipe(move);
            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
            board.printBoard();
            board.printNP();
            System.out.println("Next position?");
            int x = scan.nextInt();
            int y = scan.nextInt();
            board.insert(x,y);
            System.out.println("Next card?");
            nc = scan.nextInt();
            board.setNextCard(nc);
            System.out.println();
        }*/
    }
}
