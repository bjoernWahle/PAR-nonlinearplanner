abstract class Operator {
    ObjState stateBefore;

    Operator(ObjState stateBefore) {
        this.stateBefore = stateBefore;
    }

    public abstract boolean isExecutable();
}
