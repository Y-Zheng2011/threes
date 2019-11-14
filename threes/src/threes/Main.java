package threes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

    //Rebuild boardCard.txt file if args[0] == "y"
    public static void main(String[] args) {
//        Threes.run(args[0]);
        ImProc imProc = new ImProc();
        imProc.init(args[0]);
        int move = -1;
        Board board = new Board(imProc);
        Deck deck = new Deck(imProc.getBoard());
        board.printBoard();
    }
}
