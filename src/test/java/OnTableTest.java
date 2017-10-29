import fib.par.nonlinearplanner.Arm;
import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.Leave;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.Stack;
import fib.par.nonlinearplanner.predicates.On;
import fib.par.nonlinearplanner.predicates.OnTable;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnTableTest {

    private void setup() {
        BlocksWorld.blocksList = new LinkedList<Block>();
        Block a = new Block("A", 1);
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

        OnTable onTable = new OnTable(a);

        // execute
        Set<Operator> operatorSet = onTable.getPreOperators();

        // validate
        Set<Operator> expectedOperators = new HashSet<Operator>();

        expectedOperators.add(new Leave(a, Arm.rightArm, 2));
        expectedOperators.add(new Leave(a, Arm.rightArm, 1));
        expectedOperators.add(new Leave(a, Arm.rightArm, 0));
        expectedOperators.add(new Leave(a, Arm.leftArm, 2));
        expectedOperators.add(new Leave(a, Arm.leftArm, 1));
        expectedOperators.add(new Leave(a, Arm.leftArm, 0));

        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
