import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day4 {

    private static final String[] fields = {"byr", "ecl", "eyr", "iyr", "hgt", "hcl", "pid"};
    private static final Set<String> eye = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Map<String, String>> passList = new ArrayList<>();
        Map<String, String> passport = new HashMap<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                passList.add(passport);
                passport = new HashMap<>();
            } else {
                for (String p : line.split(" ")) {
                    String[] kv = p.split(":");
                    passport.put(kv[0], kv[1]);
                }
            }
        }
        if (!passport.isEmpty()) {
            passList.add(passport);
        }

        scanner.close();

        System.out.println(countValid(passList));
        System.out.println(strictValid(passList));
    }

    private static int strictValid(List<Map<String, String>> passList) {
        return passList.stream()
                .mapToInt(pass -> strictValidation(pass) ? 1 : 0)
                .sum();
    }

    private static int countValid(List<Map<String, String>> passList) {
        return passList.stream()
                .mapToInt(pass -> validPassword(pass) ? 1 : 0)
                .sum();
    }

    private static boolean validPassword(Map<String, String> passport) {
        for (String f : fields) {
            if (!passport.containsKey(f)) {
                return false;
            }
        }
        return true;
    }

    private static boolean strictValidation(Map<String, String> passport) {
        if (!validNumber(passport.get("byr"), 4, 1920, 2002)) {
            return false;
        }
        if (!validNumber(passport.get("iyr"), 4, 2010, 2020)) {
            return false;
        }
        if (!validNumber(passport.get("eyr"), 4, 2020, 2030)) {
            return false;
        }
        String hgt = passport.get("hgt");
        if (hgt == null) {
            return false;
        }
        if (!(hgt.endsWith("cm") || hgt.endsWith("in"))) {
            return false;
        }
        if (hgt.endsWith("cm") && !validNumber(hgt.substring(0, hgt.length() - 2), 0, 150, 193)) {
            return false;
        } else if (hgt.endsWith("in") && !validNumber(hgt.substring(0, hgt.length() - 2), 0, 59, 76)) {
            return false;
        }

        if (!validHair(passport.get("hcl"))) {
            return false;
        }
        if (passport.get("ecl") == null || !eye.contains(passport.get("ecl"))) {
            return false;
        }
        if (!validPid(passport.get("pid"))) {
            return false;
        }
        return true;
    }

    private static boolean validPid(String val) {
        return val != null && val.length() == 9 && val.matches("^[0-9]*$");
    }

    private static boolean validHair(String val) {
        return val != null && val.startsWith("#") && val.length() == 7 && val.substring(1).matches("^[0-9a-f]*$");
    }

    private static boolean validNumber(String val, int len, int min, int max) {
        if (val != null && val.matches("^[0-9]*$") && (len == 0 || val.length() == len)) {
            int vi = Integer.parseInt(val);
            return vi >= min && vi <= max;
        }
        return false;
    }
}
