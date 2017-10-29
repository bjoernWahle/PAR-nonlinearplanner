package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.predicates.*;

public class Leave extends Operator {
    private final Block blockToLeave;
    private final Arm arm;
    private final int usedColsBefore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leave leave = (Leave) o;

        if (usedColsBefore != leave.usedColsBefore) return false;
        if (blockToLeave != null ? !blockToLeave.equals(leave.blockToLeave) : leave.blockToLeave != null) return false;
        return arm != null ? arm.equals(leave.arm) : leave.arm == null;
    }

    @Override
    public int hashCode() {
        int result = blockToLeave != null ? blockToLeave.hashCode() : 0;
        result = 31 * result + (arm != null ? arm.hashCode() : 0);
        result = 31 * result + usedColsBefore;
        return result;
    }

    public Leave(Block blockToLeave, Arm arm, int usedColsBefore) {
        this.blockToLeave = blockToLeave;
        this.arm = arm;
        this.usedColsBefore = usedColsBefore;

        // preconditions
        preconditions.add(new Holding(blockToLeave, arm));
        preconditions.add(new Negation(new UsedColumnsNum(BlocksWorld.MAX_COLUMNS)));

        // add list
        addList.add(new OnTable(blockToLeave));
        addList.add(new EmptyArm(arm));
        addList.add(new UsedColumnsNum(usedColsBefore+1));

        // delete list
        deleteList.add(new Holding(blockToLeave, arm));
        deleteList.add(new UsedColumnsNum(usedColsBefore));
    }

    @Override
    public String toString() {
        return "Leave("+blockToLeave.simpleRepresentation() +","+arm+")";
    }
}
