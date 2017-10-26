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
            System.out.println("Applying operator "+ operator);
            currentState = operator.execute(currentState);
        }
        System.out.println(opNum++ + ":" + currentState.simpleRepresentation());
    }

    public boolean isInFinalState() {
        return currentState.equals(finalState);
    }

    public Plan findBestPlanWithRegression(int maxLevel) {
        StateOperatorTree.Node initialStateNode = buildStateTree(maxLevel, initialState, finalState);
        // if initial state node is null, the algorithm did not find a plan for the problem in the maximum number of
        // levels
        if(initialStateNode == null) {
            return null;
        }
        List<Operator> operatorList = new LinkedList<Operator>();
        StateOperatorTree.Node currentNode = initialStateNode;
        // get list of operators by going through the tree from the initial state node
        while(!currentNode.isRoot()) {
            operatorList.add(currentNode.getOperator());
            currentNode = currentNode.getParent();
        }
        return new Plan(operatorList);
    }

    public static StateOperatorTree.Node buildStateTree(int maxLevel, State initialState, State finalState) {
        StateOperatorTree tree = new StateOperatorTree(finalState, null);
        boolean initialStateFound = false;
        StateOperatorTree.Node initialStateNode = null;
        for(int i = 0; i < maxLevel; i++) {
            if(initialStateFound) {
                break;
            }
            // iterate over levels and create them
            // 1. get nodes in current level
            List<StateOperatorTree.Node> nodeList = tree.getNodesInLevel(i);
            for(StateOperatorTree.Node node : nodeList) {
                if(initialStateFound) {
                    break;
                }
                // for each node, get the state
                State state = node.getState();
                // TODO think about if this could be implemented more efficient
                // for each possible operator that could lead to the current state
                // check if it could be applied using goal regression
                for(Operator operator : state.getPossiblePreOperators()) {
                    if(initialStateFound) {
                        break;
                    }
                    boolean operatorPossible = true;
                    // check if the regression function returns true for all predicates in final state
                    for(Predicate predicate : state.predicateSet) {
                        if(!regression(operator, predicate)) {
                            operatorPossible = false;
                            break;
                        }
                    }
                    // if operator possible, apply it reversely to the state to obtain its previous state
                    if(operatorPossible) {
                        State childState = state.applyOperatorReverse(operator);
                        // if the previous state is valid and the operators preconditions are met
                        // add it to the tree with the operator
                        if(childState.isValid() && operator.isExecutable(childState)) {
                            StateOperatorTree.Node child = new StateOperatorTree.Node(childState, operator);
                            node.addChild(child);
                            // if the state is the initial state set initialStateFound to true to stop for loops
                            if(childState.equals(initialState)) {
                                initialStateFound = true;
                                initialStateNode = child;
                                System.out.println("Initial state found in level "+(i+1)+". Operator: "+ operator);
                            } else {
                                System.out.println("Level: " +(i+1) +" Previous state using "+ operator + " is not the initial state");
                            }
                        }
                    }
                }
            }
        }
        return initialStateNode;
    }

    private static boolean regression(Operator o, Predicate p) {
        return !o.deleteList.contains(p);
    }
}
