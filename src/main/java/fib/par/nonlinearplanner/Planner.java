package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.util.Tree;

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
        Tree<State> stateTree = buildStateTree(0, 2, finalState);
        //Plan plan = binarySearch(regressionLevelTree, initialState);
        return null;
    }

    public static Tree<State> buildStateTree(int currentLevel, int maxLevel, State finalState) {
        Tree<State> tree = new Tree<State>(finalState);
        if(currentLevel == maxLevel || !finalState.isValid()) {
            return tree;
        }
        Set<Operator> operatorSet = finalState.getPossiblePreOperators();
        for(Operator operator : operatorSet) {
            Tree.Node<State> child = buildStateTree(currentLevel+1, maxLevel, finalState.applyOperatorReverse(operator)).getRoot();
            if(child.getData().isValid()) {
                tree.getRoot().addChild(child);
            }
        }
        return tree;
    }
}
