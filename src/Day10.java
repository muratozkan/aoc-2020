import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day10 {
    public static void main(String[] args) {
        List<Integer> adapters = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            adapters.add(scanner.nextInt());
        }
        scanner.close();

        adapters.sort(Comparator.naturalOrder());

        System.out.println(adapterChain(adapters));
        System.out.println(numCombinations(adapters));
    }

    private static long numCombinations(List<Integer> adapters) {
        adapters.add(0, 0);
        long[] counts = new long[adapters.size()];
        for (int i = 0; i < adapters.size(); i++) {
            int ni = adapters.get(i);
            long cc = 0;
            for (int j = i - 1; j >= 0; j--) {
                int nj = adapters.get(j);
                if (ni - nj > 3) {
                    break;
                }
                cc += counts[j];
            }
            counts[i] = (cc == 0) ? 1 : cc;
        }
        return counts[adapters.size() - 1];
    }

    private static int adapterChain(List<Integer> adapters) {
        int n1 = 0, n3 = 0;
        int prev = 0;
        for (Integer adapter : adapters) {
            int diff = adapter - prev;
            n1 += (diff == 1) ? 1 : 0;
            n3 += (diff == 3) ? 1 : 0;
            prev = adapter;
        }

        return n1 * (n3 + 1);
    }
}
