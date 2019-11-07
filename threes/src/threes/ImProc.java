package threes;

import java.io.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImProc {

    //region Constants
    /*
    Basic info of the board. firstPix constants are the pixel used to identify the top left card.
    dist constants are the distance between two same sides of two adjacent tiles.
    Note these only work for OnePlus 6.
     */
    private static int width = 1080;
    private static int height = 2280;
    private static int firstPix_x = 235;
    private static int firstPix_y = 780;
    private static int dist_x = 202;
    private static int dist_y = 270;

    // RGB color of different cards.
    private static int[] one = {102, 204, 255};
    private static int[] two = {255, 102, 128};
    private static int[] three = {254, 255, 255};
    private static int[] zero = {187, 217, 217};

    /*
    Pixel position used to identify whether the next bonus card is
    a single possibility or multiple possibilities.
     */
    private static int[] nextIden = {374, 462};

    /*The first pixel of the mid line of central next tile.
    Use the mid line to do matching.
     */
    private static int[] next = {498, 462};
    private static int thresh = 128;
    private static int bonusWidth = 85;
    //endregion


    private BufferedImage image = null;
    private String path;
    private Map<Long, Integer> map = new HashMap<>();
    private int[][] board;
    private int maxIndex = 3;
    private boolean isBonus;
    private boolean isMultiBonus;
    private int nextCard = 0;


    public int[][] getBoard() {
        return board;
    }

    public boolean isBonus() {
        return isBonus;
    }

    //Return true if there are 3 possible bonus cards.
    public boolean isMultiBonus() {
        return isMultiBonus;
    }

    public void reloadImage() {
        try {
            path = System.getProperty("user.dir").concat("\\threes\\image\\");
            File input = new File(path.concat("screen.bmp"));
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        reloadImage();
        loadTxt();
        board = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = getCard(j, i);
            }
        }
        nextCard = getNextCard();
        System.out.println("Finish reading image screen.bmp");
    }


    public int getNextCard() {
        isMultiBonus = false;
        isBonus = false;
        long m = bonusVal();
        if (isBonus(m)) {
            int pixFlag = image.getRGB(nextIden[0], nextIden[1]);
            if ((pixFlag & 0xff) != 250) {
                isMultiBonus = true;
            }
            nextCard = map.get(m);
        } else {
            int pix = image.getRGB(next[0], next[1]);
            nextCard = match(pix);
        }
        return nextCard;
    }

    //Return if card(x,y) equals new card
    public boolean findIns(int x, int y) {
        if (isBonus) {
            if (isMultiBonus) {
                return (nextCard == )
            } else {

            }
        }
        return (nextCard == getCard(x, y));
    }

    public boolean isBonus(long m) {
        if (m != -1) {
            isBonus = true;
            if (map.containsKey(m)) {
                return true;
            } else {
                addBonus(m, maxIndex + 1);
                return true;
            }
        } else {
            return false;
        }
    }

    private long bonusVal() {
        BufferedImage img = image.getSubimage(next[0], next[1], bonusWidth, 1);
        long ret = 0;
        long m = 0;
        for (int i = 0; i < 63; i++) {
            if ((img.getRGB(i, 0) & 0xff) < thresh) {
                m = (m << 1) + 1;
            } else {
                m = m << 1;
            }
        }
        if (m != 0) {
            isBonus = true;
            ret = m;
        }
        return ret;
    }

    private void addBonus(long v, int i) {
        try {
            map.put(v, i);
            File f = new File(path.concat("bonusCard.txt"));
            FileWriter fw = new FileWriter(f, true);
            fw.write(v + " " + i + "\n");
            fw.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private int getCard(int x, int y) {
        int pix_x = x * dist_x + firstPix_x;
        int pix_y = y * dist_y + firstPix_y;

        int pix = image.getRGB(pix_x, pix_y);
        return match(pix);
    }

    //Identify the primitive type (0, 1, 2 or 3) of the card with pixel pix
    private int match(int pix) {
        int pix_r = pix >> 16 & 0xff;
        int pix_g = pix >> 8 & 0xff;
        int pix_b = pix & 0xff;

        if (pix_r == zero[0] && pix_g == zero[1] && pix_b == zero[2]) {
            return 0;
        } else if (pix_r == one[0] && pix_g == one[1] && pix_b == one[2]) {
            return 1;
        } else if (pix_r == two[0] && pix_g == two[1] && pix_b == two[2]) {
            return 2;
        } else if (pix_r == three[0] && pix_g == three[1] && pix_b == three[2]) {
            return 3;
        } else {
            System.out.println("Error! No matching tile!");
            return -1;
        }
    }

    private int matchBonus() {

    }

    private void loadTxt() {
        try {
            File f = new File(path.concat("bonusCard.txt"));
            Scanner scan = new Scanner(f);
            while (scan.hasNext()) {
                maxIndex++;
                long card = scan.nextLong();
                int val = scan.nextInt();
                map.put(card, val);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}