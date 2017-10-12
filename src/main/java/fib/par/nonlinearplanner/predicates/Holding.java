package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;

public class Holding extends Predicate {
    final Block block;
    final Arm arm;

    public Holding(Block block, Arm arm) {
        this.block = block;
        this.arm = arm;
    }

    @Override
    public String toString() {
        return "Holding("+block+","+arm+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Holding holding = (Holding) o;

        if (block != null ? !block.equals(holding.block) : holding.block != null) return false;
        return arm != null ? arm.equals(holding.arm) : holding.arm == null;
    }

    @Override
    public int hashCode() {
        int result = block != null ? block.hashCode() : 0;
        result = 31 * result + (arm != null ? arm.hashCode() : 0);
        return result;
    }
}
