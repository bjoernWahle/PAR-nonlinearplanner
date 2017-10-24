package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.predicates.*;

public class Leave extends Operator {
    public Block blockToLeave;
    public Arm arm;

    public Leave(Block blockToLeave, Arm arm, int usedColsBefore) {
        this.blockToLeave = blockToLeave;
        this.arm = arm;

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
