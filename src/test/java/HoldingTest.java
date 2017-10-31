import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.operators.*;
import fib.par.nonlinearplanner.domain.predicates.Holding;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HoldingTest {

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
        Block a = bw.getBlockFromName("A");
        Block b = bw.getBlockFromName("B");
        Arm arm = Arm.rightArm;

        Holding holding = new Holding(b, arm, bw);

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
        expectedOperators.add(new RightArmUnstack(b, a, bw));
        expectedOperators.add(new RightArmPickUp(b, 1, bw));
        expectedOperators.add(new RightArmPickUp(b, 2, bw));
        expectedOperators.add(new RightArmPickUp(b, 3, bw));



        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
