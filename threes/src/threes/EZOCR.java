package threes;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import static java.lang.System.exit;
import static java.lang.System.getProperty;

public class EZOCR {
    private static int width = 1080;
    private static int height = 2200;
    private static int firstPix_x = 235;
    private static int firstPix_y = 700;
    private static int dist_x = 202;
    private static int dist_y = 270;
    private static int[] one = {102, 203, 255};
    private static int[] two = {255, 103, 128};
    private static int[] three = {254, 255, 255};
    private static int[] zero = {187, 217, 217};

    BufferedImage image = null;

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
        System.out.println("OCR error!");
        return -1;
    }

    public static void main(String[] args) {
        EZOCR ezOCR = new EZOCR();
        ezOCR.init();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                System.out.print(ezOCR.getTile(x, y) + " ");
            }
            System.out.println(ezOCR.getTile(3, y));
        }
    }



}
