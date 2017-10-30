package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.*;

public class LeftArmUnstack extends Operator {
    private final Block blockToUnstack;
    private final Block lowerBlock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeftArmUnstack that = (LeftArmUnstack) o;

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

    public LeftArmUnstack(Block blockToUnstack, Block lowerBlock, BlocksWorld domain) {
        super(domain);
        this.blockToUnstack = blockToUnstack;
        this.lowerBlock = lowerBlock;

        // preconditions
        preconditions.add(new LightBlock(blockToUnstack, domain));
        preconditions.add(new EmptyArm(Arm.leftArm, domain));
        preconditions.add(new On(blockToUnstack, lowerBlock, domain));
        preconditions.add(new Clear(blockToUnstack, domain));


        // add list
        addList.add(new Holding(blockToUnstack, Arm.leftArm, domain));
        addList.add(new Clear(lowerBlock, domain));

        // delete list
        deleteList.add(new On(blockToUnstack, lowerBlock, domain));
        deleteList.add(new EmptyArm(Arm.leftArm, domain));
    }

    @Override
    public String toString() {
        return "LeftArmUnstack("+blockToUnstack.simpleRepresentation()+","+lowerBlock.simpleRepresentation()+")";
    }

}
