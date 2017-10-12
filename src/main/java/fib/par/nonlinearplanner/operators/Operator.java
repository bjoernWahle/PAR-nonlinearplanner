package fib.par.nonlinearplanner.operators;

import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.predicates.Predicate;

import java.util.HashSet;
import java.util.Set;

public abstract class Operator {

    public final Set<Predicate> preconditions;
    public final Set<Predicate> addList;
    public final Set<Predicate> deleteList;

    Operator() {
        this.preconditions = new HashSet<Predicate>();
        this.addList = new HashSet<Predicate>();
        this.deleteList = new HashSet<Predicate>();
    }

    public boolean isExecutable(State stateBefore) {
        return arePreconditionsMet(stateBefore);
    }

    public State execute(State stateBefore) {
        if(isExecutable(stateBefore)) {
            State stateAfter = stateBefore.applyOperator(this);
            return stateAfter;
        } else {
            throw new IllegalStateException("Cannot execute operator because preconditions are not met.");
        }
    }

    private boolean arePreconditionsMet(State stateBefore) {
        return stateBefore.meetsPreconditions(preconditions);
    }
}
