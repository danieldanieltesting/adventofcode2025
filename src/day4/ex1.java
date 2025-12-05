package day4;

import java.nio.file.*;

public class ex1 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day4/input.txt")).trim().replace("\r", "");
        String[] lines = input.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines[i].toCharArray();
        }

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '@') {
                    int adjacent = 0;
                    for (int d = 0; d < 8; d++) {
                        int nr = r + dr[d];
                        int nc = c + dc[d];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == '@') {
                            adjacent++;
                        }
                    }
                    if (adjacent < 4) {
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
    }
}