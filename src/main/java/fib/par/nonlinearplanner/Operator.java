package fib.par.nonlinearplanner;

import java.util.HashSet;
import java.util.Set;

public abstract class Operator {
    public final Domain domain;
    public final Set<Predicate> preconditions;
    public final Set<Predicate> addList;
    public final Set<Predicate> deleteList;

    public Operator(Domain domain) {
        this.domain = domain;
        this.preconditions = new HashSet<Predicate>();
        this.addList = new HashSet<Predicate>();
        this.deleteList = new HashSet<Predicate>();
    }

    public boolean isExecutable(State stateBefore) {
        return arePreconditionsMet(stateBefore);
    }

    public State execute(State stateBefore) throws IllegalStateException {
        if(isExecutable(stateBefore)) {
            return stateBefore.applyOperator(this);
        } else {
            throw new IllegalStateException("Cannot execute operator " + this + " because preconditions are not met.");
        }
    }

    private boolean arePreconditionsMet(State stateBefore) {
        return stateBefore.meetsPreconditions(preconditions);
    }
}
