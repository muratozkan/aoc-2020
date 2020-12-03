import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) throws IOException {
        Path inFile = Path.of(args[0]);

        List<Integer> numbers = Files.lines(inFile)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(sumOfTwo(numbers));
        System.out.println(sumOfThree(numbers));
    }

    private static int sumOfThree(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int n = numbers.get(i);
            for (int j = numbers.size() - 1; i < j; j--) {
                int m = numbers.get(j);
                for (int k = i + 1; k < j; k++) {
                    int l = numbers.get(k);
                    if (n + m + l == 2020) {
                        return n * m * l;
                    }

                }
            }
        }

        return 0;
    }

    private static int sumOfTwo(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int n = numbers.get(i);
            for (int j = numbers.size() - 1; i < j; j--) {
                int m = numbers.get(j);
                if (n + m == 2020) {
                    return n * m;
                }
            }
        }
        return 0;
    }
}
