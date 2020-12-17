import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 {

    private static class Pos3 {
        private final int x, y, z;

        public Pos3(int z, int y, int x) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos3 pos3 = (Pos3) o;
            return x == pos3.x && y == pos3.y && z == pos3.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        public Iterator<Pos3> neighbors() {
            return new Iterator<>() {
                int zc = -1;
                int yc = -1;
                int xc = -1;

                @Override
                public boolean hasNext() {
                    return zc <= 1 && yc <= 1 && xc <= 1;
                }

                @Override
                public Pos3 next() {
                    if (zc == 0 && yc == 0 && xc == 0) {
                        xc++;
                    }
                    Pos3 next = new Pos3(z + zc, y + yc, x + xc);
                    xc++;
                    if (xc == 2) {
                        xc = -1;
                        yc++;
                    }
                    if (yc == 2) {
                        zc++;
                        yc = -1;
                    }
                    return next;
                }
            };
        }
    }

    private static class World3 {
        private Set<Pos3> active = new HashSet<>();
        private int[] zBounds = {0, 0};
        private int[] yBounds = {0, 0};
        private int[] xBounds = {0, 0};

        public World3(List<String> lines) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '#') active(0, i, j);
                }
            }
            refreshBounds();
        }

        boolean isActive(int z, int y, int x) {
            return active.contains(new Pos3(z, y, x));
        }

        public void active(int z, int y, int x) {
            Pos3 pos3 = new Pos3(z, y, x);
            active.add(pos3);
        }

        public void refreshBounds() {
            int[] zB = {0, 0};
            int[] yB = {0, 0};
            int[] xB = {0, 0};
            active.forEach(pos3 -> {
                zB[0] = Math.min(pos3.z, zB[0]);
                zB[1] = Math.max(pos3.z, zB[1]);
                yB[0] = Math.min(pos3.y, yB[0]);
                yB[1] = Math.max(pos3.y, yB[1]);
                xB[0] = Math.min(pos3.x, xB[0]);
                xB[1] = Math.max(pos3.x, xB[1]);
            });
            zBounds = zB;
            yBounds = yB;
            xBounds = xB;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int z = zBounds[0]; z <= zBounds[1]; z++) {
                builder.append("z=").append(z).append("\n");
                for (int y = yBounds[0]; y <= yBounds[1]; y++) {
                    for (int x = xBounds[0]; x <= xBounds[1]; x++) {
                        builder.append(isActive(z, y, x) ? '#' : '.');
                    }
                    builder.append("\n");
                }
                builder.append("---");
            }
            return builder.toString();
        }

        void evolve(int gens) {
            int currGen = 0;
            do {
                refreshBounds();
                System.err.println("Generation: " + currGen);
                //System.err.println(this.toString());
                Set<Pos3> nextGen = new HashSet<>();
                Set<Pos3> toEvaluate = active.stream()
                        .flatMap(pos3 -> {
                            Iterator<Pos3> neighbors = pos3.neighbors();
                            return Stream.generate(() -> null)
                                    .takeWhile(b -> neighbors.hasNext())
                                    .map(b -> neighbors.next());
                        })
                        .collect(Collectors.toSet());

                for (Pos3 pos : toEvaluate) {
                    boolean isActive = active.contains(pos);
                    var neighbors = pos.neighbors();
                    int activeNs = 0;
                    while (activeNs < 4 && neighbors.hasNext()) {
                        activeNs += active.contains(neighbors.next()) ? 1 : 0;
                    }
                    boolean nextState = isActive ? (activeNs == 2 || activeNs == 3) : (activeNs == 3);
                    if (nextState) {
                        nextGen.add(pos);
                    }
                }

                currGen++;
                active = nextGen;
            } while (currGen < gens);
        }
    }

    private static class Pos4 {
        private final int w, z, y, x;

        public Pos4(int w, int z, int y, int x) {
            this.w = w;
            this.z = z;
            this.y = y;
            this.x = x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos4 pos4 = (Pos4) o;
            return w == pos4.w && z == pos4.z && y == pos4.y && x == pos4.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(w, z, y, x);
        }

        public Iterator<Pos4> neighbors() {
            return new Iterator<>() {
                int wc = -1;
                int zc = -1;
                int yc = -1;
                int xc = -1;

                @Override
                public boolean hasNext() {
                    return wc <= 1 && zc <= 1 && yc <= 1 && xc <= 1;
                }

                @Override
                public Pos4 next() {
                    if (wc == 0 && zc == 0 && yc == 0 && xc == 0) {
                        xc++;
                    }
                    Pos4 next = new Pos4(w + wc, z + zc, y + yc, x + xc);
                    xc++;
                    if (xc == 2) {
                        xc = -1;
                        yc++;
                    }
                    if (yc == 2) {
                        zc++;
                        yc = -1;
                    }
                    if (zc == 2) {
                        zc = -1;
                        wc++;
                    }
                    return next;
                }
            };
        }
    }

    private static class World4 {
        private Set<Pos4> active = new HashSet<>();
        private int[] wBounds = {0, 0};
        private int[] zBounds = {0, 0};
        private int[] yBounds = {0, 0};
        private int[] xBounds = {0, 0};

        public World4(List<String> lines) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '#') active(0, 0, i, j);
                }
            }
            refreshBounds();
        }

        boolean isActive(int w, int z, int y, int x) {
            return active.contains(new Pos4(w, z, y, x));
        }

        public void active(int w, int z, int y, int x) {
            Pos4 pos4 = new Pos4(w, z, y, x);
            active.add(pos4);
        }

        public void refreshBounds() {
            int[] wB = {0, 0};
            int[] zB = {0, 0};
            int[] yB = {0, 0};
            int[] xB = {0, 0};
            active.forEach(pos4 -> {
                wB[0] = Math.min(pos4.w, wB[0]);
                wB[1] = Math.max(pos4.w, wB[1]);
                zB[0] = Math.min(pos4.z, zB[0]);
                zB[1] = Math.max(pos4.z, zB[1]);
                yB[0] = Math.min(pos4.y, yB[0]);
                yB[1] = Math.max(pos4.y, yB[1]);
                xB[0] = Math.min(pos4.x, xB[0]);
                xB[1] = Math.max(pos4.x, xB[1]);
            });
            wBounds = wB;
            zBounds = zB;
            yBounds = yB;
            xBounds = xB;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int w = wBounds[0]; w < wBounds[1]; w++) {
                for (int z = zBounds[0]; z <= zBounds[1]; z++) {
                    builder.append("z=").append(z).append(", w=").append(w).append("\n");
                    for (int y = yBounds[0]; y <= yBounds[1]; y++) {
                        for (int x = xBounds[0]; x <= xBounds[1]; x++) {
                            builder.append(isActive(w, z, y, x) ? '#' : '.');
                        }
                        builder.append("\n");
                    }
                    builder.append("---");
                }
            }
            return builder.toString();
        }

        void evolve(int gens) {
            int currGen = 0;
            do {
                refreshBounds();
                System.err.println("Generation: " + currGen);
                //System.err.println(this.toString());
                Set<Pos4> nextGen = new HashSet<>();
                Set<Pos4> toEvaluate = active.stream()
                        .flatMap(pos4 -> {
                            Iterator<Pos4> neighbors = pos4.neighbors();
                            return Stream.generate(() -> null)
                                    .takeWhile(b -> neighbors.hasNext())
                                    .map(b -> neighbors.next());
                        })
                        .collect(Collectors.toSet());

                for (Pos4 pos : toEvaluate) {
                    boolean isActive = active.contains(pos);
                    var neighbors = pos.neighbors();
                    int activeNs = 0;
                    while (activeNs < 4 && neighbors.hasNext()) {
                        activeNs += active.contains(neighbors.next()) ? 1 : 0;
                    }
                    boolean nextState = isActive ? (activeNs == 2 || activeNs == 3) : (activeNs == 3);
                    if (nextState) {
                        nextGen.add(pos);
                    }
                }

                currGen++;
                active = nextGen;
            } while (currGen < gens);
        }
    }


    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();

        World3 world3 = new World3(lines);
        world3.evolve(6);
        System.out.println(world3.active.size());

        World4 world4 = new World4(lines);
        world4.evolve(6);
        System.out.println(world4.active.size());
    }
}
