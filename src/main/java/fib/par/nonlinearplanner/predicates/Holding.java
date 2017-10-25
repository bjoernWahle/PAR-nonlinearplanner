package fib.par.nonlinearplanner.predicates;

import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Holding extends Predicate {
    final Block block;
    final Arm arm;

    private static String INPUT_NAME = "HOLDING";

    public Holding(Block block, Arm arm) {
        this.block = block;
        this.arm = arm;
    }

    public static Holding fromString(String string) {
        if(!string.startsWith(INPUT_NAME+"(")) {
            throw new IllegalArgumentException("Does not start with "+INPUT_NAME+"(");
        }
        String paramsNames = string.split(INPUT_NAME+"\\(")[1];
        paramsNames = paramsNames.substring(0, paramsNames.length()-1);
        String blockName = paramsNames.split(",")[0];
        String armName = paramsNames.split(",")[1];
        Holding holding = new Holding(BlocksWorld.getBlockFromName(blockName),Arm.fromString(armName));

        return holding;
    }

    @Override
    public String toString() {
        return "Holding("+block+","+arm+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Holding holding = (Holding) o;

        if (block != null ? !block.equals(holding.block) : holding.block != null) return false;
        return arm != null ? arm.equals(holding.arm) : holding.arm == null;
    }

    @Override
    public int hashCode() {
        int result = block != null ? block.hashCode() : 0;
        result = 31 * result + (arm != null ? arm.hashCode() : 0);
        return result;
    }

    public Arm getArm() {
        return arm;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public Set<Operator> getPreOperators() { //pickUp, unstack
        Set<Operator> preOperators = new HashSet<Operator>();
        List<Block> otherBlocks = BlocksWorld.getBlocksList();
        otherBlocks.remove(block);
        for(Block otherBlock : otherBlocks) {
            if(otherBlock.weight == 1) {
                preOperators.add(new LeftArmUnstack(otherBlock, block));
            }
            preOperators.add(new RightArmUnstack(otherBlock,block));
        }
        for (int i= 1; i < BlocksWorld.MAX_COLUMNS; i++){
            if(block.weight == 1){
                preOperators.add(new LeftArmPickUp(block, i));
            }
            preOperators.add(new RightArmPickUp(block, i));
        }
        return preOperators;
    }
}
