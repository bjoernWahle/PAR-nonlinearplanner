public class Clear extends Predicate {
    Block block;

    public Clear(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Clear("+block.name+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Clear) {
            return ((Clear) obj).block.name.equals(block.name);
        } else {
            return true;
        }
    }
}
