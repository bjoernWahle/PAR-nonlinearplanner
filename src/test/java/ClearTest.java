import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.BlocksWorld;
import fib.par.nonlinearplanner.operators.LeftArmUnstack;
import fib.par.nonlinearplanner.operators.Operator;
import fib.par.nonlinearplanner.operators.RightArmUnstack;
import fib.par.nonlinearplanner.predicates.Clear;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClearTest {

    private void setup() {
        BlocksWorld.blocksList = new LinkedList<Block>();
        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        BlocksWorld.blocksList.add(a);
        BlocksWorld.blocksList.add(b);
        BlocksWorld.MAX_COLUMNS = 3;
    }

    @Test
    void getPreOperatorsShouldReturnCorrectPreOperators() {
        // setup
        setup();
        Block a = BlocksWorld.getBlockFromName("A");
        Block b = BlocksWorld.getBlockFromName("B");

        Clear clear = new Clear(a);

        // execute
        Set<Operator> operatorSet = clear.getPreOperators();

        // validate
        Set<Operator> expectedOperators = new HashSet<Operator>();
        expectedOperators.add(new RightArmUnstack(b, a));
        System.out.println("Expected Operators: "+expectedOperators);
        System.out.println("Operator Set: "+operatorSet);
        assertEquals(operatorSet, expectedOperators);
    }
}
