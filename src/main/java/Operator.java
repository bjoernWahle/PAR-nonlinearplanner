import java.util.List;

abstract class Operator {
    ObjState stateBefore;

    List<Predicate> addList;
    List<Predicate> deleteList;

    Operator(ObjState stateBefore) {
        this.stateBefore = stateBefore;
    }

    public abstract boolean isExecutable();

    public ObjState execute() {
        ObjState stateAfter = stateBefore.clone();
        stateAfter.predicateList.removeAll(deleteList);
        stateAfter.predicateList.addAll(addList);

        return stateAfter;
    }
}
