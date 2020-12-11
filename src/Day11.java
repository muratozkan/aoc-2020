import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day11 {
    private static final int OCCUPIED = 2;
    private static final int FLOOR = 0;
    private static final int EMPTY = 1;

    public static void main(String[] args) {
        List<List<Integer>> grid = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            List<Integer> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c == '.' ? 0 : (c == 'L') ? 1 : 2);
            }
            grid.add(row);
        }
        scanner.close();

        System.out.println(countOccupied(runUntilStabilization(grid, false)));
        System.out.println(countOccupied(runUntilStabilization(grid, true)));
    }


    private static int countOccupied(List<List<Integer>> grid) {
        return grid.stream()
                .flatMapToInt(r -> r.stream().mapToInt(i -> i == OCCUPIED ? 1 : 0))
                .sum();
    }

    private static void printGrid(List<List<Integer>> grid) {
        System.err.println("");
        for (List<Integer> row : grid) {
            for (int i : row) {
                System.err.print(i == EMPTY ? 'L' : i == OCCUPIED ? '#' : '.');
            }
            System.err.println("");
        }
    }

    private static List<List<Integer>> runUntilStabilization(List<List<Integer>> in, boolean newAlg) {
        List<List<Integer>> prevGen = in;
        int changedCount = 1;
        while (changedCount > 0) {
            System.err.print(".");

            changedCount = 0;
            List<List<Integer>> nextGen = new ArrayList<>();
            for (int y = 0; y < in.size(); y++) {
                List<Integer> row = new ArrayList<>(in.get(0).size());
                for (int x = 0; x < in.get(0).size(); x++) {
                    int state = prevGen.get(y).get(x);
                    int nextState = newAlg ? nextStateNew(prevGen, x, y) : nextState(prevGen, x, y);
                    row.add(nextState);
                    if (nextState != state) {
                        changedCount++;
                    }
                }
                nextGen.add(row);
            }
            prevGen = nextGen;
        }
        System.err.println();
        return prevGen;
    }

    private static int nextStateNew(List<List<Integer>> grid, int x, int y) {
        int state = grid.get(y).get(x);
        if (state == FLOOR) {
            return FLOOR;   // floor doesn't change
        }
        int nOccupied = 0;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (dx == 0 && dy == 0) continue;
                int d = 0;
                boolean inBounds;
                boolean foundSeat = false;
                do {
                    int cx = (x + dx) + (d * dx);
                    int cy = (y + dy) + (d * dy);

                    inBounds = cx >= 0 && cx < grid.get(0).size() &&
                            cy >= 0 && cy < grid.size();
                    if (inBounds) {
                        d++;
                        foundSeat = grid.get(cy).get(cx) != FLOOR;
                        nOccupied += (grid.get(cy).get(cx) == OCCUPIED) ? 1 : 0;
                    }
                } while (inBounds && !foundSeat);
            }
        }
        if (state == EMPTY) {
            return (nOccupied == 0) ? OCCUPIED : EMPTY;
        }
        return (nOccupied >= 5) ? EMPTY : OCCUPIED;
    }

    private static int nextState(List<List<Integer>> grid, int x, int y) {
        int state = grid.get(y).get(x);
        if (state == FLOOR) {
            return FLOOR;   // floor doesn't change
        }
        int nOccupied = 0;
        for (int cy = y - 1; cy < y + 2; cy++) {
            if (cy < 0) continue;
            if (cy >= grid.size()) break;
            for (int cx = x - 1; cx < x + 2; cx++) {
                if (cx < 0 || (cx == x && cy == y)) continue;
                if (cx >= grid.get(0).size()) break;
                nOccupied += (grid.get(cy).get(cx) == OCCUPIED) ? 1 : 0;
                if (nOccupied >= 4) break;
            }
        }
        if (state == EMPTY) {
            return (nOccupied == 0) ? OCCUPIED : EMPTY;
        }
        return (nOccupied >= 4) ? EMPTY : OCCUPIED;
    }
}
