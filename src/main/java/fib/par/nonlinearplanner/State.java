package fib.par.nonlinearplanner;

import com.sun.org.apache.xpath.internal.operations.Neg;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fib.par.nonlinearplanner.BlocksWorld.blocksList;

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

    public State applyOperatorReverse(Operator operator) {
        Set<Predicate> newPredicateSet = new HashSet<Predicate>();
        newPredicateSet.addAll(predicateSet);
        State stateBefore = new State(newPredicateSet);
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

    String simpleRepresentation() {
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

    Set<Operator> getPossiblePreOperators() {
        Set<Operator> operators = new HashSet<Operator>();
        for(Predicate predicate : predicateSet) {
            Set<Operator> preOps = predicate.getPreOperators();
            operators.addAll(preOps);
        }
        return operators;
    }

    boolean isValid() {
        // check used-columns-num
        if(!usedColumnsNumValid()) {
            return false;
        }
        // check empty-arm and holding
        if(!armsPredicatesValid()) {
            return false;
        }
        if(!holdingPredicatesValid()) {
            return false;
        }
        // check onTable valid
        if(!onTablePredicatesValid()) {
            return false;
        }
        return true;
    }

    private boolean usedColumnsNumValid() {
        Set<UsedColumnsNum> usedColumnsNumSet = predicateSet.stream().filter(p -> p instanceof UsedColumnsNum)
                .map(p -> (UsedColumnsNum)p).collect(Collectors.toSet());
        if(usedColumnsNumSet.size()>1) {
            return false;
        }
        // TODO implement on table behaviour
        return true;
    }

    private boolean onTablePredicatesValid() {
        // if a block is on the table, it can be on another block or being held
        Set<Block> onTableBlocks = predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toSet());
        Set<Block> notOnTableBlocks = new HashSet<Block>();
        notOnTableBlocks.addAll(predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toSet()));
        notOnTableBlocks.addAll(predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding)p).getBlock()).collect(Collectors.toSet()));
        onTableBlocks.retainAll(notOnTableBlocks);
        return onTableBlocks.size() == 0;
    }

    private boolean holdingPredicatesValid() {
        Set<Holding> holdings = predicateSet.stream().filter(p -> p instanceof Holding).map(p -> (Holding) p).collect(Collectors.toSet());
        for(Holding holding : holdings) {
            if(holding.getArm().equals(Arm.leftArm)) {
                if(holding.getBlock().weight > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean armsPredicatesValid() {
        Set<Arm> emptyArms = predicateSet.stream().filter(p -> p instanceof EmptyArm).map(p -> ((EmptyArm) p).getArm()).collect(Collectors.toSet());
        Set<Arm> holdingArms = predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding) p).getArm()).collect(Collectors.toSet());
        if(emptyArms.size() + holdingArms.size() > 2) {
            return false;
        };
        // emptyArms contains the intersection of the arms after retainAll
        emptyArms.retainAll(holdingArms);
        // if intersection has elements it means that there is at least one arm that is also holding a block
        return emptyArms.size() == 0;
    }
}
