package fib.par.nonlinearplanner.domain;

import fib.par.nonlinearplanner.Domain;
import fib.par.nonlinearplanner.Predicate;
import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.domain.predicates.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class BlocksWorld extends Domain {

    public final int maxColumns;

    public final List<Block> blocksList;

    public BlocksWorld(int maxColumns, List<Block> blocksList) {
        this.maxColumns = maxColumns;
        this.blocksList = blocksList;
    }

    Set<Predicate> getLightBlockPredicates() {
        Set<Predicate> lightBlockPredicates = new HashSet<Predicate>();
        for(Block block: blocksList) {
            if(block.weight == 1) {
                lightBlockPredicates.add(new LightBlock(block, this));
            }
        }
        return lightBlockPredicates;
    }

    Set<Predicate> getHeavierPredicateSet() {
        Set<Predicate> heavierPredicates = new HashSet<Predicate>();
        for(Block block1: blocksList) {
            for(Block block2: blocksList) {
                if(!block1.equals(block2)) {
                    if(block1.weight >= block2.weight) {
                        heavierPredicates.add(new Heavier(block1, block2, this));
                    }
                }
            }
        }
        return heavierPredicates;
    }

    public Block getBlockFromName(String name) {
        for(Block block: blocksList) {
            if(Objects.equals(block.name, name)) {
                return block;
            }
        }
        throw new IllegalArgumentException("No block with name " + name+ " found.");
    }

    public List<Block> getBlocksList() {
        return blocksList;
    }

    public static void writeToOutputFile(String output, String fileName) {
        fileName = "output/"+fileName;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(fileName)));
            writer.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer
                writer.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public Predicate getPredicateFromString(String string) {
        if(string.startsWith("CLEAR(")) {
            return Clear.fromString(string, this);
        } else if(string.startsWith("EMPTY-ARM(")) {
            return EmptyArm.fromString(string, this);
        } else if (string.startsWith("ON-TABLE(")) {
            return OnTable.fromString(string, this);
        } else if (string.startsWith("ON(")){
            return On.fromString(string, this);
        } else if (string.startsWith("HOLDING(")) {
            return Holding.fromString(string, this);
        }
        return null;
    }

    @Override
    public boolean validateStateInDomain(State state) {
        // check used-columns-num
        if(!usedColumnsNumValid(state)) {
            return false;
        }
        // check empty-arm and holding
        if(!armsPredicatesValid(state)) {
            return false;
        }
        if(!holdingPredicatesValid(state)) {
            return false;
        }
        // check onTable valid
        if(!onTablePredicatesValid(state)) {
            return false;
        }
        return onPredicatesValid(state);
    }

    private boolean onPredicatesValid(State state) {
        Set<On> onPredicates = state.predicateSet.stream().filter(p -> p instanceof On)
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

    private boolean usedColumnsNumValid(State state) {
        List<UsedColumnsNum> usedColumnsNumSet = state.predicateSet.stream().filter(p -> p instanceof UsedColumnsNum)
                .map(p -> (UsedColumnsNum)p).collect(Collectors.toList());
        return usedColumnsNumSet.size() <= 1 && usedColumnsNumSet.size() != 0;
    }

    private boolean onTablePredicatesValid(State state) {
        // if a block is on the table, it can be on another block or being held
        Set<Block> onTableBlocks = state.predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toSet());
        Set<Block> notOnTableBlocks = new HashSet<Block>();
        notOnTableBlocks.addAll(state.predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toSet()));
        notOnTableBlocks.addAll(state.predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding)p).getBlock()).collect(Collectors.toSet()));
        onTableBlocks.retainAll(notOnTableBlocks);
        return onTableBlocks.size() == 0;
    }

    private boolean holdingPredicatesValid(State state) {
        Set<Holding> holdings = state.predicateSet.stream().filter(p -> p instanceof Holding).map(p -> (Holding) p).collect(Collectors.toSet());
        Set<Block> onTableBlocks = state.predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toSet());
        Set<Block> lowerOnBlocks = state.predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getLowerBlock()).collect(Collectors.toSet());
        Set<Block> upperOnBlocks = state.predicateSet.stream().filter(p -> p instanceof On).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toSet());
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

    private boolean armsPredicatesValid(State state) {
        Set<Arm> emptyArms = state.predicateSet.stream().filter(p -> p instanceof EmptyArm).map(p -> ((EmptyArm) p).getArm()).collect(Collectors.toSet());
        Set<Arm> holdingArms = state.predicateSet.stream().filter(p -> p instanceof Holding).map(p -> ((Holding) p).getArm()).collect(Collectors.toSet());
        if(emptyArms.size() + holdingArms.size() > 2) {
            return false;
        }
        // emptyArms contains the intersection of the arms after retainAll
        emptyArms.retainAll(holdingArms);
        // if intersection has elements it means that there is at least one arm that is also holding a block
        return emptyArms.size() == 0;
    }

    public void printState(State state) {
        // print arms

        Block leftHeldBlock = getBlockHeldByLeftArm(state);
        String l = leftHeldBlock == null ? "_" : leftHeldBlock.simpleRepresentation();
        Block rightHeldBlock = getBlockHeldByRightArm(state);
        String r = rightHeldBlock == null? "_" : rightHeldBlock.simpleRepresentation();
        System.out.println("|---L---|    |---R---|");
        System.out.println("    "+l+"            "+r);
        // print 2 empty lines
        System.out.println("");
        System.out.println("");

        // get on-tables
        List<Block> onTableBlocks = getOnTableBlocks(state);
        // create array for the columns
        List<List<Block>> columns = new LinkedList<List<Block>>();
        for(Block onTableBlock : onTableBlocks) {
            columns.add(getColumnOn(onTableBlock, state));
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

    private List<Block> getColumnOn(Block onTableBlock, State state) {
        List<Block> column = new LinkedList<Block>();
        column.add(onTableBlock);
        // search for On(x, onTableBlock)
        Block currentBlock = onTableBlock;
        Block nextBlock;
        do {
            nextBlock = findBlockOn(currentBlock, state);
            if(nextBlock != null) {
                currentBlock = nextBlock;
                column.add(nextBlock);
            }
        } while (nextBlock != null);
        return column;
    }

    private Block findBlockOn(Block onTableBlock, State state) {
        List<Block> upperOnBlocks = state.predicateSet.stream().filter(p -> p instanceof On && ((On) p).getLowerBlock() == onTableBlock).map(p -> ((On)p).getUpperBlock()).collect(Collectors.toList());
        if(upperOnBlocks.size() == 0) {
            return null;
        } else {
            return upperOnBlocks.get(0);
        }
    }

    private Block getBlockHeldByLeftArm(State state) {
        List<Block> blocks = state.predicateSet.stream().filter(p -> (p instanceof Holding && (((Holding) p).getArm().equals(Arm.leftArm)))).map(h -> ((Holding) h).getBlock()).collect(Collectors.toList());
        if(blocks.size() == 0) {
            return null;
        } else {
            return blocks.get(0);
        }
    }

    private Block getBlockHeldByRightArm(State state) {
        List<Block> blocks = state.predicateSet.stream().filter(p -> (p instanceof Holding && (((Holding) p).getArm().equals(Arm.rightArm)))).map(h -> ((Holding) h).getBlock()).collect(Collectors.toList());
        if(blocks.size() == 0) {
            return null;
        } else {
            return blocks.get(0);
        }
    }

    private List<Block> getOnTableBlocks(State state) {
        return state.predicateSet.stream().filter(p -> p instanceof OnTable).map(p -> ((OnTable)p).getBlock()).collect(Collectors.toList());
    }
}
