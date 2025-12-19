package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ex1 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day9/input.txt")).trim();

        var coordinates = Pattern.compile("\\R")
                .splitAsStream(input)
                .filter(s -> !s.isBlank())
                .map(s -> s.split(","))
                .map(parts -> new long[]{Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim())})
                .toList();

        int n = coordinates.size();
        long[] x = new long[n];
        long[] y = new long[n];

        for (int i = 0; i < n; i++) {
            x[i] = coordinates.get(i)[0];
            y[i] = coordinates.get(i)[1];
        }

        long maxArea = IntStream.range(0, n).parallel().mapToLong(i -> {
            long localMax = 0;
            for (int j = i + 1; j < n; j++) {
                long width = Math.abs(x[i] - x[j]) + 1;
                long height = Math.abs(y[i] - y[j]) + 1;
                long area = width * height;
                if (area > localMax) {
                    localMax = area;
                }
            }
            return localMax;
        }).max().orElse(0);

        System.out.println(maxArea);
    }
}