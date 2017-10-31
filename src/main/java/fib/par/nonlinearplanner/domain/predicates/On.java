package fib.par.nonlinearplanner.domain.predicates;

import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.operators.Stack;

import java.util.HashSet;
import java.util.Set;

public class On extends Predicate {
    private final Block lowerBlock;
    private final Block upperBlock;

    private static final String INPUT_NAME = "ON";

    public static On fromString(String string, BlocksWorld domain) {
        if(!string.startsWith(INPUT_NAME+"(")) {
            throw new IllegalArgumentException("Does not start with "+INPUT_NAME+"(");
        }
        String blockNames = string.split(INPUT_NAME+"\\(")[1];
        blockNames = blockNames.substring(0, blockNames.length()-1);
        String[] blocksStrings = blockNames.split(",");

        return new On(domain.getBlockFromName(blocksStrings[0]),domain.getBlockFromName(blocksStrings[1]), domain);
    }

    public On(Block b, Block a, BlocksWorld domain) {
        super(domain);
        this.upperBlock = b;
        this.lowerBlock = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        On on = (On) o;

        if (lowerBlock != null ? !lowerBlock.equals(on.lowerBlock) : on.lowerBlock != null) return false;
        return upperBlock != null ? upperBlock.equals(on.upperBlock) : on.upperBlock == null;
    }

    @Override
    public int hashCode() {
        int result = lowerBlock != null ? lowerBlock.hashCode() : 0;
        result = 31 * result + (upperBlock != null ? upperBlock.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "On("+upperBlock.name+","+lowerBlock.name+")";
    }

    public Block getUpperBlock() {
        return upperBlock;
    }

    public Block getLowerBlock() {
        return lowerBlock;
    }

    @Override
    public Set<Operator> getPreOperators() {
        Set<Operator> preOperators = new HashSet<Operator>();
        if(upperBlock.weight == 1){
            preOperators.add(new Stack(upperBlock, lowerBlock, Arm.leftArm, (BlocksWorld) domain));
        }
        preOperators.add(new Stack(upperBlock, lowerBlock, Arm.rightArm, (BlocksWorld) domain));
        return preOperators;
    }
}
