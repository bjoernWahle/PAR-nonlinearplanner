package fib.par.nonlinearplanner.predicates;

public class UsedColumnsNum extends Predicate {
    int usedColumns;

    public UsedColumnsNum(int usedColumns) {
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
}
