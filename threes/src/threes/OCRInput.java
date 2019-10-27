package threes;

import com.android.ddmlib.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class OCRInput{

    public static void getScreen(){
        String filepath = "C:/Users/Yuanhua/Desktop/CS61B/threes/image/screen.png";
        try {
            AndroidDebugBridge.init(false);
            AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();

            int count = 0;
            while (!bridge.hasInitialDeviceList()) {
                try {
                    Thread.sleep(100);
                    count++;
                } catch (InterruptedException ignored) {
                }
                if (count > 100) {
                    System.err.println("Timeout getting device list!");
                    return;
                }
            }

            IDevice[] device = bridge.getDevices();
            RawImage raw = device[0].getScreenshot();
            BufferedImage image;
            image = new BufferedImage(raw.width, raw.height, BufferedImage.TYPE_INT_ARGB);
            Dimension size = new Dimension();
            size.setSize(image.getWidth(), image.getHeight());
            int index = 0;
            int indexInc = raw.bpp >> 3;
            for (int y = 0; y < raw.height; y++) {
                for (int x = 0; x < raw.width; x++, index += indexInc) {
                    int value = raw.getARGB(index);
                    image.setRGB(x, y, value);
                }
            }
            ImageIO.write(image, "png", new File(filepath));

        } catch (TimeoutException | IOException | AdbCommandRejectedException e) {
            e.printStackTrace();
        } finally {
            AndroidDebugBridge.terminate();
        }
    }

    public static void main(String[] args) {
        OCRInput.getScreen();
    }


}