package day2;

import java.nio.file.*;
import java.util.*;

public class ex2 {
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
        int maxDigits = Long.toString(maxEnd).length();

        Set<Long> invalidIds = new HashSet<>();

        for (int patternLen = 1; patternLen <= maxDigits / 2; patternLen++) {
            long patternStart = (patternLen == 1) ? 1 : (long)Math.pow(10, patternLen - 1);
            long patternEnd = (long)Math.pow(10, patternLen) - 1;

            for (long pattern = patternStart; pattern <= patternEnd; pattern++) {
                String ps = Long.toString(pattern);
                for (int rep = 2; rep * patternLen <= maxDigits; rep++) {
                    long num = Long.parseLong(ps.repeat(rep));
                    if (num <= maxEnd) {
                        invalidIds.add(num);
                    }
                }
            }
        }

        long sum = 0;
        for (long id : invalidIds) {
            for (Range r : ranges) {
                if (id >= r.start && id <= r.end) {
                    sum += id;
                    break;
                }
            }
        }

        System.out.println(sum);
    }
}