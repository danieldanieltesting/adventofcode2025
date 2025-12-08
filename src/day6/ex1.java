package day6;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ex1 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day6/input.txt")).trim();

        System.out.println(solve(input));
    }

    static BigInteger solve(String input) {
        List<String> lines = input.lines()
                .filter(s -> !s.isBlank())
                .toList();

        if (lines.isEmpty()) return BigInteger.ZERO;

        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);
        boolean[] colHasContent = new boolean[width];

        for (String line : lines) {
            int len = line.length();
            for (int c = 0; c < len; c++) {
                if (line.charAt(c) != ' ') {
                    colHasContent[c] = true;
                }
            }
        }

        BigInteger grandTotal = BigInteger.ZERO;
        int start = -1;

        for (int c = 0; c <= width; c++) {
            boolean isContent = c < width && colHasContent[c];
            if (isContent) {
                if (start == -1) start = c;
            } else {
                if (start != -1) {
                    grandTotal = grandTotal.add(processBlock(lines, start, c));
                    start = -1;
                }
            }
        }

        return grandTotal;
    }

    static BigInteger processBlock(List<String> lines, int start, int end) {
        List<BigInteger> numbers = new ArrayList<>();
        String operator = "+";
        int lastLineIdx = lines.size() - 1;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (start >= line.length()) continue;

            int effectiveEnd = Math.min(end, line.length());
            String segment = line.substring(start, effectiveEnd).trim();

            if (!segment.isEmpty()) {
                if (i == lastLineIdx) {
                    operator = segment;
                } else {
                    numbers.add(new BigInteger(segment));
                }
            }
        }

        if (numbers.isEmpty()) return BigInteger.ZERO;

        BigInteger result = operator.contains("*") ? BigInteger.ONE : BigInteger.ZERO;
        boolean isMult = operator.contains("*");

        for (BigInteger num : numbers) {
            if (isMult) {
                result = result.multiply(num);
            } else {
                result = result.add(num);
            }
        }

        return result;
    }
}