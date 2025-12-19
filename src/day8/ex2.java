package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ex2 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("src/day8/input.txt")).trim();
        System.out.println(solve(input));
    }

    private static long solve(String input) {
        List<Point> points = input.lines()
                .filter(line -> !line.isBlank())
                .map(line -> parsePoint(line))
                .toList();

        int n = points.size();
        List<Edge> edges = new ArrayList<>(n * (n - 1) / 2);

        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            for (int j = i + 1; j < n; j++) {
                Point p2 = points.get(j);
                long distSq = (p1.x - p2.x) * (p1.x - p2.x) +
                        (p1.y - p2.y) * (p1.y - p2.y) +
                        (p1.z - p2.z) * (p1.z - p2.z);
                edges.add(new Edge(i, j, distSq));
            }
        }

        edges.sort(Comparator.comparingLong(Edge::weight));

        DSU dsu = new DSU(n);
        int components = n;

        for (Edge edge : edges) {
            if (dsu.union(edge.u, edge.v)) {
                components--;
                if (components == 1) {
                    Point p1 = points.get(edge.u);
                    Point p2 = points.get(edge.v);
                    return p1.x * p2.x;
                }
            }
        }

        return -1;
    }

    private static Point parsePoint(String line) {
        String[] parts = line.split(",");
        return new Point(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1]),
                Long.parseLong(parts[2])
        );
    }

    record Point(long x, long y, long z) {}
    record Edge(int u, int v, long weight) {}

    static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int size) {
            this.parent = new int[size];
            this.rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI == rootJ) {
                return false;
            }

            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
            } else {
                parent[rootJ] = rootI;
                rank[rootI]++;
            }
            return true;
        }
    }
}