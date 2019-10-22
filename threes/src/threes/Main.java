package threes;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the current board:");
        int[][] tmp = new int[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                tmp[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Input the next card:");
        int nc = scanner.nextInt();
        Board board = new Board(tmp, nc);
        board.printBoard();
    }
}
