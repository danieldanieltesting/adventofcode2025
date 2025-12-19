package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ex2 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day9/input.txt")).trim();

        List<long[]> coordsList = Pattern.compile("\\R")
                .splitAsStream(input)
                .filter(s -> !s.isBlank())
                .map(s -> s.split(","))
                .map(parts -> new long[]{Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim())})
                .toList();

        int n = coordsList.size();
        long[] px = new long[n];
        long[] py = new long[n];

        for (int i = 0; i < n; i++) {
            px[i] = coordsList.get(i)[0];
            py[i] = coordsList.get(i)[1];
        }

        long maxArea = IntStream.range(0, n).parallel().mapToLong(i -> {
            long localMax = 0;
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                long x1 = px[i];
                long y1 = py[i];
                long x2 = px[j];
                long y2 = py[j];


                long width = Math.abs(x1 - x2) + 1;
                long height = Math.abs(y1 - y2) + 1;
                long area = width * height;

                if (area <= localMax) continue;

                if (isValid(x1, y1, x2, y2, px, py, n)) {
                    localMax = area;
                }
            }
            return localMax;
        }).max().orElse(0);

        System.out.println(maxArea);
    }

    private static boolean isValid(long x1, long y1, long x2, long y2, long[] px, long[] py, int n) {
        long minX = Math.min(x1, x2);
        long maxX = Math.max(x1, x2);
        long minY = Math.min(y1, y2);
        long maxY = Math.max(y1, y2);

        double mx = (minX + maxX) / 2.0;
        double my = (minY + maxY) / 2.0;

        boolean onBoundary = false;
        int intersections = 0;

        for (int k = 0; k < n; k++) {
            long ux = px[k];
            long uy = py[k];
            long vx = px[(k + 1) % n];
            long vy = py[(k + 1) % n];

            if (uy == vy) {
                if (uy > minY && uy < maxY) {
                    long exMin = Math.min(ux, vx);
                    long exMax = Math.max(ux, vx);
                    if (Math.max(minX, exMin) < Math.min(maxX, exMax)) {
                        return false;
                    }
                }

                if (Math.abs(my - uy) < 0.001) {
                    if (mx >= Math.min(ux, vx) && mx <= Math.max(ux, vx)) {
                        onBoundary = true;
                    }
                }
            }
            else {
                if (ux > minX && ux < maxX) {
                    long eyMin = Math.min(uy, vy);
                    long eyMax = Math.max(uy, vy);
                    if (Math.max(minY, eyMin) < Math.min(maxY, eyMax)) {
                        return false;
                    }
                }

                if (Math.abs(mx - ux) < 0.001) {
                    if (my >= Math.min(uy, vy) && my <= Math.max(uy, vy)) {
                        onBoundary = true;
                    }
                }

                if (ux > mx) {
                    double eyMin = Math.min(uy, vy);
                    double eyMax = Math.max(uy, vy);
                    if (my > eyMin && my < eyMax) {
                        intersections++;
                    }
                }
            }
        }

        if (onBoundary) return true;

        return (intersections % 2) != 0;
    }
}