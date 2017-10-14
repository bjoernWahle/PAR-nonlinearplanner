package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.*;

public class Stack extends Operator {
    public Block blockToStack;
    public Block lowerBlock;
    public Arm arm;

    public Stack(Block blockToStack, Block lowerBlock, Arm arm) {
        this.blockToStack = blockToStack;
        this.lowerBlock = lowerBlock;

        // preconditions
        preconditions.add(new Holding(blockToStack, arm));
        preconditions.add(new Clear(lowerBlock));
        preconditions.add(new Heavier(lowerBlock, blockToStack));

        // add list
        addList.add(new On(blockToStack, lowerBlock));
        addList.add(new EmptyArm(arm));

        // delete list
        deleteList.add(new Clear(lowerBlock));
        deleteList.add(new Holding(blockToStack, arm));
    }
}
