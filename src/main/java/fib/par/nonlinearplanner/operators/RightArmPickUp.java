package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.*;
import fib.par.nonlinearplanner.predicates.*;

public class RightArmPickUp extends Operator {
    Block blockToPickUp;

    public RightArmPickUp(Block blockToPickUp) {
        super();
        this.blockToPickUp = blockToPickUp;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
        // set add list
        addList.add(new Holding(blockToPickUp, Arm.rightArm));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp));
        deleteList.add(new Clear(blockToPickUp));
        deleteList.add(new EmptyArm(Arm.rightArm));
    }
}
