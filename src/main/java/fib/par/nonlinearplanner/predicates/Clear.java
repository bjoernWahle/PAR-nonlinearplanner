package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.LeftArmUnstack;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmUnstack;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Clear extends Predicate {
    private final Block block;

    public static Clear fromString(String string) {
        if(!string.startsWith("CLEAR(")) {
            throw new IllegalArgumentException("Does not start with CLEAR(");
        }
        String blockName = string.split("CLEAR\\(")[1];
        blockName = blockName.substring(0, blockName.length()-1);
        return new Clear(BlocksWorld.getBlockFromName(blockName));
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

    @Override
    public Set<Operator> getPreOperators() {
        Set<Operator> preOperators = new HashSet<Operator>();
        List<Block> otherBlocks = new LinkedList<Block>(BlocksWorld.getBlocksList());
        otherBlocks.remove(block);
        for(Block otherBlock : otherBlocks) {
            if(otherBlock.weight == 1) {
                preOperators.add(new LeftArmUnstack(otherBlock, block));
            }
            preOperators.add(new RightArmUnstack(otherBlock,block));
        }
        return preOperators;
    }
}
