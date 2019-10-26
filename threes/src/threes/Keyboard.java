package threes;

import java.awt.*;

import static java.awt.event.KeyEvent.*;

public class Keyboard {

    public static void press(int i) {
        try {
            Robot kb = new Robot();
            if (i == 0) {
                kb.keyPress(VK_LEFT);
            } else if (i == 1) {
                kb.keyPress(VK_DOWN);
            } else if (i == 2) {
                kb.keyPress(VK_RIGHT);
            } else if (i == 3) {
                kb.keyPress(VK_UP);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
