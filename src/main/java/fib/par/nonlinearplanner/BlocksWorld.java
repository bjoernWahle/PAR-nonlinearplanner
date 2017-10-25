package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.predicates.*;
import fib.par.nonlinearplanner.util.Tree;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BlocksWorld {

    public static int MAX_COLUMNS;

    static List<Block> blocksList;

    static Set<Predicate> getLightBlockPredicates() {
        Set<Predicate> lightBlockPredicates = new HashSet<Predicate>();
        for(Block block: blocksList) {
            if(block.weight == 1) {
                lightBlockPredicates.add(new LightBlock(block));
            }
        }
        return lightBlockPredicates;
    }

    static Set<Predicate> getHeavierPredicateSet() {
        Set<Predicate> heavierPredicates = new HashSet<Predicate>();
        for(Block block1: blocksList) {
            for(Block block2: blocksList) {
                if(!block1.equals(block2)) {
                    if(block1.weight >= block2.weight) {
                        heavierPredicates.add(new Heavier(block1, block2));
                    }
                }
            }
        }
        return heavierPredicates;
    }

    public static Block getBlockFromName(String name) {
        for(Block block: blocksList) {
            if(Objects.equals(block.name, name)) {
                return block;
            }
        }
        throw new IllegalArgumentException("No block with name " + name+ " found.");
    }

    public static void main(String[] args) {
        BlocksWorldInputParser parser = new BlocksWorldInputParser();
        Planner myPlanner = parser.readInputFile("input");
        System.out.println(myPlanner.finalState.getPossiblePreOperators());
        EmptyArm emptyArm = new EmptyArm(Arm.leftArm);
        System.out.println(emptyArm.getPreOperators());
        Tree<State> stateTree = Planner.buildStateTree(0, 3, myPlanner.finalState);
        int x = 4;
    }

    public static List<Block> getBlocksList() {
        return blocksList;
    }
}
