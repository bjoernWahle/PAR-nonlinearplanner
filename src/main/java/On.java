public class On extends Predicate {
    Block lowerBlock;
    Block upperBlock;

    public On(Block b, Block a) {
        this.upperBlock = b;
        this.lowerBlock = a;
    }

    @Override
    public String toString() {
        return "On("+upperBlock.name+","+lowerBlock.name+")";
    }
}
