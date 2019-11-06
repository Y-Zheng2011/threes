package threes;

import java.io.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import static java.lang.System.exit;

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
    private static int thresh = 10;
    private static int bonusWidth = 85;
    //endregion


    private BufferedImage image = null;
    private boolean multiNext = false;
    private Map<String, Integer> map = new HashMap<>();
    private int[][] board;
    private boolean isBonus = false;
    private int[] nextTile = {-1, -1, -1};

    public int[][] getBoard(){
        return board;
    }

    public void setBonus(boolean bonus) {
        isBonus = bonus;
    }

    public void load(){
        try {
            String path = System.getProperty("user.dir");
            path = path.concat("\\threes\\image\\screen.bmp");
            File input = new File(path);
            image = ImageIO.read(input);
//            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public void init(){
        load();
        board = new int[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                board[i][j] = getTile(j, i);
            }
        }
        nextTile[0] = getNextTile();
        System.out.println("Finish reading image screen.bmp");
    }

    private int getTile(int x, int y) {
        int pix_x = x * dist_x + firstPix_x;
        int pix_y = y * dist_y + firstPix_y;

        int pix = image.getRGB(pix_x, pix_y);
        return match(pix);
    }


    //Return true if there are 3 possible bonus cards.
    public boolean isMultiNext(){
        return multiNext;
    }

    public int getNextTile() {
        int pix = image.getRGB(next[0], next[1]);
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
            System.exit(1);
            return -1;
        }
    }

    public boolean findIns(int card, int x, int y) {
        return (card == getTile(x, y));
    }

/*    public int[] getNextTile(int maxIndex){
        int pixFlag = image.getRGB(nextIden[0], nextIden[1]);
        if ((pixFlag & 0xff) != 250) {
            multiNext = true;
        }
        if (multiNext) {
            int[] ret = new int[3];
            ret[1] = match(image.getSubimage(next[0], next[1], 83, 1), maxIndex);
            ret[0] = ret[1] - 1;
            ret[2] = ret[0] + 1;
            return ret;
        } else {
            return new int[]{0};
        }

    }*/

    public void addBonus(long v, int num) {
        try {
            String path = System.getProperty("user.dir");
            path = path.concat("\\threes\\image\\bonusCard.txt");
            File f = new File(path);
            FileWriter fw = new FileWriter(f,true);
            fw.write(v + " " + num + "\n");
            fw.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private long isBonus(BufferedImage image) {
        long ret = -1;
        long m = 0;
        for (int i = 0; i < 63; i++) {
            if ((image.getRGB(i,0) & 0xff) < thresh) {
                m = (m << 1) + 1;
            } else {
                m = m << 1;
            }
        }
        if (m != 0) {
            ret = m;
        }
        return ret;
    }

//    private int match(BufferedImage image, int maxIndex) {
//        StringBuilder str = new StringBuilder();
//
//        for (int i = 0; i < image.getWidth(); i++) {
//            if ((image.getRGB(i,0) & 0xff) < thresh) {
//                str.append('1');
//            } else {
//                str.append('0');
//            }
//        }
//
//        try {
//            String path = System.getProperty("user.dir");
//            path = path.concat("\\image\\config.properties");
//            OutputStream output = new FileOutputStream(path);
//
//            Properties prop = new Properties();
//
//            prop.setProperty(str.toString(), "0");
//
//            // save properties to project root folder
//            prop.store(output, null);
//
//            System.out.println(prop);
//
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
//
//        return 0;
//
//    }



}
