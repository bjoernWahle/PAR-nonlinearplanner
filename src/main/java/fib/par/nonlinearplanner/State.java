package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.domain.predicates.*;

import java.util.*;
import java.util.stream.Collectors;

public class State {
    public final Set<Predicate> predicateSet;
    private Domain domain;

    public State(Domain domain) {
        predicateSet = new HashSet<Predicate>();
        this.domain = domain;
    }

    private State(Set<Predicate> predicateSet, Domain domain) {
        this.predicateSet = predicateSet;
        this.domain = domain;
    }

    private boolean meetsPrecondition(Predicate precondition) {
        if(precondition instanceof Negation) {
            return !predicateSet.contains(((Negation) precondition).predicate);
        } else {
            return predicateSet.contains(precondition);
        }
    }

    public boolean meetsPreconditions(Set<Predicate> preconditions) {
        for(Predicate precondition: preconditions) {
            if(!meetsPrecondition(precondition)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("State(Predicates: ");
        for(Predicate predicate : predicateSet) {
            str.append(predicate).append(",");
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        str.append(")");
        return str.toString();
    }

    public String predicateListString() {
        return String.join(",", predicateSet.stream().filter(p -> !(p instanceof Heavier) && !(p instanceof LightBlock) ).map(Predicate::toString).collect(Collectors.toList()));
    }

    public void addPredicate(Predicate predicate) {
        predicateSet.add(predicate);
    }

    public void addAllPredicates(Set<Predicate> predicates) {
        predicateSet.addAll(predicates);
    }

    public State applyOperator(Operator operator) {
        Set<Predicate> newPredicateSet = new HashSet<Predicate>();
        newPredicateSet.addAll(predicateSet);
        State newState = new State(newPredicateSet, domain);
        newState.predicateSet.removeAll(operator.deleteList);
        newState.predicateSet.addAll(operator.addList);
        return newState;
    }

    public State applyOperatorReverse(Operator operator) {
        Set<Predicate> newPredicateSet = new HashSet<Predicate>(predicateSet);
        State stateBefore = new State(newPredicateSet, domain);
        stateBefore.predicateSet.addAll(operator.deleteList);
        stateBefore.predicateSet.removeAll(operator.addList);
        return stateBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return predicateSet != null ? predicateSet.equals(state.predicateSet) : state.predicateSet == null;
    }

    @Override
    public int hashCode() {
        return predicateSet != null ? predicateSet.hashCode() : 0;
    }

    Set<Operator> getPossiblePreOperators() {
        Set<Operator> operators = new HashSet<Operator>();
        for(Predicate predicate : predicateSet) {
            Set<Operator> preOps = predicate.getPreOperators();
            operators.addAll(preOps);
        }
        return operators;
    }

    boolean isValid() {
        return domain.validateStateInDomain(this);
    }
}
