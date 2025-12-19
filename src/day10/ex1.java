package day10;

import java.util.*;
import java.nio.file.*;
import java.util.regex.*;


public class ex1 {
    record Machine(long target, long[] buttonMasks) {}

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day10/input.txt")).trim();

        long totalPresses = input.lines()
                .map(ex1::parseMachine)
                .mapToLong(ex1::solve)
                .sum();

        System.out.println(totalPresses);
    }

    private static Machine parseMachine(String line) {
        Matcher lightMatcher = Pattern.compile("\\[([.#]+)\\]").matcher(line);
        if (!lightMatcher.find()) throw new IllegalArgumentException();
        String lights = lightMatcher.group(1);
        long target = 0;
        for (int i = 0; i < lights.length(); i++) {
            if (lights.charAt(i) == '#') {
                target |= (1L << i);
            }
        }

        List<Long> buttons = new ArrayList<>();
        Matcher buttonMatcher = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        while (buttonMatcher.find()) {
            long mask = 0;
            String[] indices = buttonMatcher.group(1).split(",");
            for (String idx : indices) {
                mask |= (1L << Integer.parseInt(idx.trim()));
            }
            buttons.add(mask);
        }

        return new Machine(target, buttons.stream().mapToLong(Long::longValue).toArray());
    }

    private static long solve(Machine machine) {
        int m = machine.buttonMasks.length;
        long target = machine.target;
        long[] btnMasks = machine.buttonMasks;

        int minWeight = Integer.MAX_VALUE;
        long currentState = 0;

        if (target == 0) return 0;

        for (int i = 1; i < (1 << m); i++) {
            int bitToFlip = Integer.numberOfTrailingZeros(i);
            currentState ^= btnMasks[bitToFlip];

            if (currentState == target) {
                int grayCode = i ^ (i >> 1);
                int weight = Integer.bitCount(i);

                int actualWeight = 0;
                int currentGray = i ^ (i >> 1);
                minWeight = Math.min(minWeight, Integer.bitCount(currentGray));
            }
        }

        long currentStateGray = 0;
        int min = (target == 0) ? 0 : Integer.MAX_VALUE;

        for (int i = 1; i < (1 << m); i++) {
            int bitToFlip = Integer.numberOfTrailingZeros(i);
            currentStateGray ^= btnMasks[bitToFlip];

            if (currentStateGray == target) {
                min = Math.min(min, Integer.bitCount(i ^ (i >> 1)));
            }
        }

        return min;
    }
}