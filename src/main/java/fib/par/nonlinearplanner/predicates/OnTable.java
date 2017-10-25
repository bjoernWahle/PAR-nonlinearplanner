package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.Predicate;

import java.util.HashSet;
import java.util.Set;

public class OnTable extends Predicate {
    Block block;

    public static String INPUT_NAME = "ON-TABLE";

    public OnTable(Block block) {
        this.block = block;
    }

    public static OnTable fromString(String string) {
        if(!string.startsWith(INPUT_NAME+"(")) {
            throw new IllegalArgumentException("Does not start with "+INPUT_NAME+"(");
        }
        String blockName = string.split(INPUT_NAME+"\\(")[1];
        blockName = blockName.substring(0, blockName.length()-1);
        OnTable onTable = new OnTable(BlocksWorld.getBlockFromName(blockName));
        return onTable;
    }
    @Override
    public String toString() {
        return "OnTable("+block.name+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnTable onTable = (OnTable) o;

        return block != null ? block.equals(onTable.block) : onTable.block == null;
    }

    @Override
    public int hashCode() {
        return block != null ? block.hashCode() : 0;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public Set<Operator> getPreOperators() {
        // TODO implement
        return new HashSet<Operator>();
    }
}
