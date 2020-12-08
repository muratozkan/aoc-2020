import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8 {

    enum Op {
        NOP,
        ACC,
        JMP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    private static class Inst {
        Op op;
        int param;

        public Inst(Op op, int param) {
            this.op = op;
            this.param = param;
        }

        @Override
        public String toString() {
            return op.toString() + " " + param;
        }
    }

    private static class State {
        int acc = 0;
        int pc = 0;
        List<Inst> programMem = new ArrayList<>();

        Inst current() {
            return programMem.get(pc);
        }

        void reset() {
            acc = 0;
            pc = 0;
        }

        boolean normalTermination() {
            return pc == programMem.size();
        }
    }

    public static void main(String[] args) {

        State state = new State();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String instStr = scanner.next();
            int param = scanner.nextInt();

            state.programMem.add(new Inst(Op.valueOf(instStr.toUpperCase()), param));
        }
        scanner.close();

        executeOnce(state, -1);
        System.out.println(state.acc);
        state.reset();

        fixProgram(state);
        System.out.println(state.acc);
    }

    private static void fixProgram(State state) {
        int lastChangedIdx = -1;
        while (!state.normalTermination()) {
            state.reset();
            lastChangedIdx++;
            executeOnce(state, lastChangedIdx);
        }
        System.err.println("Fix at index: " + lastChangedIdx + ", wrong op :" + state.programMem.get(lastChangedIdx));
    }

    private static Op replaceOp(Op original) {
        switch (original) {
            case NOP:
                return Op.JMP;
            case JMP:
                return Op.NOP;
            default:
                return original;
        }
    }

    private static void executeOnce(State state, int repIdx) {
        boolean[] executed = new boolean[state.programMem.size()];
        while (!state.normalTermination() && !executed[state.pc]) {
            executed[state.pc] = true;
            Inst inst = state.current();
            Op op = (repIdx == state.pc) ? replaceOp(inst.op) : inst.op;
            // System.err.println(state.pc + ": " + inst);
            switch (op) {
                case NOP:
                    state.pc++;
                    break;
                case ACC:
                    state.pc++;
                    state.acc += inst.param;
                    break;
                case JMP:
                    state.pc += inst.param;
                    break;
                default:
                    throw new IllegalStateException("Unknown OP");
            }
        }
    }

}
