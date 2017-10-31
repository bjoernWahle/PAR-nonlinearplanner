package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.*;

public class RightArmPickUp extends Operator {
    private final Block blockToPickUp;
    private final int usedColsBefore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RightArmPickUp that = (RightArmPickUp) o;

        if (usedColsBefore != that.usedColsBefore) return false;
        return blockToPickUp != null ? blockToPickUp.equals(that.blockToPickUp) : that.blockToPickUp == null;
    }

    @Override
    public int hashCode() {
        int result = blockToPickUp != null ? blockToPickUp.hashCode() : 0;
        result = 31 * result + usedColsBefore;
        return result;
    }

    public RightArmPickUp(Block blockToPickUp, int usedColsBefore, BlocksWorld domain) {
        super(domain);
        this.blockToPickUp = blockToPickUp;
        this.usedColsBefore = usedColsBefore;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp, domain));
        preconditions.add(new Clear(blockToPickUp, domain));
        // set add list
        addList.add(new Holding(blockToPickUp, Arm.rightArm, domain));
        addList.add(new UsedColumnsNum(usedColsBefore-1, domain));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp, domain));
        deleteList.add(new EmptyArm(Arm.rightArm, domain));
        deleteList.add(new UsedColumnsNum(usedColsBefore, domain));
    }
    public String toString() {
        return "RightArmPickUp("+blockToPickUp.simpleRepresentation()+")";
    }
}
