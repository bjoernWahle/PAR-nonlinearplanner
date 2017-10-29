import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.*;
import fib.par.nonlinearplanner.predicates.Holding;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HoldingTest {
    private void setup() {
        BlocksWorld.blocksList = new LinkedList<Block>();
        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        BlocksWorld.blocksList.add(a);
        BlocksWorld.blocksList.add(b);
        BlocksWorld.MAX_COLUMNS = 3;
    }

    @Test
    public void getPreOperatorsShouldReturnCorrectPreOperators() {
        // setup
        setup();
        Block a = BlocksWorld.getBlockFromName("A");
        Block b = BlocksWorld.getBlockFromName("B");
        Arm arm = Arm.rightArm;

        Holding holding = new Holding(b, arm);

        // execute
        Set<Operator> operatorSet = holding.getPreOperators();

        // validate
        Set<Operator> expectedOperators = new HashSet<Operator>();

        // Left Arm
        //expectedOperators.add(new LeftArmUnstack(a, b));
        //expectedOperators.add(new LeftArmPickUp(a, 0));
        //expectedOperators.add(new LeftArmPickUp(a, 1));
        //expectedOperators.add(new LeftArmPickUp(a, 2));

        // Right Arm
        expectedOperators.add(new RightArmUnstack(b, a));
        expectedOperators.add(new RightArmPickUp(b, 1));
        expectedOperators.add(new RightArmPickUp(b, 2));
        expectedOperators.add(new RightArmPickUp(b, 3));



        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
