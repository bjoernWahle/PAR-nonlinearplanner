package fib.par.nonlinearplanner;

import java.util.List;

public class Plan {
    final List<Operator> operators;

    public Plan(List<Operator> operators) {
        this.operators = operators;
    }
}
