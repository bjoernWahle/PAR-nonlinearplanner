package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.predicates.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

class BlocksWorldInputParser {

    Planner readInputFile(String path) {
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
            Set<Predicate> heavierPredicates = BlocksWorld.getHeavierPredicateSet();
            // create light predicates
            Set<Predicate> lightBlockPredicates = BlocksWorld.getLightBlockPredicates();

            // init initial state
            State initialState = getState(initialPredicates, heavierPredicates, lightBlockPredicates);
            // init goal state
            State goalState = getState(goalPredicates, heavierPredicates, lightBlockPredicates);

            // return planner
            return new Planner(initialState, goalState);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private State getState(Set<Predicate> predicates, Set<Predicate> heavierPredicates, Set<Predicate> lightBlockPredicates) {
        State state = new State();
        state.addAllPredicates(predicates);
        state.addAllPredicates(heavierPredicates);
        state.addAllPredicates(lightBlockPredicates);

        // create used column predicates
        int usedColumns = 0;
        for(Predicate predicate : state.predicateSet) {
            if(predicate instanceof OnTable) {
                usedColumns++;
            }
        }
        state.addPredicate(new UsedColumnsNum(usedColumns));
        return state;
    }

    private Set<Predicate> readPredicateListFromString(String rawStateString) {
        String stateString = rawStateString.split("State=")[1];
        stateString = stateString.substring(0, stateString.length()-1);
        String[] predicatesArray = stateString.split("\\.");
        Set<Predicate> predicateSet = new HashSet<Predicate>();
        for(String predicateString : predicatesArray) {
            predicateSet.add(Predicate.getPredicateFromString(predicateString));
        }
        return predicateSet;
    }
}
