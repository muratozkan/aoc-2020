import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Day15 {

    private static final int[] TEST = {1, 3, 2};
    private static final int[] INPUT = {0, 1, 4, 13, 15, 12, 16};

    private static final int[] NUMS = INPUT;

    public static void main(String[] args) {
        GameIterator iterator = new GameIterator(NUMS, 2020);
        int last = 0;
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        System.out.println(last);
        iterator = new GameIterator(NUMS, 30000000);
        last = 0;
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        System.out.println(last);

    }

    private static class GameIterator implements Iterator<Integer> {

        private final Map<Integer, List<Integer>> numAtTurn = new HashMap<>();
        private int current = 0;
        private int idx = 0;
        private final int limit;

        public GameIterator(int[] initial, int limit) {
            this.limit = limit;
            for (int i = 0; i < initial.length - 1; i++) {
                int n = initial[i];
                if (!numAtTurn.containsKey(n)) {
                    numAtTurn.put(n, new ArrayList<>(2));
                }
                numAtTurn.get(n).add(i + 1);
            }
            current = initial[initial.length - 1];
            idx = initial.length;
        }

        @Override
        public boolean hasNext() {
            return idx < limit;
        }

        @Override
        public Integer next() {
            int next = 0;
            if (!numAtTurn.containsKey(current)) {
                numAtTurn.put(current, new ArrayList<>(2));
                numAtTurn.get(current).add(idx);
            } else {
                updateRecentlySeen(numAtTurn.get(current), idx);
                List<Integer> prevIdxs = numAtTurn.get(current);
                next = prevIdxs.get(1) - prevIdxs.get(0);
            }
            idx++;
            current = next;
            return next;
        }

        private static void updateRecentlySeen(List<Integer> seen, int idx) {
            if (seen.size() == 2) {
                seen.set(0, seen.get(1));
                seen.remove(1);
            }
            seen.add(idx);
        }
    }
}
