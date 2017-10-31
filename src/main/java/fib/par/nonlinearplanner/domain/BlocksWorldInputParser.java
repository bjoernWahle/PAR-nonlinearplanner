package fib.par.nonlinearplanner.domain;

import com.sun.java.browser.plugin2.DOM;
import fib.par.nonlinearplanner.Planner;
import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.domain.predicates.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BlocksWorldInputParser {

    public Planner readInputFile(File file) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String columnsString = br.readLine();
            String blocksString = br.readLine();
            String initialStateString = br.readLine();
            String goalStateString = br.readLine();

            // extract max column number
            String maxColumnsString = columnsString.split("MaxColumns=")[1];
            int maxColumns = Integer.parseInt(maxColumnsString.substring(0, maxColumnsString.length()-1));

            // extract blocks
            String blocks = blocksString.split("Blocks=")[1];
            blocks = blocks.substring(0, blocks.length()-1);
            String[] blocksArray = blocks.split("\\.");
            List<Block> blocksList = new LinkedList<Block>();
            for(String blockString : blocksArray) {
                String name = blockString.substring(0,1);
                int weight = blockString.substring(1).length();
                blocksList.add(new Block(name, weight));
            }

            BlocksWorld domain = new BlocksWorld(maxColumns, blocksList);

            // extract states
            Set<Predicate> initialPredicates = readPredicateListFromString(initialStateString, domain);
            Set<Predicate> goalPredicates = readPredicateListFromString(goalStateString, domain);

            // create heavier predicates
            Set<Predicate> heavierPredicates = domain.getHeavierPredicateSet();
            // create light predicates
            Set<Predicate> lightBlockPredicates = domain.getLightBlockPredicates();

            // init initial state
            State initialState = getState(initialPredicates, heavierPredicates, lightBlockPredicates, domain);
            // init goal state
            State goalState = getState(goalPredicates, heavierPredicates, lightBlockPredicates, domain);

            // return planner
            return new Planner(initialState, goalState, domain);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private State getState(Set<Predicate> predicates, Set<Predicate> heavierPredicates, Set<Predicate> lightBlockPredicates, BlocksWorld domain) {
        State state = new State(domain);
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
        state.addPredicate(new UsedColumnsNum(usedColumns, domain));
        return state;
    }

    private Set<Predicate> readPredicateListFromString(String rawStateString, BlocksWorld domain) {
        String stateString = rawStateString.split("State=")[1];
        stateString = stateString.substring(0, stateString.length()-1);
        String[] predicatesArray = stateString.split("\\.");
        Set<Predicate> predicateSet = new HashSet<Predicate>();
        for(String predicateString : predicatesArray) {
            predicateSet.add(domain.getPredicateFromString(predicateString));
        }
        return predicateSet;
    }
}
