package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Negation;
import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.*;

public class Stack extends Operator {
    private final Block blockToStack;
    private final Block lowerBlock;
    private final Arm arm;

    public Stack(Block blockToStack, Block lowerBlock, Arm arm, BlocksWorld domain) {
        super(domain);
        this.blockToStack = blockToStack;
        this.lowerBlock = lowerBlock;
        this.arm = arm;

        Arm otherArm = arm.equals(Arm.leftArm) ? Arm.rightArm : Arm.leftArm;
        // preconditions
        preconditions.add(new Holding(blockToStack, arm, domain));
        preconditions.add(new Clear(lowerBlock, domain));
        preconditions.add(new Heavier(lowerBlock, blockToStack, domain));
        preconditions.add(new Negation(new Holding(lowerBlock, otherArm, domain), domain));

        // add list
        addList.add(new On(blockToStack, lowerBlock, domain));
        addList.add(new EmptyArm(arm, domain));

        // delete list
        deleteList.add(new Clear(lowerBlock, domain));
        deleteList.add(new Holding(blockToStack, arm, domain));
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
