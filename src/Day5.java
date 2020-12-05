import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day5 {

    private static class Seat {
        int id;
        int row;
        int col;

        @Override
        public String toString() {
            return "Seat[" + id + ", row=" + row + ", col=" + col + ']';
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Seat> seatList = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Seat seat = parseSeat(line);
            seatList.add(seat);
        }
        scanner.close();

        seatList.sort(Comparator.comparing(seat -> seat.id));

        System.out.println(highestSeatId(seatList));
        System.out.println(missingSeatId(seatList));
    }

    private static int missingSeatId(List<Seat> seatList) {
        int prevId = seatList.get(0).id;
        for (int i = 1; i < seatList.size(); i++) {
            if (seatList.get(i).id != prevId + 1) {
                return prevId + 1;
            }
            prevId = seatList.get(i).id;
        }
        return -1;
    }

    private static int highestSeatId(List<Seat> seatList) {
        return seatList.get(seatList.size() - 1).id;
    }

    private static Seat parseSeat(String line) {
        Seat seat = new Seat();
        seat.row = binaryPartition(line.substring(0, 7), 'F', 'B', 0, 127);
        seat.col = binaryPartition(line.substring(7), 'L', 'R', 0, 7);
        seat.id = seat.row * 8 + seat.col;
        return seat;
    }

    private static int binaryPartition(String part, char lower, char higher, int low, int high) {
        int lo = low;
        int hi = high;
        for (int i = 0; i < part.length(); i++) {
            char ch = part.charAt(i);
            int mid = (hi - lo) / 2 + lo;
            if (ch == lower) {
                hi = mid;
            } else if (ch == higher) {
                lo = mid;
            } else {
                throw new IllegalStateException("unreachable");
            }
        }
        return hi;
    }
}
