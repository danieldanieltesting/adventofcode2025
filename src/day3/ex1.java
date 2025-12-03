package day3;

import java.nio.file.*;

public class ex1 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day3/input.txt")).trim();

        long total = 0;
        for (String line : input.split("\n")) {
            int n = line.length();
            int maxRight = line.charAt(n - 1) - '0';
            int maxJolt = 0;
            for (int i = n - 2; i >= 0; i--) {
                int digit = line.charAt(i) - '0';
                maxJolt = Math.max(maxJolt, digit * 10 + maxRight);
                maxRight = Math.max(maxRight, digit);
            }
            total += maxJolt;
        }
        System.out.println(total);
    }
}