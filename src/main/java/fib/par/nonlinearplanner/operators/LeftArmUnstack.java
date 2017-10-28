package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.*;

public class LeftArmUnstack extends Operator {
    public Block blockToUnstack;
    public Block lowerBlock;

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

    public LeftArmUnstack(Block blockToUnstack, Block lowerBlock) {
        this.blockToUnstack = blockToUnstack;
        this.lowerBlock = lowerBlock;

        // preconditions
        preconditions.add(new LightBlock(blockToUnstack));
        preconditions.add(new EmptyArm(Arm.leftArm));
        preconditions.add(new On(blockToUnstack, lowerBlock));
        preconditions.add(new Clear(blockToUnstack));


        // add list
        addList.add(new Holding(blockToUnstack, Arm.leftArm));
        addList.add(new Clear(lowerBlock));

        // delete list
        deleteList.add(new On(blockToUnstack, lowerBlock));
        deleteList.add(new EmptyArm(Arm.leftArm));
    }

    @Override
    public String toString() {
        return "LeftArmUnstack("+blockToUnstack.simpleRepresentation()+","+lowerBlock.simpleRepresentation()+")";
    }

}
