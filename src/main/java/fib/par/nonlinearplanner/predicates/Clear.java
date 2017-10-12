package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;

public class Clear extends Predicate {
    final Block block;

    public Clear(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Clear("+block.name+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clear clear = (Clear) o;

        return block != null ? block.equals(clear.block) : clear.block == null;
    }

    @Override
    public int hashCode() {
        return block != null ? block.hashCode() : 0;
    }
}
