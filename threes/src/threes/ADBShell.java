package threes;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ADBShell {

    public static void screenCap() {
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec("adb shell screencap -p /sdcard/threes_ai/screen.bmp");
            process.waitFor();
            process = Runtime.getRuntime().exec("adb pull /sdcard/threes_ai/screen.bmp threes/image/screen.bmp");
            process.waitFor();
            process = Runtime.getRuntime().exec("adb shell rm /sdcard/threes_ai/screen.bmp");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void swipe(int i) {
        try {
            Scanner scan = new Scanner(System.in);
            if (i == 0) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 1000 1000 100 1000 50");
                System.out.println("Continue?");
                String w = scan.next();
                System.out.println();
//                process.waitFor(2000, MILLISECONDS);
            } else if (i == 1) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 300 500 1300 50");
                System.out.println("Continue?");
                String w = scan.next();
                System.out.println();
//                process.waitFor(2000, MILLISECONDS);
            } else if (i == 2) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 100 1000 1000 1000 50");
                System.out.println("Continue?");
                String w = scan.next();
                System.out.println();
//                process.waitFor(2000, MILLISECONDS);
            } else if (i == 3) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 1300 500 300 50");
                System.out.println("Continue?");
                String w = scan.next();
                System.out.println();
//                process.waitFor(2000, MILLISECONDS);
            } else {
                System.out.println("Game over!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
