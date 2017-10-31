package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.domain.predicates.*;

import java.util.Set;

public abstract class Predicate {
    public final Domain domain;

    public Predicate(Domain domain) {
        this.domain = domain;
    }

    public abstract Set<Operator> getPreOperators();
}
