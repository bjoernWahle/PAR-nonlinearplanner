public class Holding extends Predicate {
    Block block;
    Arm arm;

    public Holding(Block block, Arm arm) {
        this.block = block;
        this.arm = arm;
    }
}
