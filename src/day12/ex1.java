package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ex1 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day12/input.txt")).trim();
        System.out.println(solve(input));
    }

    private static long solve(String input) {
        String[] chunks = input.replace("\r", "").split("\n\n");
        List<ShapeStats> shapes = new ArrayList<>();
        List<String> regionLines = new ArrayList<>();

        for (String chunk : chunks) {
            if (chunk.matches("^\\d+:.*[\\s\\S]*")) {
                shapes.add(parseShape(chunk));
            } else {
                String[] lines = chunk.split("\n");
                for (String line : lines) {
                    if (line.matches(".*\\d+x\\d+:.*")) {
                        regionLines.add(line);
                    }
                }
            }
        }

        shapes.sort(Comparator.comparingInt(ShapeStats::id));

        long validRegions = 0;
        for (String region : regionLines) {
            if (canFit(region, shapes)) {
                validRegions++;
            }
        }
        return validRegions;
    }

    private static boolean canFit(String regionLine, List<ShapeStats> shapes) {
        int colon = regionLine.indexOf(':');
        String[] dims = regionLine.substring(0, colon).split("x");
        long gridW = Long.parseLong(dims[0]);
        long gridH = Long.parseLong(dims[1]);
        long gridArea = gridW * gridH;

        long[] counts = Arrays.stream(regionLine.substring(colon + 1).trim().split("\\s+"))
                .mapToLong(Long::parseLong)
                .toArray();

        long totalArea = 0;
        long minWhite = 0;
        long maxWhite = 0;

        for (int i = 0; i < counts.length; i++) {
            long count = counts[i];
            if (count > 0) {
                ShapeStats s = shapes.get(i);
                totalArea += count * s.size();
                minWhite += count * s.minW();
                maxWhite += count * s.maxW();
            }
        }

        if (totalArea > gridArea) {
            return false;
        }

        long gridWhites = (gridArea + 1) / 2;
        long gridBlacks = gridArea / 2;

        long requiredMinWhite = totalArea - gridBlacks;

        long overlapStart = Math.max(minWhite, requiredMinWhite);
        long overlapEnd = Math.min(maxWhite, gridWhites);

        return overlapStart <= overlapEnd;
    }

    private record Point(int r, int c) {}
    private record ShapeStats(int id, int size, int minW, int maxW) {}

    private static ShapeStats parseShape(String chunk) {
        String[] lines = chunk.split("\n");
        int id = Integer.parseInt(lines[0].replace(":", "").trim());
        List<Point> points = new ArrayList<>();

        for (int r = 1; r < lines.length; r++) {
            for (int c = 0; c < lines[r].length(); c++) {
                if (lines[r].charAt(c) == '#') {
                    points.add(new Point(r - 1, c));
                }
            }
        }

        int size = points.size();
        int globalMinW = Integer.MAX_VALUE;
        int globalMaxW = Integer.MIN_VALUE;

        List<Point> current = points;
        for (int i = 0; i < 8; i++) {
            if (i == 4) current = flip(points);
            else if (i > 0) current = rotate(current);

            current = normalize(current);

            int w = 0;
            for (Point p : current) {
                if ((p.r() + p.c()) % 2 == 0) w++;
            }
            int b = size - w;

            globalMinW = Math.min(globalMinW, Math.min(w, b));
            globalMaxW = Math.max(globalMaxW, Math.max(w, b));
        }

        return new ShapeStats(id, size, globalMinW, globalMaxW);
    }

    private static List<Point> normalize(List<Point> shape) {
        int minR = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        for (Point p : shape) {
            minR = Math.min(minR, p.r());
            minC = Math.min(minC, p.c());
        }
        List<Point> norm = new ArrayList<>();
        for (Point p : shape) {
            norm.add(new Point(p.r() - minR, p.c() - minC));
        }
        return norm;
    }

    private static List<Point> rotate(List<Point> shape) {
        List<Point> rot = new ArrayList<>();
        for (Point p : shape) {
            rot.add(new Point(p.c(), -p.r()));
        }
        return rot;
    }

    private static List<Point> flip(List<Point> shape) {
        List<Point> flipped = new ArrayList<>();
        for (Point p : shape) {
            flipped.add(new Point(p.r(), -p.c()));
        }
        return flipped;
    }
}