package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.EmptyArm;
import fib.par.nonlinearplanner.predicates.Holding;
import fib.par.nonlinearplanner.predicates.OnTable;

public class Leave extends Operator {
    public Block blockToLeave;
    public Arm arm;

    public Leave(Block blockToLeave, Arm arm) {
        this.blockToLeave = blockToLeave;

        // preconditions
        preconditions.add(new Holding(blockToLeave, arm));
        //preconditions.add(new Columns-Used())

        // add list
        addList.add(new OnTable(blockToLeave));
        addList.add(new EmptyArm(arm));

        // delete list
        deleteList.add(new Holding(blockToLeave, arm));
    }
}
