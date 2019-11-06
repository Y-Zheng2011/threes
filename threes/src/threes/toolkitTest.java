package threes;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPath;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.exit;

public class toolkitTest {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try {
            String path = System.getProperty("user.dir");
            path = path.concat("\\threes\\image\\screen.png");
            File input = new File(path);
            BufferedImage bufferedImage = ImageIO.read(input);
            byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
            int rgb = pixels[1080*779+234];
            System.out.println(rgb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.printf("getRGB method takes: %d\n", end - start);
        System.out.println();

        start = System.currentTimeMillis();
        BufferedImage image = null;
        try {
            String p = System.getProperty("user.dir");
            p = p.concat("\\threes\\image\\screen.png");
            File input = new File(p);
            image = ImageIO.read(input);
//            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            image = ImageIO.read(input);
            System.out.println(image.getRGB(235, 780));
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.printf("getRGB method takes: %d\n", end - start);
        System.out.println();

        start = System.currentTimeMillis();
        try {
            String p = System.getProperty("user.dir");
            p = p.concat("\\threes\\image\\screen.png");
            BufferedImage i = ImageIO.read(Files.newInputStream(Paths.get(p)));
            System.out.println(i.getRGB(235, 780));
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.printf("getRGB method takes: %d\n", end - start);



    }
}
