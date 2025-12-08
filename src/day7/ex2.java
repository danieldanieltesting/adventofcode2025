package day7;

import java.nio.file.*;
import java.util.*;
import java.math.BigInteger;

public class ex2 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day7/input.txt")).trim();
        String[] lines = input.split("\\R");
        int rows = lines.length;
        int cols = lines[0].length();

        int startRow = 0;
        int startCol = 0;
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (lines[r].charAt(c) == 'S') {
                    startRow = r;
                    startCol = c;
                    break outer;
                }
            }
        }

        Map<Integer, BigInteger> timelines = new HashMap<>();
        timelines.put(startCol, BigInteger.ONE);

        for (int r = startRow + 1; r < rows; r++) {
            String line = lines[r];
            Map<Integer, BigInteger> nextTimelines = new HashMap<>();

            for (var entry : timelines.entrySet()) {
                int col = entry.getKey();
                BigInteger count = entry.getValue();

                if (col >= 0 && col < cols) {
                    if (line.charAt(col) == '^') {
                        if (col > 0) {
                            nextTimelines.merge(col - 1, count, BigInteger::add);
                        }
                        if (col < cols - 1) {
                            nextTimelines.merge(col + 1, count, BigInteger::add);
                        }
                    } else {
                        nextTimelines.merge(col, count, BigInteger::add);
                    }
                }
            }

            timelines = nextTimelines;
            if (timelines.isEmpty()) break;
        }

        BigInteger totalTimelines = timelines.values().stream()
                .reduce(BigInteger.ZERO, BigInteger::add);
        System.out.println(totalTimelines);
    }
}