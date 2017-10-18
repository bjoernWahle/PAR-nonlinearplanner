package fib.par.nonlinearplanner;

import com.sun.org.apache.xpath.internal.operations.Neg;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.Heavier;
import fib.par.nonlinearplanner.predicates.LightBlock;
import fib.par.nonlinearplanner.predicates.Negation;
import fib.par.nonlinearplanner.predicates.Predicate;

import java.util.HashSet;
import java.util.Set;

public class State {
    final Set<Predicate> predicateSet;

    State() {
        predicateSet = new HashSet<Predicate>();
    }

    public State(Set<Predicate> predicateSet) {
        this.predicateSet = predicateSet;
    }

    public boolean meetsPrecondition(Predicate precondition) {
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
        String str = "";
        str += "State: ";
        str += "(Predicates: ";
        for(Predicate predicate : predicateSet) {
            str += predicate + ",";
        }
        str = str.substring(0, str.length()-1);
        str += ")";
        return str;
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
        State newState = new State(newPredicateSet);
        newState.predicateSet.removeAll(operator.deleteList);
        newState.predicateSet.addAll(operator.addList);
        return newState;
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

    public String simpleRepresentation() {
        String str = "";
        str += "State: ";
        str += "(Predicates: ";
        for(Predicate predicate : predicateSet) {
            if(!(predicate instanceof Heavier || predicate instanceof LightBlock)) {
                str += predicate + ",";
            }
        }
        str = str.substring(0, str.length()-1);
        str += ")";
        return str;
    }
}
