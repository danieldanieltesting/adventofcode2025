package day8;

import java.nio.file.*;
import java.util.*;

public class ex1 {

    record Edge(long dist, int i, int j) {}

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day8/input.txt")).trim();
        String[] lines = input.split("\n");
        int n = lines.length;
        int[][] pts = new int[n][3];

        for (int i = 0; i < n; i++) {
            String[] p = lines[i].split(",");
            pts[i][0] = Integer.parseInt(p[0].trim());
            pts[i][1] = Integer.parseInt(p[1].trim());
            pts[i][2] = Integer.parseInt(p[2].trim());
        }

        List<Edge> edges = new ArrayList<>(n * (n - 1) / 2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = pts[i][0] - pts[j][0];
                long dy = pts[i][1] - pts[j][1];
                long dz = pts[i][2] - pts[j][2];
                edges.add(new Edge(dx * dx + dy * dy + dz * dz, i, j));
            }
        }

        edges.sort(Comparator.comparingLong(Edge::dist));

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int limit = Math.min(1000, edges.size());
        for (int k = 0; k < limit; k++) {
            Edge e = edges.get(k);
            int pi = find(parent, e.i);
            int pj = find(parent, e.j);
            if (pi != pj) parent[pi] = pj;
        }

        Map<Integer, Integer> sizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            sizes.merge(find(parent, i), 1, Integer::sum);
        }

        List<Integer> sorted = new ArrayList<>(sizes.values());
        sorted.sort(Collections.reverseOrder());

        System.out.println((long) sorted.get(0) * sorted.get(1) * sorted.get(2));
    }

    static int find(int[] p, int x) {
        return p[x] == x ? x : (p[x] = find(p, p[x]));
    }
}