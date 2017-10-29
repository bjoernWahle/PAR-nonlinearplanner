package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.Leave;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.Predicate;

import java.util.HashSet;
import java.util.Set;

public class OnTable extends Predicate {
    private final Block block;

    private static final String INPUT_NAME = "ON-TABLE";

    public OnTable(Block block) {
        this.block = block;
    }

    public static OnTable fromString(String string) {
        if(!string.startsWith(INPUT_NAME+"(")) {
            throw new IllegalArgumentException("Does not start with "+INPUT_NAME+"(");
        }
        String blockName = string.split(INPUT_NAME+"\\(")[1];
        blockName = blockName.substring(0, blockName.length()-1);
        return new OnTable(BlocksWorld.getBlockFromName(blockName));
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
        Set<Operator> preOperators = new HashSet<Operator>();
        for(int i = 0; i < BlocksWorld.MAX_COLUMNS; i++) {
            if(block.weight == 1) {
                preOperators.add(new Leave(block, Arm.leftArm, i));
            }
            preOperators.add(new Leave(block, Arm.rightArm, i));
        }
        return preOperators;
    }
}
