package threes;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ADBShell {

    public static void screencap() {
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec("adb shell screencap -p /sdcard/threes_ai/screen.png");
            process.waitFor();
            process = Runtime.getRuntime().exec("adb pull /sdcard/threes_ai/screen.png threes/image/screen.png");
            process.waitFor();
            process = Runtime.getRuntime().exec("adb shell rm /sdcard/threes_ai/screen.png");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void swipe(int i) {
        try {
            if (i == 0) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 1000 1000 100 1000 50");
                process.waitFor(1000, MILLISECONDS);
            } else if (i == 1) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 300 500 1300 50");
                process.waitFor(1000, MILLISECONDS);
            } else if (i == 2) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 100 1000 1000 1000 50");
                process.waitFor(1000, MILLISECONDS);
            } else if (i == 3) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 1300 500 300 50");
                process.waitFor(1000, MILLISECONDS);
            } else {
                System.out.println("Game over!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
