package fib.par.nonlinearplanner;

public class Block {
    final public String name;
    final public int weight;

    public Block(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (weight != block.weight) return false;
        return name.equals(block.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + weight;
        return result;
    }

    @Override
    public String toString() {
        String string = name;
        for(int i=0; i< weight; i++) {
            string+= "*";
        }
        return string;
    }
}
