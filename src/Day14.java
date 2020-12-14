import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day14 {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        Map<Integer, Long> memory = new HashMap<>();
        long setMask = 0L;
        long unsetMask = 0L;

        String mask = "";
        Map<Long, Long> memoryV2 = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("mask")) {
                mask = line.substring(7);
                setMask = buildSetMask(mask);
                unsetMask = buildUnsetMask(mask);
            } else {
                String[] parts = line.split("=");
                int address = Integer.parseInt(parts[0].substring(4, parts[0].length() - 2));
                long value = Long.parseLong(parts[1].substring(1));
                memory.put(address, convertValue(setMask, unsetMask, value));
                for (long addr : buildAddr(mask, address)) {
                    memoryV2.put(addr, value);
                }
            }
        }

        System.out.println(memory.values().stream().mapToLong(l -> l).sum());
        BigInteger sum = BigInteger.valueOf(0L);
        for (Long v : memoryV2.values()) {
            sum = sum.add(BigInteger.valueOf(v));
        }
        System.out.println(sum.toString());
    }

    private static List<Long> buildAddr(String mask, long address) {
        long setMask = 0L;
        long unsetMask = 0L;
        List<Integer> floatIdx = new ArrayList<>();
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '0') continue;
            if (mask.charAt(i) == '1')
                setMask |= (1L << (mask.length() - i - 1));
            else {
                unsetMask |= (1L << (mask.length() - i - 1));    // 0 the address
                floatIdx.add(i);
            }
        }
        long base = (address & ~unsetMask) | setMask;
        floatIdx.sort(Comparator.reverseOrder());
        List<Long> list = new ArrayList<>();
        int setSize = BigInteger.valueOf(2).pow(floatIdx.size()).intValue();
        for (int i = 0; i < setSize; i++) {
            long setI = 0L;
            for (int j = floatIdx.size() - 1; j >= 0; j--) {
                int s = (i >> j) & 1;
                if (s == 1) {
                    setI |= (1L << (mask.length() - floatIdx.get(j) - 1));
                }
            }
            list.add(base | setI);
            if (DEBUG) {
                for (int j = 35; j >= 0; j--) {
                    int c = (int) ((setI >> j) & 1);
                    if (c == 1) {
                        System.err.print("v");
                    } else {
                        System.err.print(" ");
                    }
                }
                System.err.println();
                System.err.println(mask + "\n" + Long.toBinaryString(base) + "\n" + Long.toBinaryString(base | setI));
            }
        }
        return list;
    }

    private static long buildSetMask(String mask) {
        long val = 0L;
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '1') val |= (1L << (mask.length() - i - 1));
        }
        // System.err.println(Long.toBinaryString(val));
        return val;
    }

    private static long buildUnsetMask(String mask) {
        long val = 0L;
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '0') val |= (1L << (mask.length() - i - 1));
        }
        // System.err.println(Long.toBinaryString(val));
        return val;
    }

    private static long convertValue(long setMask, long unsetMask, long value) {
        return (value | setMask) & ~unsetMask;
        // System.err.println(Long.toBinaryString(value) + "-->" + Long.toBinaryString(result));
    }
}
