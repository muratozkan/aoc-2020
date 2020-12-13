import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) {
        int timestamp;
        List<Integer> numbers = new ArrayList<>();


        Scanner scanner = new Scanner(System.in);
        timestamp = scanner.nextInt();
        scanner.nextLine();
        String line = scanner.nextLine();
        scanner.close();

        for (String n : line.split(",")) {
            if ("x".equals(n)) {
                numbers.add(-1);
            } else {
                numbers.add(Integer.parseInt(n));
            }
        }
        System.out.println(firstAfterTime(timestamp, numbers));
        System.out.println(earliestSequence(numbers));
    }

    private static long earliestSequence(List<Integer> numbers) {
        List<Integer> nums = new ArrayList<>();
        List<Integer> mods = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            int n = numbers.get(i);
            if (n == -1) {
                continue;
            }
            nums.add(n);
            int modVal = (n - i) % n;
            mods.add(modVal < 0 ? modVal + n : modVal);
        }
        long x = 0;
        long mod = nums.get(0);
        for (int ni = 1; ni < nums.size(); ni++) {
            int n = nums.get(ni);
            while (x % n != mods.get(ni)) {
                x+= mod;
            }
            mod = mod * n;
        }

        return x;
    }

    private static int firstAfterTime(int timestamp, List<Integer> numbers) {
        int min = Integer.MAX_VALUE;
        int minIdx = -1;
        for (int i = 0; i < numbers.size(); i++) {
            int n = numbers.get(i);
            if (n == -1) continue;
            int next = ((timestamp / n) + 1) * n - timestamp;
            if (next < min) {
                min = next;
                minIdx = i;
            }
        }

        return min * numbers.get(minIdx);
    }
}
