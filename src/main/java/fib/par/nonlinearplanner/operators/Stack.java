package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.*;

public class Stack extends Operator {
    private final Block blockToStack;
    private final Block lowerBlock;
    private final Arm arm;

    public Stack(Block blockToStack, Block lowerBlock, Arm arm) {
        this.blockToStack = blockToStack;
        this.lowerBlock = lowerBlock;
        this.arm = arm;

        Arm otherArm = arm.equals(Arm.leftArm) ? Arm.rightArm : Arm.leftArm;
        // preconditions
        preconditions.add(new Holding(blockToStack, arm));
        preconditions.add(new Clear(lowerBlock));
        preconditions.add(new Heavier(lowerBlock, blockToStack));
        preconditions.add(new Negation(new Holding(lowerBlock, otherArm)));

        // add list
        addList.add(new On(blockToStack, lowerBlock));
        addList.add(new EmptyArm(arm));

        // delete list
        deleteList.add(new Clear(lowerBlock));
        deleteList.add(new Holding(blockToStack, arm));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stack stack = (Stack) o;

        if (blockToStack != null ? !blockToStack.equals(stack.blockToStack) : stack.blockToStack != null) return false;
        if (lowerBlock != null ? !lowerBlock.equals(stack.lowerBlock) : stack.lowerBlock != null) return false;
        return arm != null ? arm.equals(stack.arm) : stack.arm == null;
    }

    @Override
    public int hashCode() {
        int result = blockToStack != null ? blockToStack.hashCode() : 0;
        result = 31 * result + (lowerBlock != null ? lowerBlock.hashCode() : 0);
        result = 31 * result + (arm != null ? arm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Stack("+blockToStack.simpleRepresentation()+","+lowerBlock.simpleRepresentation()+","+arm +")";
    }
}
