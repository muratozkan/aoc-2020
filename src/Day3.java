import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<List<Boolean>> trees = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            List<Boolean> treeLine = new ArrayList<>();
            for (char c : line.toCharArray()) {
                treeLine.add(c == '#');
            }
            trees.add(treeLine);
        }
        scanner.close();

        System.out.println(treesDownRight(trees, 1, 3));
        System.out.println(slopes(trees));

    }

    private static long slopes(List<List<Boolean>> trees) {
        int one = treesDownRight(trees, 1, 1);
        int three = treesDownRight(trees, 1, 3);
        int five = treesDownRight(trees, 1, 5);
        int seven = treesDownRight(trees, 1, 7);
        int downTwo = treesDownRight(trees, 2, 1);
        return (long) one * three * five * seven * downTwo;
    }

    private static int treesDownRight(List<List<Boolean>> trees, int dy, int dx) {
        int x = 0, y = 0;
        int colSize = trees.get(0).size();
        int treeCount = 0;
        while (y < trees.size()) {
            treeCount += trees.get(y).get(x) ? 1 : 0;
            y += dy;
            x = (x + dx) % colSize;
        }

        return treeCount;
    }
}
