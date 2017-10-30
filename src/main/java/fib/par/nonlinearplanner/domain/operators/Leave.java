package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Negation;
import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.*;

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

    public Leave(Block blockToLeave, Arm arm, int usedColsBefore, BlocksWorld domain) {
        super(domain);
        this.blockToLeave = blockToLeave;
        this.arm = arm;
        this.usedColsBefore = usedColsBefore;

        // preconditions
        preconditions.add(new Holding(blockToLeave, arm, domain));
        preconditions.add(new Negation(new UsedColumnsNum(domain.maxColumns, domain), domain));

        // add list
        addList.add(new OnTable(blockToLeave, domain));
        addList.add(new EmptyArm(arm, domain));
        addList.add(new UsedColumnsNum(usedColsBefore+1, domain));

        // delete list
        deleteList.add(new Holding(blockToLeave, arm, domain));
        deleteList.add(new UsedColumnsNum(usedColsBefore, domain));
    }

    @Override
    public String toString() {
        return "Leave("+blockToLeave.simpleRepresentation() +","+arm+")";
    }
}
