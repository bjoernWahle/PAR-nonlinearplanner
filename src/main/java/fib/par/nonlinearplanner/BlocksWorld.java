package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Leave;
import fib.par.nonlinearplanner.operators.LeftArmPickUp;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmPickUp;;
import fib.par.nonlinearplanner.predicates.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BlocksWorld {

    public static int MAX_COLUMNS;

    public static void main(String[] args) {
        // TODO parse configuration

        BlocksWorld.MAX_COLUMNS = 3;

        List<Block> blocks = new LinkedList<Block>();

        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        Block c = new Block("C", 3);

        blocks.add(a);
        blocks.add(b);
        blocks.add(c);

        // create heavier predicates
        Set<Predicate> heavierPredicates = new HashSet<Predicate>();
        for(Block block1: blocks) {
            for(Block block2: blocks) {
                if(!block1.equals(block2)) {
                    if(block1.weight >= block2.weight) {
                        heavierPredicates.add(new Heavier(block1, block2));
                    }
                }
            }
        }

        // create light predicates
        Set<Predicate> lightBlockPredicates = new HashSet<Predicate>();
        for(Block block: blocks) {
            if(block.weight == 1) {
                lightBlockPredicates.add(new LightBlock(block));
            }
        }

        State initialState = new State();
        initialState.addAllPredicates(heavierPredicates);
        initialState.addAllPredicates(lightBlockPredicates);
        initialState.addPredicate(new OnTable(a));
        initialState.addPredicate(new Clear(a));
        initialState.addPredicate(new OnTable(b));
        initialState.addPredicate(new Clear(b));
        initialState.addPredicate(new OnTable(c));
        initialState.addPredicate(new Clear(c));
        initialState.addPredicate(new EmptyArm(Arm.leftArm));
        initialState.addPredicate(new EmptyArm(Arm.rightArm));

        // create used column predicates
        int usedColumns = 0;
        for(Predicate predicate : initialState.predicateSet) {
            if(predicate instanceof OnTable) {
                usedColumns++;
            }
        }
        initialState.addPredicate(new UsedColumnsNum(usedColumns));

        // Final state
        State finalState = new State();
        finalState.addAllPredicates(heavierPredicates);
        finalState.addAllPredicates(lightBlockPredicates);
        finalState.addPredicate(new EmptyArm(Arm.leftArm));
        finalState.addPredicate(new OnTable(c));
        finalState.addPredicate(new OnTable(a));
        finalState.addPredicate(new Clear(c));
        finalState.addPredicate(new Clear(a));
        finalState.addPredicate(new Clear(b));
        finalState.addPredicate(new Holding(b, Arm.rightArm));

        // calculate used column predicates
        int usedColumnsFinal = 0;
        for(Predicate predicate : finalState.predicateSet) {
            if(predicate instanceof OnTable) {
                usedColumnsFinal++;
            }
        }
        finalState.addPredicate(new UsedColumnsNum(usedColumnsFinal));

        LeftArmPickUp op1 = new LeftArmPickUp(a, usedColumns);
        RightArmPickUp op2 = new RightArmPickUp(b, usedColumns-1);
        Leave op3 = new Leave(a, Arm.leftArm, usedColumns-2);

        LinkedList<Operator> operatorList = new LinkedList<Operator>();
        operatorList.add(op1);
        operatorList.add(op2);
        operatorList.add(op3);

        Planner myPlanner = new Planner(initialState, finalState);
        myPlanner.executePlan(new Plan(operatorList));

        System.out.println("The planner terminated in the final state: "+ myPlanner.isInFinalState());
        System.out.println("The final state should be: " +myPlanner.finalState.simpleRepresentation());

    }
}
