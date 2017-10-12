package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;

public class LightBlock extends Predicate {
    final Block block;

    public LightBlock(Block block) {
        this.block = block;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightBlock that = (LightBlock) o;

        return block.equals(that.block);
    }

    @Override
    public int hashCode() {
        return block.hashCode();
    }

    @Override
    public String toString() {
        return "LightBlock("+block.name+")";
    }
}
