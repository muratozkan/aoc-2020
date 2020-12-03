import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 {

    private static class PassPolicy {
        int min;
        int max;
        char ch;
        String password;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<PassPolicy> list = new ArrayList<>();
        while (scanner.hasNext()) {
            PassPolicy policy = new PassPolicy();

            String[] minMax = scanner.next().split("-");

            policy.min = Integer.parseInt(minMax[0]);
            policy.max = Integer.parseInt(minMax[1]);

            policy.ch = scanner.next().charAt(0);

            policy.password = scanner.next();
            list.add(policy);
        }
        scanner.close();

        System.out.println(numValidPasswords(list));
        System.out.println(numValidWithNewPolicy(list));
    }

    private static int numValidWithNewPolicy(List<PassPolicy> policies) {
        return policies.stream()
                .mapToInt(p -> {
                    boolean posA = p.password.charAt(p.min - 1) == p.ch;
                    boolean posB = p.password.charAt(p.max - 1) == p.ch;
                    return (posA ^ posB) ? 1 : 0;
                })
                .sum();
    }

    private static int numValidPasswords(List<PassPolicy> policies) {
        return policies.stream()
                .mapToInt(p -> {
                    int replaced = p.password.length() - p.password.replace("" + p.ch, "").length();
                    return (replaced >= p.min && replaced <= p.max) ? 1 : 0;
                })
                .sum();
    }
}
