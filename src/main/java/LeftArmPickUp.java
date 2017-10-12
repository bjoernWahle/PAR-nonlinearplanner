import java.util.LinkedList;
import java.util.List;

public class LeftArmPickUp extends Operator {
    List<Predicate> preconditions;
    Block blockToPickUp;

    LeftArmPickUp(ObjState stateBefore, Block blockToPickUp) {
        super(stateBefore);
        // set precondtions
        preconditions = new LinkedList<Predicate>();
        preconditions.add(new OnTable(blockToPickUp));
        preconditions.add(new Clear(blockToPickUp));
        preconditions.add(new LightBlock(blockToPickUp));
        preconditions.add(new EmptyArm(Arm.leftArm));

        // set addList
        addList = new LinkedList<Predicate>();
        addList.add(new Holding(blockToPickUp, Arm.leftArm));

        // set deleteList
        deleteList = new LinkedList<Predicate>();
        deleteList.add(new OnTable(blockToPickUp));
        deleteList.add(new Clear(blockToPickUp));
        deleteList.add(new EmptyArm(Arm.leftArm));

    }

    public boolean isExecutable() {
        return arePreconditionsMet();
    }

    public boolean arePreconditionsMet() {
        return stateBefore.meetsPreconditions(preconditions);
    }
}
