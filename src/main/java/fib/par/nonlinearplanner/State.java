package fib.par.nonlinearplanner;

import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.predicates.*;

import java.util.*;
import java.util.stream.Collectors;

public class State {
    final Set<Predicate> predicateSet;

    public State() {
        predicateSet = new HashSet<Predicate>();
    }

    private State(Set<Predicate> predicateSet) {
        this.predicateSet = predicateSet;
    }

    private boolean meetsPrecondition(Predicate precondition) {
        if(precondition instanceof Negation) {
            return !predicateSet.contains(((Negation) precondition).predicate);
        } else {
            return predicateSet.contains(precondition);
        }
    }

    public boolean meetsPreconditions(Set<Predicate> preconditions) {
        for(Predicate precondition: preconditions) {
            if(!meetsPrecondition(precondition)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("State(Predicates: ");
        for(Predicate predicate : predicateSet) {
            str.append(predicate).append(",");
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        str.append(")");
        return str.toString();
    }

    public String predicateListString() {
        return String.join(",", predicateSet.stream().filter(p -> !(p instanceof Heavier) && !(p instanceof LightBlock) ).map(Predicate::toString).collect(Collectors.toList()));
    }

    public void addPredicate(Predicate predicate) {
        predicateSet.add(predicate);
    }

    public void addAllPredicates(Set<Predicate> predicates) {
        predicateSet.addAll(predicates);
    }

    public State applyOperator(Operator operator) {
        Set<Predicate> newPredicateSet = new HashSet<Predicate>();
        newPredicateSet.addAll(predicateSet);
        State newState = new State(newPredicateSet);
        newState.predicateSet.removeAll(operator.deleteList);
        newState.predicateSet.addAll(operator.addList);
        return newState;
    }

    public State applyOperatorReverse(Operator operator) {
        Set<Predicate> newPredicateSet = new HashSet<Predicate>(predicateSet);
        State stateBefore = new State(newPredicateSet);
        stateBefore.predicateSet.addAll(operator.deleteList);
        stateBefore.predicateSet.removeAll(operator.addList);
        return stateBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return predicateSet != null ? predicateSet.equals(state.predicateSet) : state.predicateSet == null;
    }

    @Override
    public int hashCode() {
        return predicateSet != null ? predicateSet.hashCode() : 0;
    }

    String simpleRepresentation() {
        StringBuilder str = new StringBuilder("State(Predicates: ");
        for(Predicate predicate : predicateSet) {
            if(!(predicate instanceof Heavier || predicate instanceof LightBlock)) {
                str.append(predicate).append(",");
            }
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        str.append(")");
        return str.toString();
    }

    Set<Operator> getPossiblePreOperators() {
        Set<Operator> operators = new HashSet<Operator>();
        for(Predicate predicate : predicateSet) {
            Set<Operator> preOps = predicate.getPreOperators();
            operators.addAll(preOps);
        }
        return operators;
    }

    boolean isValid() {
        // check used-columns-num
        if(!usedColumnsNumValid()) {
            return false;
        }
        // check empty-arm and holding
        if(!armsPredicatesValid()) {
            return false;
        }
        if(!holdingPredicatesValid()) {
            return false;
        }
        // check onTable valid
        if(!onTablePredicatesValid()) {
            return false;
        }
        return onPredicatesValid();
    }

    private boolean onPredicatesValid() {
        Set<On> onPredicates = predicateSet.stream().filter(p -> p instanceof On)
                .map(p -> (On)p).collect(Collectors.toSet());
        // check that no block has two blocks on it
        List<Block> lowerBlocks = onPredicates.stream().map(On::getLowerBlock).collect(Collectors.toList());
        List<Block> upperBlocks = onPredicates.stream().map(On::getUpperBlock).collect(Collectors.toList());
        if(new HashSet<Block>(lowerBlocks).size() > lowerBlocks.size()) {
            return false;
        }
        if(new HashSet<Block>(upperBlocks).size() > upperBlocks.size()) {
            return false;
        }
        return true;
    }

    private boolean usedColumnsNumValid() {
        List<UsedColumnsNum> usedColumnsNumSet = predicateSet.stream().filter(p -> p instanceof UsedColumnsNum)
                .map(p -> (UsedColumnsNum)p).collect(Collectors.toList());
        return usedColumnsNumSet.size() <= 1 && usedColumnsNumSet.size() != 0;
    }

    private boolean onTablePredicatesValid() {
        // if a block is on the table, it can be on another block or being held
        Set<Block> onTableBlocks = predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toSet());
        Set<Block> notOnTableBlocks = new HashSet<Block>();
        notOnTableBlocks.addAll(predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toSet()));
        notOnTableBlocks.addAll(predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding)p).getBlock()).collect(Collectors.toSet()));
        onTableBlocks.retainAll(notOnTableBlocks);
        return onTableBlocks.size() == 0;
    }

    private boolean holdingPredicatesValid() {
        Set<Holding> holdings = predicateSet.stream().filter(p -> p instanceof Holding).map(p -> (Holding) p).collect(Collectors.toSet());
        Set<Block> onTableBlocks = predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toSet());
        Set<Block> lowerOnBlocks = predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getLowerBlock()).collect(Collectors.toSet());
        Set<Block> upperOnBlocks = predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toSet());
        for(Holding holding : holdings) {
            Block block = holding.getBlock();
            if(holding.getArm().equals(Arm.leftArm)) {
                if(block.weight > 1) {
                    return false;
                }
            }
            if(onTableBlocks.contains(block) ||lowerOnBlocks.contains(block) || upperOnBlocks.contains(block)) {
                return false;
            }
        }
        return true;
    }

    private boolean armsPredicatesValid() {
        Set<Arm> emptyArms = predicateSet.stream().filter(p -> p instanceof EmptyArm).map(p -> ((EmptyArm) p).getArm()).collect(Collectors.toSet());
        Set<Arm> holdingArms = predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding) p).getArm()).collect(Collectors.toSet());
        if(emptyArms.size() + holdingArms.size() > 2) {
            return false;
        }
        // emptyArms contains the intersection of the arms after retainAll
        emptyArms.retainAll(holdingArms);
        // if intersection has elements it means that there is at least one arm that is also holding a block
        return emptyArms.size() == 0;
    }

    public void printState() {
        // print arms

        Block leftHeldBlock = getBlockHeldByLeftArm();
        String l = leftHeldBlock == null ? "_" : leftHeldBlock.simpleRepresentation();
        Block rightHeldBlock = getBlockHeldByRightArm();
        String r = rightHeldBlock == null? "_" : rightHeldBlock.simpleRepresentation();
        System.out.println("|---L---|    |---R---|");
        System.out.println("    "+l+"            "+r);
        // print 2 empty lines
        System.out.println("");
        System.out.println("");

        // get on-tables
        List<Block> onTableBlocks = getOnTableBlocks();
        // create array for the columns
        List<List<Block>> columns = new LinkedList<List<Block>>();
        for(Block onTableBlock : onTableBlocks) {
            columns.add(getColumnOn(onTableBlock));
        }
        int maxHeight = columns.stream().max(Comparator.comparingInt(List::size)).get().size();
        String gap = "    ";
        for(int i = maxHeight-1; i >= 0; i--) {
            StringBuilder levelString = new StringBuilder();
            for(List<Block> blocks : columns) {
                if(blocks.size() > i) {
                    levelString.append(blocks.get(i).simpleRepresentation());
                } else {
                    levelString.append(" ");
                }
                levelString.append(gap);
            }
            System.out.println(levelString);
        }
    }

    private List<Block> getColumnOn(Block onTableBlock) {
        List<Block> column = new LinkedList<Block>();
        column.add(onTableBlock);
        // search for On(x, onTableBlock)
        Block currentBlock = onTableBlock;
        Block nextBlock;
        do {
            nextBlock = findBlockOn(currentBlock);
            if(nextBlock != null) {
                currentBlock = nextBlock;
                column.add(nextBlock);
            }
        } while (nextBlock != null);
        return column;
    }

    private Block findBlockOn(Block onTableBlock) {
        List<Block> upperOnBlocks = predicateSet.stream().filter(p -> p instanceof On && ((On) p).getLowerBlock() == onTableBlock).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toList());
        if(upperOnBlocks.size() == 0) {
            return null;
        } else {
            return upperOnBlocks.get(0);
        }
    }

    private Block getBlockHeldByLeftArm() {
        List<Block> blocks = predicateSet.stream().filter(p -> (p instanceof Holding && (((Holding) p).getArm().equals(Arm.leftArm)))).map(h -> ((Holding) h).getBlock()).collect(Collectors.toList());
        if(blocks.size() == 0) {
            return null;
        } else {
            return blocks.get(0);
        }
    }

    private Block getBlockHeldByRightArm() {
        List<Block> blocks = predicateSet.stream().filter(p -> (p instanceof Holding && (((Holding) p).getArm().equals(Arm.rightArm)))).map(h -> ((Holding) h).getBlock()).collect(Collectors.toList());
        if(blocks.size() == 0) {
            return null;
        } else {
            return blocks.get(0);
        }
    }

    private List<Block> getOnTableBlocks() {
        return predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toList());
    }
}
