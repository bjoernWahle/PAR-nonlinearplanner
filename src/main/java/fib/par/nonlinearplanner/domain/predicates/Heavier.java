package fib.par.nonlinearplanner.domain.predicates;

import fib.par.nonlinearplanner.Domain;
import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.Operator;

import java.util.HashSet;
import java.util.Set;

public class Heavier extends Predicate {
    private final Block block1;
    private final Block block2;

    public Heavier(Block block1, Block block2, Domain domain) {
        super(domain);
        this.block1 = block1;
        this.block2 = block2;
    }

/*    public boolean isTrue() {
        return block1.weight > block2.weight;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Heavier heavier = (Heavier) o;

        if (block1 != null ? !block1.equals(heavier.block1) : heavier.block1 != null) return false;
        return block2 != null ? block2.equals(heavier.block2) : heavier.block2 == null;
    }

    @Override
    public int hashCode() {
        int result = block1 != null ? block1.hashCode() : 0;
        result = 31 * result + (block2 != null ? block2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Heavier("+block1+","+block2+")";
    }

    @Override
    public Set<Operator> getPreOperators() {
        return new HashSet<Operator>();
    }
}
