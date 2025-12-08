package day7;

import java.nio.file.*;
import java.util.*;

public class ex1 {
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

        Set<Integer> activeBeams = new HashSet<>();
        activeBeams.add(startCol);

        int totalSplits = 0;

        for (int r = startRow + 1; r < rows; r++) {
            String line = lines[r];
            Set<Integer> nextBeams = new HashSet<>();

            for (int col : activeBeams) {
                if (col >= 0 && col < cols) {
                    if (line.charAt(col) == '^') {
                        totalSplits++;
                        if (col > 0) nextBeams.add(col - 1);
                        if (col < cols - 1) nextBeams.add(col + 1);
                    } else {
                        nextBeams.add(col);
                    }
                }
            }

            activeBeams = nextBeams;
            if (activeBeams.isEmpty()) break;
        }

        System.out.println(totalSplits);
    }
}