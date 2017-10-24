package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.BlocksWorld;

public class EmptyArm extends Predicate {
    final Arm arm;

    public static String INPUT_NAME = "EMPTY-ARM";

    public EmptyArm(Arm arm) {
        this.arm = arm;
    }

    public static EmptyArm fromString(String string) {
        if(!string.startsWith(INPUT_NAME+"(")) {
            throw new IllegalArgumentException("Does not start with "+INPUT_NAME+"(");
        }
        String armName = string.split(INPUT_NAME+"\\(")[1];
        armName = armName.substring(0, armName.length()-1);
        EmptyArm emptyArm = new EmptyArm(Arm.fromString(armName));

        return emptyArm;
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
