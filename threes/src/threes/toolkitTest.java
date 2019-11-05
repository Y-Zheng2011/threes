package threes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class toolkitTest {
    public static void main(String[] args) {
        //BufferedImage bufferedImage = new BufferedImage(1080, 2280, BufferedImage.TYPE_INT_RGB);
        String path = System.getProperty("user.dir");
        path = path.concat("\\threes\\image\\screen.jpeg");
        Image img = Toolkit.getDefaultToolkit().getImage(path);
        System.out.println(img.);

    }
}
