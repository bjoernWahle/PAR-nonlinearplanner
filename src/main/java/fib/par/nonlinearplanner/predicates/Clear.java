package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;

import java.util.List;

public class Clear extends Predicate {
    final Block block;

    public static Clear fromString(String string) {
        if(!string.startsWith("CLEAR(")) {
            throw new IllegalArgumentException("Does not start with CLEAR(");
        }
        String blockName = string.split("CLEAR\\(")[1];
        blockName = blockName.substring(0, blockName.length()-1);
        Clear clear = new Clear(BlocksWorld.getBlockFromName(blockName));
        return clear;
    }

    public Clear(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Clear("+block.name+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clear clear = (Clear) o;

        return block != null ? block.equals(clear.block) : clear.block == null;
    }

    @Override
    public int hashCode() {
        return block != null ? block.hashCode() : 0;
    }
}
