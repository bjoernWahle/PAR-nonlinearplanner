package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Arm;

public class EmptyArm extends Predicate {
    final Arm arm;

    public EmptyArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmptyArm emptyArm = (EmptyArm) o;

        return arm != null ? arm.equals(emptyArm.arm) : emptyArm.arm == null;
    }

    @Override
    public int hashCode() {
        return arm != null ? arm.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EmptyArm("+arm+")";
    }
}
