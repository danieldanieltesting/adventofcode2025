package day10;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class ex2 {
    private static final double EPS = 1e-9;

    record Machine(int numButtons, int numCounters, double[][] matrix) {}

    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("src/day10/input.txt")).trim();
        List<Machine> machines = input.lines().map(ex2::parse).toList();

        long result = machines.stream()
                .mapToLong(ex2::solveMachine)
                .sum();

        System.out.println(result);
    }

    private static Machine parse(String line) {
        List<int[]> buttons = new ArrayList<>();
        Matcher bm = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        while (bm.find()) {
            buttons.add(Arrays.stream(bm.group(1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray());
        }
        Matcher tm = Pattern.compile("\\{([^}]+)\\}").matcher(line);
        int[] targets = tm.find() ? Arrays.stream(tm.group(1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray() : new int[0];

        int B = buttons.size();
        int C = targets.length;
        double[][] mat = new double[C][B + 1];
        for (int j = 0; j < B; j++) {
            for (int counterIdx : buttons.get(j)) {
                mat[counterIdx][j] = 1.0;
            }
        }
        for (int i = 0; i < C; i++) {
            mat[i][B] = (double) targets[i];
        }
        return new Machine(B, C, mat);
    }

    private static long solveMachine(Machine m) {
        double[][] mat = m.matrix;
        int rows = mat.length;
        int cols = mat[0].length;
        int B = m.numButtons;

        int pivotRow = 0;
        int[] pivotCol = new int[rows];
        Arrays.fill(pivotCol, -1);

        for (int j = 0; j < B && pivotRow < rows; j++) {
            int sel = pivotRow;
            for (int i = pivotRow + 1; i < rows; i++) {
                if (Math.abs(mat[i][j]) > Math.abs(mat[sel][j])) sel = i;
            }
            if (Math.abs(mat[sel][j]) < EPS) continue;

            double[] temp = mat[sel]; mat[sel] = mat[pivotRow]; mat[pivotRow] = temp;
            pivotCol[pivotRow] = j;

            double val = mat[pivotRow][j];
            for (int k = j; k < cols; k++) mat[pivotRow][k] /= val;
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow) {
                    double factor = mat[i][j];
                    for (int k = j; k < cols; k++) mat[i][k] -= factor * mat[pivotRow][k];
                }
            }
            pivotRow++;
        }

        for (int i = 0; i < rows; i++) {
            boolean allZero = true;
            for (int j = 0; j < B; j++) if (Math.abs(mat[i][j]) > EPS) allZero = false;
            if (allZero && Math.abs(mat[i][B]) > EPS) return 0;
        }

        boolean[] isPivot = new boolean[B];
        int actualPivots = 0;
        for (int i = 0; i < rows; i++) {
            if (pivotCol[i] != -1) {
                isPivot[pivotCol[i]] = true;
                actualPivots++;
            }
        }

        List<Integer> freeVars = new ArrayList<>();
        for (int j = 0; j < B; j++) if (!isPivot[j]) freeVars.add(j);

        int[] pivotToRow = new int[B];
        Arrays.fill(pivotToRow, -1);
        for (int i = 0; i < rows; i++) if (pivotCol[i] != -1) pivotToRow[pivotCol[i]] = i;

        return new Solver(m, mat, freeVars, isPivot, pivotToRow).findMin();
    }

    private static class Solver {
        final Machine m;
        final double[][] rref;
        final List<Integer> freeVars;
        final boolean[] isPivot;
        final int[] pivotToRow;
        final int[] freeValues;
        long minSum = Long.MAX_VALUE;
        final int maxT;

        Solver(Machine m, double[][] rref, List<Integer> freeVars, boolean[] isPivot, int[] pivotToRow) {
            this.m = m;
            this.rref = rref;
            this.freeVars = freeVars;
            this.isPivot = isPivot;
            this.pivotToRow = pivotToRow;
            this.freeValues = new int[freeVars.size()];
            int mt = 0;
            for (int i = 0; i < rref.length; i++) mt = Math.max(mt, (int) Math.round(rref[i][m.numButtons]));
            this.maxT = mt + 10;
        }

        long findMin() {
            search(0, 0);
            return minSum == Long.MAX_VALUE ? 0 : minSum;
        }

        void search(int idx, long currentSum) {
            if (currentSum >= minSum) return;

            if (idx == freeVars.size()) {
                long total = currentSum;
                for (int j = 0; j < m.numButtons; j++) {
                    if (isPivot[j]) {
                        int r = pivotToRow[j];
                        double val = rref[r][m.numButtons];
                        for (int k = 0; k < freeVars.size(); k++) {
                            val -= rref[r][freeVars.get(k)] * freeValues[k];
                        }
                        long rounded = Math.round(val);
                        if (Math.abs(val - rounded) > EPS || rounded < 0) return;
                        total += rounded;
                    }
                }
                minSum = Math.min(minSum, total);
                return;
            }

            int fVarIdx = freeVars.get(idx);
            for (int v = 0; v <= maxT; v++) {
                freeValues[idx] = v;
                search(idx + 1, currentSum + v);
            }
        }
    }
}