public class Heavier extends Predicate {
    Block block1;
    Block block2;

    public Heavier(Block block1, Block block2) {
        this.block1 = block1;
        this.block2 = block2;
    }

/*    public boolean isTrue() {
        return block1.weight > block2.weight;
    }*/

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Heavier) {
            return ((Heavier) obj).block1.name.equals(block1.name) && ((Heavier) obj).block2.name.equals(block2.name);
        } else {
            return false;
        }
    }
}
