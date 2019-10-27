package threes;

import java.io.IOException;

public class ADBshell{

    public static void screencap() {
        try {
            Process process = Runtime.getRuntime().exec("adb shell screencap -p /sdcard/screen.png");
            process = Runtime.getRuntime().exec("adb pull /sdcard/screen.png image/screen.png");
            process = Runtime.getRuntime().exec("adb shell rm /sdcard/screen.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void swipe(int i) {
        try {
            if (i == 0) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 1000 1000 100 1000 50");
            } else if (i == 1) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 300 500 1300 50");
            } else if (i == 2) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 100 1000 1000 1000 50");
            } else if (i == 3) {
                Process process = Runtime.getRuntime().exec("adb shell input swipe 500 1300 500 300 50");
            } else {
                System.out.println("Game over!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start, end;
        start = System.currentTimeMillis();
        ADBshell.screencap();
        end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start) + " ms");
    }
}
