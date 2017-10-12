package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.predicates.Predicate;

public class OnTable extends Predicate {
    Block block;

    public OnTable(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "OnTable("+block.name+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnTable onTable = (OnTable) o;

        return block != null ? block.equals(onTable.block) : onTable.block == null;
    }

    @Override
    public int hashCode() {
        return block != null ? block.hashCode() : 0;
    }
}
