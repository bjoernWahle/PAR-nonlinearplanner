import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.*;
import fib.par.nonlinearplanner.predicates.Clear;
import fib.par.nonlinearplanner.predicates.EmptyArm;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmptyArmTest {

    private void setup() {
        BlocksWorld.blocksList = new LinkedList<Block>();
        Block a = new Block("A", 2);
        Block b = new Block("B", 2);
        BlocksWorld.blocksList.add(a);
        BlocksWorld.blocksList.add(b);
        BlocksWorld.MAX_COLUMNS = 3;
    }

    @Test
    public void getPreOperatorsShouldReturnCorrectPreOperators() {
        // setup
        setup();
        Arm rightArm = Arm.rightArm;
        Arm leftArm = Arm.leftArm;
        Block a = BlocksWorld.getBlockFromName("A");
        Block b = BlocksWorld.getBlockFromName("B");

        //EmptyArm emptyLeftArm = new EmptyArm(leftArm);
        EmptyArm emptyRightArm = new EmptyArm(rightArm);

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
        expectedOperators.add(new Leave(a, rightArm, 0));
        expectedOperators.add(new Leave(a, rightArm, 1));
        expectedOperators.add(new Leave(a, rightArm, 2));
        expectedOperators.add(new Leave(b, rightArm, 0));
        expectedOperators.add(new Leave(b, rightArm, 1));
        expectedOperators.add(new Leave(b, rightArm, 2));
        expectedOperators.add(new Stack(b, a, rightArm));
        expectedOperators.add(new Stack(a, b, rightArm));

        System.out.println("Expected Operators: " + expectedOperators);
        System.out.println("Operator Set: " + operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}