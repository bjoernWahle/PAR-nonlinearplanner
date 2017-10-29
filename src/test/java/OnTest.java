import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmPickUp;
import fib.par.nonlinearplanner.operators.RightArmUnstack;
import fib.par.nonlinearplanner.operators.Stack;
import fib.par.nonlinearplanner.predicates.On;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnTest {

    private void setup() {
        BlocksWorld.blocksList = new LinkedList<Block>();
        Block a = new Block("A", 2);
        Block b = new Block("B", 3);
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

        On on = new On(b, a);

        // execute
        Set<Operator> operatorSet = on.getPreOperators();

        // validate
        Set<Operator> expectedOperators = new HashSet<Operator>();

        expectedOperators.add(new Stack(b, a, Arm.rightArm));
        //expectedOperators.add(new Stack(a, b, Arm.leftArm));

        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
