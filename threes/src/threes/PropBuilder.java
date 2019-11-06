package threes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class PropBuilder {

    private static int thresh = 128;
    private static int[] next = {498, 462};
    private static int width = 85;


    public long isBonus(BufferedImage image) {
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

    public static void main(String[] args) {
        BufferedImage image;
        PropBuilder t = new PropBuilder();
        int num = 3;
        try {
            for (int i = 1; i <5; i++) {
                num = num * 2;
                String sb = System.getProperty("user.dir") + "\\threes\\image\\screen" + i + ".bmp";
                File input = new File(sb);
                image = ImageIO.read(input);
                long v = t.isBonus(image.getSubimage(next[0], next[1], width, 1));
                t.addBonus(v, num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
