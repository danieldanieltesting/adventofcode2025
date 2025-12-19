package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ex1 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day11/input.txt")).trim();

        var graph = parseInput(input);
        var pathCounter = new PathCounter(graph);
        long paths = pathCounter.countPaths("you", "out");

        System.out.println(paths);
    }

    private static Map<String, List<String>> parseInput(String input) {
        return input.lines()
                .filter(line -> !line.isBlank())
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(
                        parts -> parts[0],
                        parts -> Arrays.asList(parts[1].split(" "))
                ));
    }

    static class PathCounter {
        private final Map<String, List<String>> graph;
        private final Map<String, Long> memo;

        public PathCounter(Map<String, List<String>> graph) {
            this.graph = graph;
            this.memo = new HashMap<>();
        }

        public long countPaths(String current, String target) {
            if (current.equals(target)) {
                return 1L;
            }
            if (memo.containsKey(current)) {
                return memo.get(current);
            }

            long totalPaths = 0;
            List<String> neighbors = graph.get(current);

            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    totalPaths += countPaths(neighbor, target);
                }
            }

            memo.put(current, totalPaths);
            return totalPaths;
        }
    }
}