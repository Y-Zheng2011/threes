package threes;

import ij.io.FileInfo;
import ij.io.ImageReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class IJtester {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path.concat("\\threes\\image\\");
        FileInfo f = new FileInfo();
        f.directory = path;
        f.fileName = "screen.bmp";
        f.fileType = f.RGB;
        f.fileFormat = f.BMP;
        f.height = 2280;
        f.width = 1080;
        ImageReader imageReader = new ImageReader(f);

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(path.concat("screen.bmp")));
            System.out.println(imageReader.readPixels(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
