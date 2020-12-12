import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day12 {

    private enum Action {
        N,
        S,
        E,
        W,
        L,
        R,
        F
    }

    private static class Inst {
        Action act;
        int value;
    }

    private static class Ship {
        // NORTH, EAST, SOUTH, WEST
        static final int[] DIRECTIONS = {0, 1, 2, 3};

        int x = 0;
        int y = 0;
        int direction = 1;
    }

    private static class Waypoint {
        int x = 0;
        int y = 0;

        void rotateRight(int d) {   // 0, 1, 2 or 3
            int t = x;
            switch (d % 4) {
                case 0:
                    break;
                case 1:
                    x = y;
                    y = -t;
                    break;
                case 2:
                    x = -x;
                    y = -y;
                    break;
                case 3:
                    x = -y;
                    y = t;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        List<Inst> instList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Inst inst = new Inst();
            inst.act = Action.valueOf(line.substring(0, 1));
            inst.value = Integer.parseInt(line.substring(1));
            instList.add(inst);
        }
        scanner.close();

        Ship ship = new Ship();
        moveShip(ship, instList);
        System.out.println(Math.abs(ship.x) + Math.abs(ship.y));
        ship = new Ship();
        Waypoint waypoint = new Waypoint();
        waypoint.x = 10;
        waypoint.y = 1;
        moveWaypoint(ship, waypoint, instList);
        System.out.println(Math.abs(ship.x) + Math.abs(ship.y));
    }

    private static void moveWaypoint(Ship ship, Waypoint waypoint, List<Inst> instList) {
        for (Inst inst : instList) {
            switch (inst.act) {
                case N:
                    waypoint.y += inst.value;
                    break;
                case S:
                    waypoint.y -= inst.value;
                    break;
                case E:
                    waypoint.x += inst.value;
                    break;
                case W:
                    waypoint.x -= inst.value;
                    break;
                case R:
                    waypoint.rotateRight(inst.value / 90);
                    break;
                case L:
                    waypoint.rotateRight(4 - inst.value / 90);
                    break;
                case F:
                    ship.x += (waypoint.x * inst.value);
                    ship.y += (waypoint.y * inst.value);
                    break;
                default:
                    throw new IllegalStateException("not reachable");
            }
        }
    }

    private static void moveShip(Ship ship, List<Inst> instList) {
        for (Inst inst : instList) {
            switch (inst.act) {
                case N:
                    ship.y += inst.value;
                    break;
                case S:
                    ship.y -= inst.value;
                    break;
                case E:
                    ship.x += inst.value;
                    break;
                case W:
                    ship.x -= inst.value;
                    break;
                case R:
                    ship.direction = ((inst.value / 90) + ship.direction) % 4;
                    break;
                case L:
                    ship.direction = (ship.direction - (inst.value / 90) + 4) % 4;
                    break;
                case F:
                    int dx = (ship.direction == 1) ? 1 : (ship.direction == 3) ? -1 : 0;
                    int dy = (ship.direction == 0) ? 1 : (ship.direction == 2) ? -1 : 0;
                    ship.x += dx * inst.value;
                    ship.y += dy * inst.value;
                    break;
                default:
                    throw new IllegalStateException("not reachable");
            }
        }
    }
}
