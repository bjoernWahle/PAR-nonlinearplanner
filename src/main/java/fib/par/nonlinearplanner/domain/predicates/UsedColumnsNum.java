package fib.par.nonlinearplanner.domain.predicates;

import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.domain.BlocksWorld;

import java.util.HashSet;
import java.util.Set;

public class UsedColumnsNum extends Predicate {
    public int getUsedColumns() {
        return usedColumns;
    }

    private final int usedColumns;

    public UsedColumnsNum(int usedColumns, BlocksWorld domain) {
        super(domain);
        this.usedColumns = usedColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsedColumnsNum that = (UsedColumnsNum) o;

        return usedColumns == that.usedColumns;
    }

    @Override
    public int hashCode() {
        return usedColumns;
    }

    @Override
    public String toString() {
        return "UsedColumnsNum("+usedColumns+")";
    }

    @Override
    public Set<Operator> getPreOperators() {
        return new HashSet<Operator>();
    }
}
