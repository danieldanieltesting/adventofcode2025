package day6;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ex2 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day6/input.txt")).trim();


        System.out.println(solve(input));
    }

    static BigInteger solve(String input) {
        List<String> rawLines = input.lines().collect(Collectors.toList());


        while (!rawLines.isEmpty() && rawLines.getLast().isBlank()) {
            rawLines.removeLast();
        }

        if (rawLines.isEmpty()) return BigInteger.ZERO;

        int height = rawLines.size();
        int width = rawLines.stream().mapToInt(String::length).max().orElse(0);

        char[][] grid = new char[height][width];
        for (int r = 0; r < height; r++) {
            String line = rawLines.get(r);
            for (int c = 0; c < width; c++) {
                grid[r][c] = (c < line.length()) ? line.charAt(c) : ' ';
            }
        }

        boolean[] isSeparator = new boolean[width];
        for (int c = 0; c < width; c++) {
            boolean empty = true;
            for (int r = 0; r < height; r++) {
                if (grid[r][c] != ' ') {
                    empty = false;
                    break;
                }
            }
            isSeparator[c] = empty;
        }

        BigInteger grandTotal = BigInteger.ZERO;
        int blockStart = -1;

        for (int c = 0; c <= width; c++) {
            boolean isSep = (c == width) || isSeparator[c];

            if (!isSep) {
                if (blockStart == -1) {
                    blockStart = c;
                }
            } else {
                if (blockStart != -1) {
                    grandTotal = grandTotal.add(calculateBlock(grid, height, blockStart, c - 1));
                    blockStart = -1;
                }
            }
        }

        return grandTotal;
    }

    private static BigInteger calculateBlock(char[][] grid, int height, int startCol, int endCol) {
        char operator = '+';
        int operatorRow = height - 1;

        for (int c = startCol; c <= endCol; c++) {
            char ch = grid[operatorRow][c];
            if (ch == '+' || ch == '*') {
                operator = ch;
                break;
            }
        }

        BigInteger result = (operator == '*') ? BigInteger.ONE : BigInteger.ZERO;
        boolean isMultiplication = (operator == '*');
        boolean hasNumbers = false;

        for (int c = endCol; c >= startCol; c--) {
            StringBuilder numStr = new StringBuilder();

            for (int r = 0; r < operatorRow; r++) {
                char ch = grid[r][c];
                if (Character.isDigit(ch)) {
                    numStr.append(ch);
                }
            }

            if (!numStr.isEmpty()) {
                BigInteger num = new BigInteger(numStr.toString());
                if (isMultiplication) {
                    result = result.multiply(num);
                } else {
                    result = result.add(num);
                }
                hasNumbers = true;
            }
        }

        return hasNumbers ? result : BigInteger.ZERO;
    }
}