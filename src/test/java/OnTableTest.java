import fib.par.nonlinearplanner.domain.Arm;
import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.operators.Leave;
import fib.par.nonlinearplanner.Operator;
import fib.par.nonlinearplanner.domain.predicates.OnTable;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnTableTest {

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

        OnTable onTable = new OnTable(a, bw);

        // execute
        Set<Operator> operatorSet = onTable.getPreOperators();

        // validate
        Set<Operator> expectedOperators = new HashSet<Operator>();

        expectedOperators.add(new Leave(a, Arm.rightArm, 2, bw));
        expectedOperators.add(new Leave(a, Arm.rightArm, 1, bw));
        expectedOperators.add(new Leave(a, Arm.rightArm, 0, bw));
        expectedOperators.add(new Leave(a, Arm.leftArm, 2, bw));
        expectedOperators.add(new Leave(a, Arm.leftArm, 1, bw));
        expectedOperators.add(new Leave(a, Arm.leftArm, 0, bw));

        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
