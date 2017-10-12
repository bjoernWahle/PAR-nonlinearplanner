package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;

public class On extends Predicate {
    Block lowerBlock;
    Block upperBlock;

    public On(Block b, Block a) {
        this.upperBlock = b;
        this.lowerBlock = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        On on = (On) o;

        if (lowerBlock != null ? !lowerBlock.equals(on.lowerBlock) : on.lowerBlock != null) return false;
        return upperBlock != null ? upperBlock.equals(on.upperBlock) : on.upperBlock == null;
    }

    @Override
    public int hashCode() {
        int result = lowerBlock != null ? lowerBlock.hashCode() : 0;
        result = 31 * result + (upperBlock != null ? upperBlock.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "On("+upperBlock.name+","+lowerBlock.name+")";
    }
}
