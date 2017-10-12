public class LightBlock extends Predicate {
    Block block;

    public LightBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LightBlock) {
            return ((LightBlock) obj).block.name.equals(block.name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "LightBlock("+block.name+")";
    }
}
