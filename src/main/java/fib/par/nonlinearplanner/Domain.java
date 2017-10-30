package fib.par.nonlinearplanner;

public abstract class Domain {

    public abstract boolean validateStateInDomain(State state);

    public abstract void printState(State state);

}
