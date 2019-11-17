package threes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

    //Rebuild boardCard.txt file if args[0] == "y"
    public static void main(String[] args) {
        Threes.run(args[0]);
//        ImProc im = new ImProc();
//        im.init(args[0]);
    }
}
