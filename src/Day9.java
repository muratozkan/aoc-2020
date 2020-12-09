import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Day9 {
    private static final int PREAMBLE = 25;

    public static void main(String[] args) {
        List<Long> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            numbers.add(scanner.nextLong());
        }
        scanner.close();

        long invalid = firstNonMatching(numbers);
        System.out.println(invalid);

        List<Long> weakness = findConsecutive(numbers, invalid);
        weakness.sort(Comparator.naturalOrder());
        System.out.println(weakness.get(0) + weakness.get(weakness.size() - 1));
    }

    private static List<Long> findConsecutive(List<Long> numbers, long target) {
        for (int i = 0; i < numbers.size() - 1; i++) {  // at least two numbers
            long sum = numbers.get(i);
            int j = i + 1;
            while (j < numbers.size() && sum < target) {
                sum += numbers.get(j);
                j++;
            }
            if (sum == target) {
                return numbers.subList(i, j);
            }
        }

        return List.of();
    }

    private static long firstNonMatching(List<Long> numbers) {
        Set<Long> preambleSet = new TreeSet<>();
        for (int i = 0; i < PREAMBLE; i++) {
            preambleSet.add(numbers.get(i));
        }
        for (int i = PREAMBLE; i < numbers.size(); i++) {
            long n = numbers.get(i);

            boolean found = false;
            for (int j = 0; j < PREAMBLE; j++) {
                long r = n - numbers.get(i - j);
                if (preambleSet.contains(r)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return n;
            }

            preambleSet.remove(numbers.get(i - PREAMBLE));
            preambleSet.add(n);
        }
        return -1L;
    }

}
