package threes;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.io.ImageReader;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class IJtester {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path.concat("\\image\\");
        FileInfo f = new FileInfo();
        f.directory = path;
        f.fileName = "screen.jpeg";
        f.fileType = f.BGR;
        f.fileFormat = f.JPEG;
        f.height = 2280;
        f.width = 1080;
        ImageReader imageReader = new ImageReader(f);


        FileOpener fo = new FileOpener(f);
        ImagePlus image = fo.open(true);
        ImageProcessor cP = image.getProcessor();
        int[] pixels = (int[]) cP.getPixels();
        System.out.println(pixels.length);
//        for (int i = 0; i < 100; i++) {
//            int red = (int)(pixels & 0xff0000)>>16;
//            int green = (int)(pixels & 0x00ff00)>>8;
//            int blue = (int)(pixels & 0x0000ff);
//            System.out.println(red + " " + green + " " + blue);
//        }

//            InputStream in = new BufferedInputStream(new FileInputStream(path.concat("screen.bmp")));
//            System.out.println(imageReader.readPixels(in));
    }

}
