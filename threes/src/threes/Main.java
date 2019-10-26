package threes;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long start, end;
        System.out.println("Input the current board:");
        int[][] tmp = new int[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                tmp[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Input the next card:");
        int nc = scanner.nextInt();
        int move = -1, x, y;
        Board board = new Board(tmp, nc);
        Deck deck = new Deck(tmp);
        board.printBoard();
        while (true) {
            start = System.currentTimeMillis();
            move = Threes.findMove(board, 4, deck);
            if (move == -1) break;
            board.swipe(move);
            System.out.printf("Move: %d (0: left, 1: down, 2: right, 3: up)\n",move);
            board.printBoard();
            end = System.currentTimeMillis();
            System.out.println("Running time: " + (end - start) + " ms");
            System.out.println();
            System.out.println("Input the index of new card:");
            x = scanner.nextInt();
            y = scanner.nextInt();
            board.insCard(board.getNextCard(),x,y);
            System.out.println("Input the next card:");
            nc = scanner.nextInt();
            board.setNextCard(nc);
        }
    }
}
