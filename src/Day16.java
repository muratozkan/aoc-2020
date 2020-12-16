import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day16 {

    private static class Rule {
        String name;
        int[] first = new int[2];
        int[] second = new int[2];

        boolean isValid(int val) {
            return (val >= first[0] && val <= first[1]) || (val >= second[0] && val <= second[1]);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rule rule = (Rule) o;
            return Objects.equals(name, rule.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public static void main(String[] args) {
        List<Rule> rules = new ArrayList<>();
        List<Integer> myTicket = new ArrayList<>();
        List<List<Integer>> nearbyTickets = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        int parseStage = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("".equals(line)) {
                continue;
            }
            if ("your ticket:".equals(line)) {
                parseStage = 1;
            } else if ("nearby tickets:".equals(line)) {
                parseStage = 2;
            } else if (parseStage == 0) {
                String[] parts = line.split(": ");
                Rule rule = new Rule();
                rule.name = parts[0];

                String[] nums = parts[1].split(" or ");
                String[] numParts = nums[0].split("-");

                rule.first[0] = Integer.parseInt(numParts[0]);
                rule.first[1] = Integer.parseInt(numParts[1]);

                numParts = nums[1].split("-");

                rule.second[0] = Integer.parseInt(numParts[0]);
                rule.second[1] = Integer.parseInt(numParts[1]);

                rules.add(rule);
            } else if (parseStage == 1) {
                for (String str : line.split(",")) {
                    myTicket.add(Integer.parseInt(str));
                }
            } else {
                List<Integer> ticket = new ArrayList<>();
                for (String str : line.split(",")) {
                    ticket.add(Integer.parseInt(str));
                }
                nearbyTickets.add(ticket);
            }
        }
        scanner.close();

        System.out.println(scanningErrorRate(rules, nearbyTickets));
        List<List<Integer>> validTickets = validTickets(rules, nearbyTickets);
        List<Rule> rulesOfFields = rulesOfFields(rules, validTickets);
        long mult = 1L;
        for (int i = 0; i < rulesOfFields.size(); i++) {
            Rule rule = rulesOfFields.get(i);
            if (rule.name.startsWith("departure")) {
                mult *= myTicket.get(i);
            }
        }
        System.out.println(mult);
    }

    private static List<Rule> rulesOfFields(List<Rule> rules, List<List<Integer>> validTickets) {
        List<Set<Rule>> availableRules = new ArrayList<>();
        for (int i = 0; i < validTickets.get(0).size(); i ++) {
            Set<Rule> ruleSet = new HashSet<>(rules);
            availableRules.add(ruleSet);
        }

        for (List<Integer> ticket : validTickets) {
            for (int i = 0; i < ticket.size(); i++) {
                List<Rule> nonMatching = new ArrayList<>();
                Set<Rule> possibleSet = availableRules.get(i);
                for (Rule rule : possibleSet) {
                    if (!rule.isValid(ticket.get(i))) {
                        nonMatching.add(rule);
                    }
                }
                possibleSet.removeAll(nonMatching);
            }
        }

        Rule aFoundRule = availableRules.stream()
                .filter(set -> set.size() == 1)
                .findFirst()
                .orElseThrow()
                .stream().findFirst()
                .orElseThrow();
        Queue<Rule> removalQueue = new ArrayDeque<>();
        removalQueue.add(aFoundRule);
        while (!removalQueue.isEmpty()) {
            Rule toRemove = removalQueue.remove();
            for (Set<Rule> ruleSet : availableRules) {
                if (ruleSet.size() > 1) {
                    ruleSet.remove(toRemove);
                    if (ruleSet.size() == 1) {
                        removalQueue.addAll(ruleSet);
                    }
                }
            }
        }

        return availableRules.stream()
                .map(ruleSet -> ruleSet.stream().findFirst().orElseThrow())
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> validTickets(List<Rule> rules, List<List<Integer>> nearbyTickets) {
        List<List<Integer>> validTickets = new ArrayList<>();
        for (List<Integer> ticket : nearbyTickets) {
            boolean validTicket = true;
            for (int value : ticket) {
                boolean isValid = false;
                for (Rule rule : rules) {
                    if (rule.isValid(value)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    validTicket = false;
                    break;
                }
            }
            if (validTicket) {
                validTickets.add(ticket);
            }
        }
        return validTickets;
    }

    private static int scanningErrorRate(List<Rule> rules, List<List<Integer>> nearbyTickets) {
        List<Integer> errors = new ArrayList<>();
        for (List<Integer> ticket : nearbyTickets) {
            for (int value : ticket) {
                boolean isValid = false;
                for (Rule rule : rules) {
                    if (rule.isValid(value)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    errors.add(value);
                }
            }
        }

        return errors.stream().mapToInt(i -> i).sum();
    }
}
