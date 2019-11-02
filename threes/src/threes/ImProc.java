package threes;

import java.io.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
    private static int height = 2200;
    private static int firstPix_x = 235;
    private static int firstPix_y = 700;
    private static int dist_x = 202;
    private static int dist_y = 270;

    // RGB color of different cards.
    private static int[] one = {102, 203, 255};
    private static int[] two = {255, 103, 128};
    private static int[] three = {254, 255, 255};
    private static int[] zero = {187, 217, 217};

    /*
    Pixel position used to identify whether the next bonus card is
    a single possibility or multiple possibilities.
     */
    private static int[] nextIden = {374, 382};

    /*The first pixel of the mid line of central next tile.
    Use the mid line to do matching.
     */
    private static int[] next = {498, 382};

    private static int thresh = 10;
    //endregion


    private BufferedImage image = null;
    private boolean multiNext = false;
    private Map<String, Integer> map = new HashMap<>();


    public void init(){
        try {
            String path = System.getProperty("user.dir");
            path = path.concat("\\image\\screen.jpg");
            System.out.println(path);
            File input = new File(path);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        System.out.println("Finish reading image screen.png");
    }

    public int getTile(int x, int y) {
        int pix_x = x * dist_x + firstPix_x;
        int pix_y = y * dist_y + firstPix_y;

        int pix = image.getRGB(pix_x, pix_y);
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
        }
        System.out.println("Error! No matching tile!");
        return -1;
    }

    //Return true if there are 3 possible bonus cards.
    public boolean isMultiNext(){
        return multiNext;
    }

    public int[] getNextTile(int maxIndex){
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

    }

    private int match(BufferedImage image, int maxIndex) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < image.getWidth(); i++) {
            if ((image.getRGB(i,0) & 0xff) < thresh) {
                str.append('1');
            } else {
                str.append('0');
            }
        }

        try {
            String path = System.getProperty("user.dir");
            path = path.concat("\\image\\config.properties");
            OutputStream output = new FileOutputStream(path);

            Properties prop = new Properties();

            prop.setProperty(str.toString(), "0");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }

        return 0;

    }



}
