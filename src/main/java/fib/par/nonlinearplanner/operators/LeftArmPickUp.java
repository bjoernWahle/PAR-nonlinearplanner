package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.*;
import fib.par.nonlinearplanner.predicates.*;

public class LeftArmPickUp extends Operator {
    final Block blockToPickUp;

    public LeftArmPickUp(Block blockToPickUp) {
        super();
        this.blockToPickUp = blockToPickUp;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
        preconditions.add(new LightBlock(blockToPickUp));
        preconditions.add(new EmptyArm(Arm.leftArm));
        // set addList
        addList.add(new Holding(blockToPickUp, Arm.leftArm));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp));
        deleteList.add(new Clear(blockToPickUp));
        deleteList.add(new EmptyArm(Arm.leftArm));
    }
}
