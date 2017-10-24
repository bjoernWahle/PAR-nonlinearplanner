package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Leave;
import fib.par.nonlinearplanner.operators.LeftArmPickUp;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmPickUp;;
import fib.par.nonlinearplanner.predicates.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.in;

public class BlocksWorld {

    public static int MAX_COLUMNS;

    public static List<Block> blocksList;

    public static Planner readInputFile(String path) {
        try {
            String absolutePath = BlocksWorld.class.getClassLoader().getResource(path).getPath();
            List<String> inputStrings = Files.readAllLines(FileSystems.getDefault().getPath(absolutePath.split("C:")[1]), StandardCharsets.UTF_8);
            String columnsString = inputStrings.get(0);
            String blocksString = inputStrings.get(1);
            String initialStateString = inputStrings.get(2);
            String goalStateString = inputStrings.get(3);

            // extract max column number
            String maxColumns = columnsString.split("MaxColumns=")[1];
            int maxColumnsInt = Integer.parseInt(maxColumns.substring(0, maxColumns.length()-1));

            // extract blocks
            String blocks = blocksString.split("Blocks=")[1];
            blocks = blocks.substring(0, blocks.length()-1);
            String[] blocksArray = blocks.split("\\.");
            BlocksWorld.blocksList = new LinkedList<Block>();
            for(String blockString : blocksArray) {
                String name = blockString.substring(0,1);
                int weight = blockString.substring(1).length();
                BlocksWorld.blocksList.add(new Block(name, weight));
            }

            // extract states
            Set<Predicate> initialPredicates = readPredicateListFromString(initialStateString);
            Set<Predicate> goalPredicates = readPredicateListFromString(goalStateString);

            // create heavier predicates
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
            // create light predicates
            Set<Predicate> lightBlockPredicates = new HashSet<Predicate>();
            for(Block block: blocksList) {
                if(block.weight == 1) {
                    lightBlockPredicates.add(new LightBlock(block));
                }
            }

            State initialState = new State();
            initialState.addAllPredicates(initialPredicates);
            initialState.addAllPredicates(heavierPredicates);
            initialState.addAllPredicates(lightBlockPredicates);

            // create used column predicates
            int usedColumns = 0;
            for(Predicate predicate : initialState.predicateSet) {
                if(predicate instanceof OnTable) {
                    usedColumns++;
                }
            }
            initialState.addPredicate(new UsedColumnsNum(usedColumns));

            // create goal state
            State goalState = new State();
            goalState.addAllPredicates(initialPredicates);
            goalState.addAllPredicates(heavierPredicates);
            goalState.addAllPredicates(lightBlockPredicates);

            // create used column predicates
            int usedColumnsAfter = 0;
            for(Predicate predicate : goalState.predicateSet) {
                if(predicate instanceof OnTable) {
                    usedColumnsAfter++;
                }
            }
            goalState.addPredicate(new UsedColumnsNum(usedColumnsAfter));

            return new Planner(initialState, goalState);



        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Block getBlockFromName(String name) {
        for(Block block: blocksList) {
            if(Objects.equals(block.name, name)) {
                return block;
            }
        }
        throw new IllegalArgumentException("No block with name " + name+ " found.");
    }

    public static Set<Predicate> readPredicateListFromString(String rawStateString) {
        String stateString = rawStateString.split("InitialState=")[1];
        stateString = stateString.substring(0, stateString.length()-1);
        String[] predicatesArray = stateString.split("\\.");
        Set<Predicate> predicateSet = new HashSet<Predicate>();
        for(String predicateString : predicatesArray) {
            predicateSet.add(Predicate.getPredicateFromString(predicateString));
        }
        return predicateSet;
    }

    public static void main(String[] args) {

        Planner myPlanner = readInputFile("input");

    }
}
