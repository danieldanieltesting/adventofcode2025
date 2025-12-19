package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ex2 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day1/input.txt")).trim();

        int position = 50;
        long count = 0;

        for (String line : input.strip().split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            char dir = line.charAt(0);
            int dist = Integer.parseInt(line.substring(1));

            int stepsToZero = (dir == 'L') ? position : (100 - position);
            if (stepsToZero == 0) stepsToZero = 100;

            if (dist >= stepsToZero) {
                count += (dist - stepsToZero) / 100 + 1;
            }

            position = (dir == 'L')
                    ? Math.floorMod(position - dist, 100)
                    : (position + dist) % 100;
        }

        System.out.println(count);
    }
}