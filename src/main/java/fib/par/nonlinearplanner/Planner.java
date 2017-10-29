package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.Predicate;
import fib.par.nonlinearplanner.util.StateOperatorTree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Planner {
    private final State initialState;
    private final State finalState;
    private State currentState;
    Plan bestPlan;
    private StateOperatorTree stateOperatorTree;
    private boolean planningAlgorithmExecuted;

    public Planner(State initialState, State finalState) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.currentState = initialState;
        this.planningAlgorithmExecuted = false;
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
            // generate discontinued states list
            List<StateOperatorTree.Node> discontinuedNodeList = stateOperatorTree.getInvalidNodes();

            strB.append(numberOfOperators);
            strB.append(System.getProperty("line.separator"));
            strB.append(numberOfStatesProduced);
            strB.append(System.getProperty("line.separator"));
            strB.append(operatorList);
            strB.append(System.getProperty("line.separator"));
            strB.append("---------");
            strB.append(System.getProperty("line.separator"));

            for(StateOperatorTree.Node node :discontinuedNodeList) {
                strB.append(node.getState().predicateListString());
                strB.append(System.getProperty("line.separator"));
                strB.append(node.getStatus().getReason());
                strB.append(System.getProperty("line.separator"));
                strB.append("---------");
                strB.append(System.getProperty("line.separator"));
            }

            return strB.toString();
        } else {
            throw new IllegalStateException("Planning algorithm was not executed yet.");
        }
    }

    private void executePlan(Plan plan) {
        int opNum = 0;
        for(Operator operator: plan.operators) {
            System.out.println(opNum++ +":"+ currentState.simpleRepresentation());
            currentState.printState();
            System.out.println("Applying operator "+ operator);
            currentState = operator.execute(currentState);
        }
        System.out.println(opNum + ":" + currentState.simpleRepresentation());
        currentState.printState();
    }

    public boolean verifyPlan(Plan plan) {
        try{
            executePlan(plan);
        } catch (IllegalStateException e)  {
            return false;
        }
        return isInFinalState();
    }

    private boolean isInFinalState() {
        return currentState.equals(finalState);
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
        Set<State> states = new HashSet<State>();
        // add final state to the list of already reached states
        states.add(finalState);
        for(int i = 0; i < maxLevel; i++) {
            if(initialStateFound) break;
            // iterate over levels and create them
            // 1. get nodes in current level and select only those that should be continued
            List<StateOperatorTree.Node> nodeList = tree.getNodesInLevel(i).stream().filter(StateOperatorTree.Node::isValid).collect(Collectors.toList());
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
                        StateOperatorTree.Node.NodeStatus status;
                        if(!childState.isValid()) {
                            // prior state would be invalid
                            status = StateOperatorTree.Node.NodeStatus.INVALID_STATE;
                        } else if(!operator.isExecutable(childState)) {
                            // operator cannot be executed on the child state (missing prec)
                            status = StateOperatorTree.Node.NodeStatus.OP_PREC_NOT_MET;
                        } else if(states.contains(childState)) {
                            // state was already found in the tree
                            status = StateOperatorTree.Node.NodeStatus.REPEATED_STATE;
                        } else {
                            status = StateOperatorTree.Node.NodeStatus.VALID;
                        }
                        StateOperatorTree.Node child = new StateOperatorTree.Node(childState, operator, status);
                        node.addChild(child);
                        states.add(child.getState());
                        // if the state is the initial state set initialStateFound to true to stop for loops
                        if(status == StateOperatorTree.Node.NodeStatus.VALID && childState.equals(initialState)) {
                            initialStateFound = true;
                            initialStateNode = child;
                            System.out.println("Initial state found in level "+(i+1)+".");
                        }
                    }
                }
            }
        }
        stateOperatorTree = tree;
        return initialStateNode;
    }

    private static boolean regression2(Operator o, Predicate p) {
        return !o.deleteList.contains(p);
    }

    private static boolean regression(Operator o, Set<Predicate> p) {
        return p.containsAll(o.addList) && o.deleteList.stream().noneMatch(p::contains);
    }

    public boolean planWasFound() {
        return bestPlan != null;
    }
}
