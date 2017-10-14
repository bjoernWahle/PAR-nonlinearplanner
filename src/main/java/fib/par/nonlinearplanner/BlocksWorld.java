package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.LeftArmPickUp;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmPickUp;;
import fib.par.nonlinearplanner.predicates.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BlocksWorld {

    public static void main(String[] args) {
        // TODO parse configuration

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

        // Final state
        State finalState = new State();
        finalState.addAllPredicates(heavierPredicates);
        finalState.addAllPredicates(lightBlockPredicates);
        finalState.addPredicate(new OnTable(c));
        finalState.addPredicate(new Clear(c));
        finalState.addPredicate(new Holding(a, Arm.leftArm));
        finalState.addPredicate(new Holding(b, Arm.rightArm));

        LeftArmPickUp op1 = new LeftArmPickUp(a);
        RightArmPickUp op2 = new RightArmPickUp(b);

        LinkedList<Operator> operatorList = new LinkedList<Operator>();
        operatorList.add(op1);
        operatorList.add(op2);

        Planner myPlanner = new Planner(initialState, finalState);
        myPlanner.executePlan(new Plan(operatorList));

        System.out.println(myPlanner.isInFinalState());

    }
}
