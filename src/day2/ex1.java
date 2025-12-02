package day2;

import java.nio.file.*;
import java.util.*;

public class ex1 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day2/input.txt")).trim();

        record Range(long start, long end) {}

        List<Range> ranges = Arrays.stream(input.split(","))
                .map(s -> {
                    String[] parts = s.split("-");
                    return new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                })
                .toList();

        long maxEnd = ranges.stream().mapToLong(Range::end).max().orElse(0);

        long sum = 0;
        for (long pattern = 1; ; pattern++) {
            String ps = Long.toString(pattern);
            long doubled = Long.parseLong(ps + ps);
            if (doubled > maxEnd) break;

            for (Range r : ranges) {
                if (doubled >= r.start && doubled <= r.end) {
                    sum += doubled;
                    break;
                }
            }
        }

        System.out.println(sum);
    }
}