package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.util.NodeStatus;
import fib.par.nonlinearplanner.util.StateOperatorTree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.*;

public class Planner {
    private final State initialState;
    private final State finalState;
    public Plan bestPlan;
    private StateOperatorTree stateOperatorTree;
    private boolean planningAlgorithmExecuted;
    private Set<Pair<State,NodeStatus>> cancelledStates;
    private Domain domain;

    public Planner(State initialState, State finalState, Domain domain) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.planningAlgorithmExecuted = false;
        this.cancelledStates = new HashSet<>();
        this.domain = domain;
    }

    public boolean planWasFound() {
        return bestPlan != null;
    }

    public String generateOutput() {
        if(planningAlgorithmExecuted) {
            StringBuilder strB = new StringBuilder();
            // add number of operators in the best plan
            int numberOfOperators = bestPlan.operators.size();
            // get number of states produced in the tree
            int numberOfStatesProduced = stateOperatorTree.getNodesCount();
            // generate operator list from plan
            String operatorList = String.join(
                    ",",
                    bestPlan.operators.stream().map(Object::toString).collect(Collectors.toList())
            );

            strB.append(numberOfOperators);
            strB.append(System.getProperty("line.separator"));
            strB.append(numberOfStatesProduced);
            strB.append(System.getProperty("line.separator"));
            strB.append(operatorList);
            strB.append(System.getProperty("line.separator"));
            strB.append("---------");
            strB.append(System.getProperty("line.separator"));

            for(Pair<State, NodeStatus> pair:cancelledStates) {
                strB.append(pair.getLeft().predicateListString());
                strB.append(System.getProperty("line.separator"));
                strB.append(pair.getRight().getReason());
                strB.append(System.getProperty("line.separator"));
                strB.append("---------");
                strB.append(System.getProperty("line.separator"));
            }

            return strB.toString();
        } else {
            throw new IllegalStateException("Planning algorithm was not executed yet.");
        }
    }

    private State executePlan(Plan plan) {
        State currentState = initialState;
        int opNum = 0;
        for(Operator operator: plan.operators) {
            System.out.println(opNum++ +":"+ domain.stateRepresentation(currentState));
            domain.printState(currentState);
            System.out.println("Applying operator "+ operator);
            currentState = operator.execute(currentState);
        }
        System.out.println(opNum + ":" + domain.stateRepresentation(currentState));
        domain.printState(currentState);
        return currentState;
    }

    public boolean verifyPlan(Plan plan) {
        try{
            State endState = executePlan(plan);
            return endState.equals(finalState);
        } catch (IllegalStateException e)  {
            return false;
        }

    }

    public void findBestPlanWithRegression(int maxLevel) {
        StateOperatorTree.Node initialStateNode = buildStateTree(maxLevel, initialState, finalState);
        this.planningAlgorithmExecuted = true;
        // if initial state node is null, the algorithm did not find a plan for the problem in the maximum number of
        // levels
        if(initialStateNode == null) {
            return;
        }
        List<Operator> operatorList = new LinkedList<Operator>();
        StateOperatorTree.Node currentNode = initialStateNode;
        // get list of operators by going through the tree from the initial state node
        while(!currentNode.isRoot()) {
            operatorList.add(currentNode.getOperator());
            currentNode = currentNode.getParent();
        }
        bestPlan = new Plan(operatorList);
    }

    private StateOperatorTree.Node buildStateTree(int maxLevel, State initialState, State finalState) {
        StateOperatorTree tree = new StateOperatorTree(finalState);
        boolean initialStateFound = false;
        StateOperatorTree.Node initialStateNode = null;
        Set<State> visitedStates = new HashSet<State>();
        // add final state to the list of already reached states
        visitedStates.add(finalState);
        for(int i = 0; i < maxLevel; i++) {
            if(initialStateFound) break;
            // iterate over levels and create them
            // 1. get nodes in current level and select only those that should be continued
            List<StateOperatorTree.Node> nodeList = tree.getNodesInLevel(i);
            if(nodeList.size() == 0) {
                break;
            }
            for(StateOperatorTree.Node node : nodeList) {
                if(initialStateFound) break;
                // for each node, get the state
                State state = node.getState();
                // TODO think about if this could be implemented more efficient
                // for each possible operator that could lead to the current state
                // check if it could be applied using goal regression
                Set<Operator> possiblePreOperators = state.getPossiblePreOperators();
                for(Operator operator : possiblePreOperators) {
                    if(initialStateFound) break;
                    // if operator possible, apply it reversely to the state to obtain its previous state
                    if(regression(operator, state.predicateSet)) {
                        State childState = state.applyOperatorReverse(operator);
                        // if the previous state is valid and the operators preconditions are met
                        // add it to the tree with the operator
                        NodeStatus status;
                        if(!childState.isValid()) {
                            // prior state would be invalid
                            status = NodeStatus.INVALID_STATE;
                        } else if(!operator.isExecutable(childState)) {
                            // operator cannot be executed on the child state (missing prec)
                            status = NodeStatus.OP_PREC_NOT_MET;
                        } else if(visitedStates.contains(childState)) {
                            // state was already found in the tree
                            status = NodeStatus.REPEATED_STATE;
                        } else {
                            status = NodeStatus.VALID;
                        }
                        StateOperatorTree.Node child = new StateOperatorTree.Node(childState, operator);
                        if(status.equals(NodeStatus.VALID)) {
                            node.addChild(child);
                            visitedStates.add(child.getState());
                            // if the state is the initial state set initialStateFound to true to stop for loops
                            if(childState.equals(initialState)) {
                                initialStateFound = true;
                                initialStateNode = child;
                                System.out.println("Initial state found in level "+(i+1)+".");
                            }
                        } else {
                            cancelledStates.add(new ImmutablePair<>(state, status));
                        }

                    }
                }
            }
        }
        stateOperatorTree = tree;
        return initialStateNode;
    }

    private static boolean regression(Operator o, Set<Predicate> p) {
        return p.containsAll(o.addList) && o.deleteList.stream().noneMatch(p::contains);
    }
}
