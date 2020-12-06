import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<List<String>> groups = new ArrayList<>();
        List<String> group = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                groups.add(group);
                group = new ArrayList<>();
            } else {
                group.add(line);
            }
        }
        if (!group.isEmpty()) {
            groups.add(group);
        }
        scanner.close();

        System.out.println(sumCountsForAny(groups));
        System.out.println(sumCountsForAll(groups));
    }

    private static int sumCountsForAll(List<List<String>> groups) {
        int sum = 0;
        for (List<String> group : groups) {
            Set<Character> initialSet = new HashSet<>();
            addChars(initialSet, group.get(0));
            for (int i = 1; i < group.size(); i++) {
                Set<Character> newSet = new HashSet<>();
                addChars(newSet, group.get(i));
                initialSet.retainAll(newSet);
                if (initialSet.isEmpty()) {
                    break;
                }
            }
            sum += initialSet.size();
        }
        return sum;
    }

    private static int sumCountsForAny(List<List<String>> groups) {
        int sum = 0;
        for (List<String> group : groups) {
            Set<Character> set = new HashSet<>();
            for (String s : group) {
                addChars(set, s);
            }
            sum += set.size();
        }
        return sum;
    }

    private static void addChars(Set<Character> set, String str) {
        for (char ch : str.toCharArray()) {
            set.add(ch);
        }
    }
}
