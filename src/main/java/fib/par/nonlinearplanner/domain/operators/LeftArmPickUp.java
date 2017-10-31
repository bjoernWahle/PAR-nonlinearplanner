package fib.par.nonlinearplanner.domain.operators;

import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.predicates.*;

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

    public LeftArmPickUp(Block blockToPickUp, int usedColsBefore, BlocksWorld domain) {
        super(domain);
        this.blockToPickUp = blockToPickUp;
        this.usedColsBefore = usedColsBefore;
        // set preconditions
        preconditions.add(new OnTable(blockToPickUp, domain));
        preconditions.add(new Clear(blockToPickUp, domain));
        preconditions.add(new LightBlock(blockToPickUp, domain));
        preconditions.add(new EmptyArm(Arm.leftArm, domain));
        // set addList
        addList.add(new Holding(blockToPickUp, Arm.leftArm, domain));

        addList.add(new UsedColumnsNum(usedColsBefore-1, domain));
        // set deleteList
        deleteList.add(new OnTable(blockToPickUp, domain));
        deleteList.add(new EmptyArm(Arm.leftArm, domain));
        deleteList.add(new UsedColumnsNum(usedColsBefore, domain));
    }

    @Override
    public String toString() {
        return "LeftArmPickUp("+blockToPickUp.simpleRepresentation() +")";
    }
}
