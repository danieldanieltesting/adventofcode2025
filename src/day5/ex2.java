package day5;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ex2 {

    record Range(long start, long end) implements Comparable<Range> {
        public int compareTo(Range other) {
            return Long.compare(start, other.start);
        }

        long count() {
            return end - start + 1;
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("src/day5/input.txt"));

        List<Range> ranges = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            int dashIdx = trimmed.indexOf('-');
            if (dashIdx > 0) {
                long start = Long.parseLong(trimmed.substring(0, dashIdx));
                long end = Long.parseLong(trimmed.substring(dashIdx + 1));
                ranges.add(new Range(start, end));
            }
        }

        ranges.sort(null);

        List<Range> merged = new ArrayList<>();
        for (Range r : ranges) {
            if (merged.isEmpty() || merged.getLast().end() < r.start() - 1) {
                merged.add(r);
            } else {
                Range last = merged.removeLast();
                merged.add(new Range(last.start(), Math.max(last.end(), r.end())));
            }
        }

        long totalFresh = merged.stream()
                .mapToLong(Range::count)
                .sum();

        System.out.println(totalFresh);
    }
}