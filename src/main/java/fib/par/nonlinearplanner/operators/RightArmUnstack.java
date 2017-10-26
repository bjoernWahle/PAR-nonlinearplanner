package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.Clear;
import fib.par.nonlinearplanner.predicates.EmptyArm;
import fib.par.nonlinearplanner.predicates.Holding;
import fib.par.nonlinearplanner.predicates.On;

public class RightArmUnstack extends Operator {
    public Block blockToUnstack;
    public Block lowerBlock;

    public RightArmUnstack(Block blockToUnstack, Block lowerBlock) {
        this.blockToUnstack = blockToUnstack;
        this.lowerBlock = lowerBlock;

        // preconditions
        preconditions.add(new On(blockToUnstack, lowerBlock));
        preconditions.add(new Clear(blockToUnstack));
        preconditions.add(new EmptyArm(Arm.rightArm));

        // add list
        addList.add(new Clear(lowerBlock));
        addList.add(new Holding(blockToUnstack, Arm.rightArm));

        // delete list
        deleteList.add(new On(blockToUnstack, lowerBlock));
        deleteList.add(new EmptyArm(Arm.rightArm));
    }

    @Override
    public String toString() {
        return "RightArmUnstack("+blockToUnstack.simpleRepresentation()+","+lowerBlock.simpleRepresentation()+")";
    }

}
