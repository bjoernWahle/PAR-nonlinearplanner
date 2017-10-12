public class EmptyArm extends Predicate {
    Arm arm;

    public EmptyArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EmptyArm) {
            return ((EmptyArm) obj).arm.equals(this);
        } else {
            return false;
        }
    }
}
