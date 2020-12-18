import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day18 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }

        Map<String, Integer> precEq = Map.of(
                "(", 0, ")", 0,
                "+", 1, "*", 1
        );

        long sum = 0L;
        for (String line : lines) {
            long result = evaluate(line, precEq);
            System.err.println(result);
            sum += result;
        }
        System.out.println(sum);

        Map<String, Integer> precPlus = Map.of(
                "(", 0, ")", 0,
                "*", 1, "+", 2
        );
        sum = 0L;
        for (String line : lines) {
            long result = evaluate(line, precPlus);
            System.err.println(result);
            sum += result;
        }
        System.out.println(sum);
    }

    private static long evaluate(String line, Map<String, Integer> prec) {
        String[] tokens = line.replace("(", "( ")
                .replace(")", " )")
                .split(" ");

        Deque<Long> operands = new ArrayDeque<>();
        Deque<String> ops = new ArrayDeque<>();
        for (String t : tokens) {
            // t is a number
            if (!prec.containsKey(t)) {
                operands.addFirst(Long.valueOf(t));
                continue;
            }

            while (true) {
                if (ops.isEmpty() || "(".equals(t) || (prec.get(t) > prec.get(ops.peekFirst()))) {
                    ops.addFirst(t);
                    break;
                }

                String prevOp = ops.removeFirst();
                if ("(".equals(prevOp)) {
                    break;
                } else {
                    operands.addFirst(doOp(prevOp, operands.removeFirst(), operands.removeFirst()));
                }
            }
        }

        while (operands.size() > 1) {
            operands.addFirst(doOp(ops.removeFirst(), operands.removeFirst(), operands.removeFirst()));
        }

        return operands.removeFirst();
    }


    private static long doOp(String op, long a, long b) {
        if ("+".equals(op)) {
            return a + b;
        }
        return a * b;
    }
}
