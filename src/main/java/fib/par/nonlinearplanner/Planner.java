package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.Predicate;
import fib.par.nonlinearplanner.util.StateOperatorTree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Planner {
    State initialState;
    State finalState;
    State currentState;
    List<State> pastStates;

    public Planner(State initialState, State finalState) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.currentState = initialState;
        this.pastStates = new LinkedList<State>();
    }

    public void executePlan(Plan plan) {
        int opNum = 0;
        for(Operator operator: plan.operators) {
            System.out.println(opNum++ +":"+ currentState.simpleRepresentation());
            currentState = operator.execute(currentState);
        }
        System.out.println(opNum++ + ":" + currentState.simpleRepresentation());
    }

    public boolean isInFinalState() {
        return currentState.equals(finalState);
    }

    public Plan findBestPlanWithRegression() {
        StateOperatorTree stateTree = buildStateTree(0, 5, initialState, finalState);
        //Plan plan = binarySearch(regressionLevelTree, initialState);
        return null;
    }

    public static StateOperatorTree buildStateTree(int currentLevel, int maxLevel, State initialState, State finalState) {
        StateOperatorTree tree = new StateOperatorTree(finalState, null);
        if(currentLevel == maxLevel || !finalState.isValid() || initialState.equals(finalState)) {
            if(initialState.equals(finalState)) {
                System.out.println("Initial state found!!");
            }
            return tree;
        }
        Set<Operator> operatorSet = finalState.getPossiblePreOperators();
        for(Operator operator : operatorSet) {
            boolean operatorPossible = true;
            // check if the regression function returns true for all predicates in final state
            for(Predicate predicate : finalState.predicateSet) {
                if(!regression(operator, predicate)) {
                    operatorPossible = false;
                    break;
                }
            }
            // if not, continue with next operator
            if(!operatorPossible) {
                continue;
            }
            StateOperatorTree.Node child = buildStateTree(currentLevel+1, maxLevel, initialState, finalState.applyOperatorReverse(operator)).getRoot();
            child.setOperator(operator);
            if(child.getState().isValid()) {
                tree.getRoot().addChild(child);
            }
        }
        return tree;
    }

    private static boolean regression(Operator o, Predicate p) {
        return !o.deleteList.contains(p);
    }
}
