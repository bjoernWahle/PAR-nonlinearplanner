package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;

import java.util.List;

public class Plan {
    List<Operator> operators;

    public Plan(List<Operator> operators) {
        this.operators = operators;
    }
}
