package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ex1 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day1/input.txt")).trim();

        int position = 50;
        int count = 0;

        for (String line : input.strip().split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            char dir = line.charAt(0);
            int dist = Integer.parseInt(line.substring(1));
            position = switch (dir) {
                case 'L' -> Math.floorMod(position - dist, 100);
                case 'R' -> (position + dist) % 100;
                default -> throw new IllegalArgumentException("Invalid direction: " + dir);
            };
            if (position == 0) count++;
        }

        System.out.println(count);
    }
}