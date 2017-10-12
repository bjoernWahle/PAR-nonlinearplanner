import java.util.LinkedList;
import java.util.List;

public class RightArmPickUp extends Operator {
    List<Predicate> preconditions;
    Block blockToPickUp;

    RightArmPickUp(ObjState stateBefore, Block blockToPickUp) {
        super(stateBefore);
        preconditions = new LinkedList<Predicate>();
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
    }

    public boolean isExecutable() {
        return arePreconditionsMet();
    }

    private boolean arePreconditionsMet() {
        return stateBefore.meetsPreconditions(preconditions);
    }
}
