package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.Clear;
import fib.par.nonlinearplanner.domain.predicates.EmptyArm;
import fib.par.nonlinearplanner.domain.predicates.Holding;
import fib.par.nonlinearplanner.domain.predicates.On;

public class RightArmUnstack extends Operator {
    private final Block blockToUnstack;
    private final Block lowerBlock;

    public RightArmUnstack(Block blockToUnstack, Block lowerBlock, BlocksWorld domain) {
        super(domain);
        this.blockToUnstack = blockToUnstack;
        this.lowerBlock = lowerBlock;

        // preconditions
        preconditions.add(new On(blockToUnstack, lowerBlock, domain));
        preconditions.add(new Clear(blockToUnstack, domain));
        preconditions.add(new EmptyArm(Arm.rightArm, domain));

        // add list
        addList.add(new Clear(lowerBlock, domain));
        addList.add(new Holding(blockToUnstack, Arm.rightArm, domain));

        // delete list
        deleteList.add(new On(blockToUnstack, lowerBlock, domain));
        deleteList.add(new EmptyArm(Arm.rightArm, domain));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RightArmUnstack that = (RightArmUnstack) o;

        if (blockToUnstack != null ? !blockToUnstack.equals(that.blockToUnstack) : that.blockToUnstack != null)
            return false;
        return lowerBlock != null ? lowerBlock.equals(that.lowerBlock) : that.lowerBlock == null;
    }

    @Override
    public int hashCode() {
        int result = blockToUnstack != null ? blockToUnstack.hashCode() : 0;
        result = 31 * result + (lowerBlock != null ? lowerBlock.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RightArmUnstack("+blockToUnstack.simpleRepresentation()+","+lowerBlock.simpleRepresentation()+")";
    }

}
