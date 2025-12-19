package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ex2 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day11/input.txt")).trim();

        var graph = parseGraph(input);
        var solver = new PathSolver(graph);

        long svrToDac = solver.countPaths("svr", "dac");
        long dacToFft = solver.countPaths("dac", "fft");
        long fftToOut = solver.countPaths("fft", "out");

        long svrToFft = solver.countPaths("svr", "fft");
        long fftToDac = solver.countPaths("fft", "dac");
        long dacToOut = solver.countPaths("dac", "out");

        long pathOrder1 = svrToDac * dacToFft * fftToOut;
        long pathOrder2 = svrToFft * fftToDac * dacToOut;

        System.out.println(pathOrder1 + pathOrder2);
    }

    private static Map<String, List<String>> parseGraph(String input) {
        return input.lines()
                .filter(line -> !line.isBlank())
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(
                        parts -> parts[0],
                        parts -> Arrays.asList(parts[1].split(" "))
                ));
    }

    static class PathSolver {
        private final Map<String, List<String>> graph;

        public PathSolver(Map<String, List<String>> graph) {
            this.graph = graph;
        }

        public long countPaths(String start, String end) {
            return countPathsRecursive(start, end, new HashMap<>());
        }

        private long countPathsRecursive(String current, String target, Map<String, Long> memo) {
            if (current.equals(target)) {
                return 1L;
            }
            if (memo.containsKey(current)) {
                return memo.get(current);
            }

            long total = 0;
            List<String> neighbors = graph.get(current);

            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    total += countPathsRecursive(neighbor, target, memo);
                }
            }

            memo.put(current, total);
            return total;
        }
    }
}