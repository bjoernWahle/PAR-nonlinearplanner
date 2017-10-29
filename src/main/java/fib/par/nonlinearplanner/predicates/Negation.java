package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.operators.Operator;

import java.util.HashSet;
import java.util.Set;

public class Negation extends Predicate {
    public final Predicate predicate;

    public Negation(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Negation negation = (Negation) o;

        return predicate != null ? predicate.equals(negation.predicate) : negation.predicate == null;
    }

    @Override
    public int hashCode() {
        return predicate != null ? predicate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Not("+predicate+")";
    }

    @Override
    public Set<Operator> getPreOperators() {
        return new HashSet<Operator>();
    }
}
