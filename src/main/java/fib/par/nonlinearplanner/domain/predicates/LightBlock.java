package fib.par.nonlinearplanner.domain.predicates;

import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.BlocksWorld;

import java.util.HashSet;
import java.util.Set;

public class LightBlock extends Predicate {
    private final Block block;

    public LightBlock(Block block, BlocksWorld domain) {
        super(domain);
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

    @Override
    public Set<Operator> getPreOperators() {
        return new HashSet<Operator>();
    }
}
