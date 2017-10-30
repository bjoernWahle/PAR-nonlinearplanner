package fib.par.nonlinearplanner.util;

public enum NodeStatus {
    VALID("Node is valid"),
    REPEATED_STATE("Repeated state"),
    INVALID_STATE("State is not valid"),
    OP_PREC_NOT_MET("Operator preconditions are not fulfilled.");

    final String reason;

    NodeStatus(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
