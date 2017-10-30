import static org.junit.jupiter.api.Assertions.assertEquals;

import fib.par.nonlinearplanner.domain.Block;
import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.domain.BlocksWorld;
import fib.par.nonlinearplanner.domain.operators.RightArmPickUp;
import fib.par.nonlinearplanner.util.StateOperatorTree;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class StateOperatorTreeTest {

    private BlocksWorld setup() {
        List<Block> blocksList = new LinkedList<Block>();
        Block a = new Block("A", 1);
        blocksList.add(a);
        return new BlocksWorld(3, blocksList);
    }

    @Test
    void getNodeCountShouldReturn3WhenCalledOnTreeWith2Children() {
        BlocksWorld bw = setup();
        Block a = bw.getBlockFromName("A");
        // prepare
        State state = new State(bw);
        RightArmPickUp op = new RightArmPickUp(a, 1, bw);
        StateOperatorTree tree = new StateOperatorTree(state);
        StateOperatorTree.Node root = tree.getRoot();
        root.addChild(new StateOperatorTree.Node(state,op));
        root.addChild(new StateOperatorTree.Node(state,op));

        // execute & validate
        assertEquals(3, tree.getNodesCount());
    }

}

