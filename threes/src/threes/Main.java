package threes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ADBshell.screencap();

        ImProc imProc = new ImProc();
        imProc.init();

        int[][] tmp = new int[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                tmp[i][j] = imProc.getTile(j, i);
            }
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Input next tile: ");
        int nc = scan.nextInt();
        System.out.println("Is it a bonus card? Y/N");
        char ch = scan.next().charAt(0);

        if (ch == 'y' || ch == 'Y') {

        }

//        int nc = imProc.getNextTile();
        int move = -1, x, y;
        Board board = new Board(tmp, nc);
        Deck deck = new Deck(tmp);
        board.printBoard();
        if (nc > board.getMaxCard() / 8) {
            ImProc
        }


//        while (true) {
//            start = System.currentTimeMillis();
//            move = Threes.findMove(board, 4, deck);
//            if (move == -1) break;
//            board.swipe(move);
//            ADBshell.swipe(move);
//            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
//            board.printBoard();
//            end = System.currentTimeMillis();
//            System.out.println("Running time: " + (end - start) + " ms");
//            System.out.println();
//            System.out.println("Input the index of new card:");
//            x = scanner.nextInt();
//            y = scanner.nextInt();
//            board.insCard(board.getNextCard(),x,y);
//            System.out.println("Input the next card:");
//            nc = scanner.nextInt();
//            board.setNextCard(nc);
//        }
    }
}
