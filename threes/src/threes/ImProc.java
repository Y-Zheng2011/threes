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
    private static int firstPix_x = 200;
    private static int firstPix_y = 876;
    private static int dist_x = 202;
    private static int dist_y = 270;

    // RGB color of different cards.
    private static int[] one = {102, 204, 255};
    private static int[] two = {255, 102, 128};
    private static int[] three = {252, 252, 252};
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
    private static int cardWidth = 169;
    //endregion


    private BufferedImage image = null;
    private String path;
    private Map<Long, Integer> bonusMap = new HashMap<>();
    private Map<Long, Integer> cardMap = new HashMap<>();
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

    public void reloadImage(String imgName) {
        try {
            path = System.getProperty("user.dir").concat("\\threes\\image\\");
            File input = new File(path.concat(imgName));
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String args) {
        reloadImage("screen.bmp");
        loadTxt(args);
        board = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = getCard(j, i);
            }
        }
        nextCard = imProcNextCard();
        System.out.println("Finish reading image screen.bmp");
        reloadImage("boardCard.bmp");
        int index = 3;
        try {
            File f = new File(path.concat("boardCard.txt"));
            FileWriter fw = new FileWriter(f, false);
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 4; i++) {
                    int x = i * dist_x + firstPix_x;
                    int y = j * dist_y + firstPix_y;
                    long val = cardVal(x, y);
                    cardMap.put(val, index);
                    fw.write(val + " " + index + "\n");
                    index++;
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finish processing image boardCard.txt");
    }

    public int getNextCard() {
        return nextCard;
    }

    public int imProcNextCard() {
        isMultiBonus = false;
        isBonus = false;
        long m = bonusVal();
        if (isBonus(m)) {
            int pixFlag = image.getRGB(nextIden[0], nextIden[1]);
            if ((pixFlag & 0xff) != 250) {
                isMultiBonus = true;
            }
            nextCard = bonusMap.get(m);
        } else {
            int pix = image.getRGB(next[0], next[1]);
            nextCard = match(pix);
        }
        return nextCard;
    }

    //Return true if card(x,y) equals new card
    public boolean findIns(int x, int y) {
        if (isBonus) {
            if (isMultiBonus) {
                int index = matchBonus(x, y);
                if (index == -1) {
                    return false;
                } else if (nextCard == index) {
                    return (nextCard == matchBonus(x, y));
                } else if (nextCard - 1 == index) {
                    nextCard--;
                    return true;
                } else if (nextCard + 1 == index) {
                    nextCard++;
                    return true;
                }
            } else {
                return (nextCard == matchBonus(x, y));
            }
        }
        return (nextCard == getCard(x, y));
    }

    public boolean isBonus(long m) {
        if (m != 0) {
            isBonus = true;
            if (!bonusMap.containsKey(m)) {
                maxIndex++;
                addBonus(m, maxIndex);
            }
            return true;
        }
        return false;
    }

    private void loadTxt(String args) {
        try {
            File f = new File(path.concat("nextCard.txt"));
            Scanner scan = new Scanner(f);
            while (scan.hasNext()) {
                maxIndex++;
                long card = scan.nextLong();
                int val = scan.nextInt();
                bonusMap.put(card, val);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (args == "y") {
                int index = 3;
                File f = new File(path.concat("boardCard.txt"));
                FileWriter fw = new FileWriter(f, false);
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < 4; i++) {
                        int x = i * dist_x + firstPix_x;
                        int y = j * dist_y + firstPix_y;
                        long val = cardVal(x, y);
                        cardMap.put(val, index);
                        fw.write(val + " " + index + "\n");
                        index++;
                    }
                }
                fw.close();
            } else {
                File f = new File(path.concat("boardCard.txt"));
                Scanner scan = new Scanner(f);
                while (scan.hasNext()) {
                    long card = scan.nextLong();
                    int val = scan.nextInt();
                    cardMap.put(card, val);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish processing image boardCard.txt");
    }

    private void addBonus(long v, int i) {
        try {
            bonusMap.put(v, i);
            File f = new File(path.concat("nextCard.txt"));
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
        } else if (pix_r > three[0] && pix_g > three[1] && pix_b > three[2]) {
            return 3;
        } else {
            System.out.println("Error! No matching tile!");
            return -1;
        }
    }

    private int matchBonus(int x, int y) {
        int pix_x = x * dist_x + firstPix_x;
        int pix_y = y * dist_y + firstPix_y;
        long val = cardVal(pix_x, pix_y);
        if (bonusMap.containsKey(val)){
            return bonusMap.get(val);
        }
        return -1;
    }

    private long cardVal(int x, int y) {
        BufferedImage img = image.getSubimage(x, y, cardWidth, 1);
        long m = 0;
        for (int i = 0; i < 63; i++) {
            if ((img.getRGB(i, 0) & 0xff) < thresh) {
                m = (m << 1) + 1;
            } else {
                m = m << 1;
            }
        }
        return m;
    }

    private long bonusVal() {
        BufferedImage img = image.getSubimage(next[0], next[1], bonusWidth, 1);
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
        }
        return m;
    }
}