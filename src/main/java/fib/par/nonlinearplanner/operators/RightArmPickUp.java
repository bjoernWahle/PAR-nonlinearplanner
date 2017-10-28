package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.*;
import fib.par.nonlinearplanner.predicates.*;

public class RightArmPickUp extends Operator {
    Block blockToPickUp;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RightArmPickUp that = (RightArmPickUp) o;

        return blockToPickUp != null ? blockToPickUp.equals(that.blockToPickUp) : that.blockToPickUp == null;
    }

    @Override
    public int hashCode() {
        return blockToPickUp != null ? blockToPickUp.hashCode() : 0;
    }

    public RightArmPickUp(Block blockToPickUp, int usedColsBefore) {
        super();
        this.blockToPickUp = blockToPickUp;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
        // set add list
        addList.add(new Holding(blockToPickUp, Arm.rightArm));
        addList.add(new UsedColumnsNum(usedColsBefore-1));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp));
        deleteList.add(new EmptyArm(Arm.rightArm));
        deleteList.add(new UsedColumnsNum(usedColsBefore));
    }
    public String toString() {
        return "RightArmPickUp("+blockToPickUp.simpleRepresentation()+")";
    }
}
