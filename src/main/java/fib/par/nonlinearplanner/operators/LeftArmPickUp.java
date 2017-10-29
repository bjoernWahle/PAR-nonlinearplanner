package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.*;
import fib.par.nonlinearplanner.predicates.*;

public class LeftArmPickUp extends Operator {
    private final Block blockToPickUp;
    private final int usedColsBefore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeftArmPickUp that = (LeftArmPickUp) o;

        if (usedColsBefore != that.usedColsBefore) return false;
        return blockToPickUp != null ? blockToPickUp.equals(that.blockToPickUp) : that.blockToPickUp == null;
    }

    @Override
    public int hashCode() {
        int result = blockToPickUp != null ? blockToPickUp.hashCode() : 0;
        result = 31 * result + usedColsBefore;
        return result;
    }

    public LeftArmPickUp(Block blockToPickUp, int usedColsBefore) {
        super();
        this.blockToPickUp = blockToPickUp;
        this.usedColsBefore = usedColsBefore;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
        preconditions.add(new LightBlock(blockToPickUp));
        preconditions.add(new EmptyArm(Arm.leftArm));
        // set addList
        addList.add(new Holding(blockToPickUp, Arm.leftArm));

        addList.add(new UsedColumnsNum(usedColsBefore-1));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp));
        deleteList.add(new EmptyArm(Arm.leftArm));
        deleteList.add(new UsedColumnsNum(usedColsBefore));
    }

    @Override
    public String toString() {
        return "LeftArmPickUp("+blockToPickUp.simpleRepresentation() +")";
    }
}
