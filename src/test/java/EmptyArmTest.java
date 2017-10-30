import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.operators.*;
import fib.par.nonlinearplanner.domain.predicates.EmptyArm;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmptyArmTest {

    private BlocksWorld setup() {
        List<Block> blocksList = new LinkedList<Block>();
        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        blocksList.add(a);
        blocksList.add(b);
        return new BlocksWorld(3, blocksList);
    }

    @Test
    public void getPreOperatorsShouldReturnCorrectPreOperators() {
        // setup
        BlocksWorld bw = setup();
        Arm rightArm = Arm.rightArm;
        Arm leftArm = Arm.leftArm;
        Block a = bw.getBlockFromName("A");
        Block b = bw.getBlockFromName("B");

        EmptyArm emptyRightArm = new EmptyArm(rightArm, bw);

        // execute
        //Set<Operator> operatorSet = emptyLeftArm.getPreOperators();
        Set<Operator> operatorSet = emptyRightArm.getPreOperators();

        // validate right arm
        Set<Operator> expectedOperators = new HashSet<Operator>();
//        expectedOperators.add(new Leave(b, leftArm, 0));
//        expectedOperators.add(new Leave(b, leftArm, 1));
//        expectedOperators.add(new Leave(b, leftArm, 2));
//        expectedOperators.add(new Stack(b, a, leftArm));

        // validate left arm
        expectedOperators.add(new Leave(a, rightArm, 0, bw));
        expectedOperators.add(new Leave(a, rightArm, 1, bw));
        expectedOperators.add(new Leave(a, rightArm, 2, bw));
        expectedOperators.add(new Leave(b, rightArm, 0, bw));
        expectedOperators.add(new Leave(b, rightArm, 1, bw));
        expectedOperators.add(new Leave(b, rightArm, 2, bw));
        expectedOperators.add(new Stack(b, a, rightArm, bw));
        expectedOperators.add(new Stack(a, b, rightArm, bw));

        System.out.println("Expected Operators: " + expectedOperators);
        System.out.println("Operator Set: " + operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}