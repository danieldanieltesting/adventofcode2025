package day3;

import java.nio.file.*;
import java.util.*;

public class ex2 {

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day3/input.txt")).trim();

        long total = Arrays.stream(input.split("\\R"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .mapToLong(ex2::selectMaxTwelveDigits)
                .sum();

        System.out.println(total);
    }

    static long selectMaxTwelveDigits(String s) {
        int n = s.length();
        int k = 12;
        StringBuilder result = new StringBuilder(k);
        int start = 0;

        for (int i = 0; i < k; i++) {
            int end = n - k + i;
            int maxIdx = start;
            for (int j = start + 1; j <= end; j++) {
                if (s.charAt(j) > s.charAt(maxIdx)) {
                    maxIdx = j;
                }
            }
            result.append(s.charAt(maxIdx));
            start = maxIdx + 1;
        }

        return Long.parseLong(result.toString());
    }
}