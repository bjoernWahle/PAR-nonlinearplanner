package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.predicates.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BlocksWorld {

    public static int MAX_COLUMNS;

    public static List<Block> blocksList;

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

        URL resource = BlocksWorld.class.getResource("/input3");
        //URL resource = BlocksWorld.class.getResource("/simple1");

        File file = null;
        try {
            file = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Planner myPlanner = parser.readInputFile(file);
        myPlanner.findBestPlanWithRegression(100);
        if(myPlanner.planWasFound()) {
            Plan plan = myPlanner.bestPlan;
            boolean validPlan = myPlanner.verifyPlan(plan);
            System.out.println("Best plan is valid: "+validPlan);

//            System.out.println("Output: ");
//            System.out.println(myPlanner.generateOutput());
        } else {
            System.out.println("No plan could be found.");
        }

    }

    public static List<Block> getBlocksList() {
        return blocksList;
    }
}
