public class OnTable extends Predicate {
    Block block;

    public OnTable(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "OnTable("+block.name+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OnTable) {
            return  ((OnTable) obj).block == block;
        } else {
            return false;
        }
    }
}
