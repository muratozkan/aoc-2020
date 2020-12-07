import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day7 {

    private static class Rule {
        int bag;
        final Map<Integer, Integer> contains = new HashMap<>();
    }

    public static void main(String[] args) {
        Map<String, Integer> colorIds = new HashMap<>();
        List<Rule> ruleList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] pieces = line.split("contain ");

            String[] left = pieces[0].split(" ");

            Rule rule = new Rule();
            rule.bag = parseBagId(colorIds, left[0] + " " + left[1]);

            if (!pieces[1].startsWith("no")) {
                for (String rightBag : pieces[1].split(", ")) {
                    String[] right = rightBag.split(" ");
                    int amount = Integer.parseInt(right[0]);
                    int rightId = parseBagId(colorIds, right[1] + " " + right[2]);

                    rule.contains.put(rightId, amount);
                }
            }
            ruleList.add(rule);
        }
        scanner.close();
        ruleList.sort(Comparator.comparing(r -> r.bag));

        ConnectedTo ct = new ConnectedTo(ruleList, colorIds.get("shiny gold"));
        System.out.println(ct.connected());

        CostOf costOf = new CostOf(ruleList, colorIds.get("shiny gold"));
        System.out.println(costOf.cost());
    }

    private static int parseBagId(Map<String, Integer> colorIds, String bag) {
        if (colorIds.containsKey(bag)) {
            return colorIds.get(bag);
        }
        int id = colorIds.size();
        colorIds.put(bag, id);
        return id;
    }

    private static class ConnectedTo {

        private boolean[] marked;
        private final boolean[] connected;

        public ConnectedTo(List<Rule> graph, int target) {
            connected = new boolean[graph.size()];
            for (Rule rule : graph) {
                marked = new boolean[graph.size()];
                if (rule.bag != target && !marked[rule.bag]) {
                    boolean reachable = dfs(graph, rule.bag, target);
                    connected[rule.bag] = reachable;
                }
            }
        }

        private boolean dfs(List<Rule> graph, int v, int target) {
            marked[v] = true;
            if (v == target || connected[v]) {
                return true;
            }
            for (Integer i : graph.get(v).contains.keySet()) {
                if (!marked[i]) {
                    boolean reachable = dfs(graph, i, target);
                    if (reachable) {
                        return true;
                    }
                }
            }
            return false;
        }

        private int connected() {
            int count = 0;
            for (boolean c : connected) {
                count += c ? 1 : 0;
            }
            return count;
        }
    }

    private static class CostOf {
        private final boolean[] marked;
        private final int[] costs;
        private final int cost;

        public CostOf(List<Rule> graph, int source) {
            marked = new boolean[graph.size()];
            costs = new int[graph.size()];
            cost = dfs(graph, source);
        }

        private int dfs(List<Rule> graph, int v) {
            if (marked[v]) {
                return costs[v];
            }
            int cost = 1;
            for (Integer i : graph.get(v).contains.keySet()) {
                int childCost = dfs(graph, i);
                cost += childCost * graph.get(v).contains.get(i);
            }

            // assuming non-cyclic list
            marked[v] = true;
            costs[v] = cost;

            return cost;
        }

        public int cost() {
            return cost - 1;
        }
    }
}
