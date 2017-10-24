package fib.par.nonlinearplanner.predicates;

public abstract class Predicate {

    public static Predicate getPredicateFromString(String string) {
        if(string.startsWith("CLEAR(")) {
            return Clear.fromString(string);
        } else if(string.startsWith("EMPTY-ARM(")) {
            return EmptyArm.fromString(string);
        } else if (string.startsWith("ON-TABLE(")) {
            return OnTable.fromString(string);
        } else if (string.startsWith("ON(")){
            return On.fromString(string);
        }
        return null;
    }

}
