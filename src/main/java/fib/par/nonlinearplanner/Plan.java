package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;

import java.util.List;

class Plan {
    final List<Operator> operators;

    public Plan(List<Operator> operators) {
        this.operators = operators;
    }
}
