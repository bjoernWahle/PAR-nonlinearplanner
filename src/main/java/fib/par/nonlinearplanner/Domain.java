package fib.par.nonlinearplanner;

public abstract class Domain {

    public abstract boolean validateStateInDomain(State state);

    public void printState(State state) {
        System.out.print(stateRepresentation(state));
    };

    public String stateRepresentation(State state) {
        return state.toString();
    }

}
