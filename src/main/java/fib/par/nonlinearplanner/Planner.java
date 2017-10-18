package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;

import java.util.LinkedList;
import java.util.List;

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
}
