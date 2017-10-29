import static org.junit.jupiter.api.Assertions.assertEquals;

import fib.par.nonlinearplanner.Block;
import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.operators.RightArmPickUp;
import fib.par.nonlinearplanner.util.StateOperatorTree;
import org.junit.jupiter.api.Test;

class StateOperatorTreeTest {

    @Test
    void getNodeCountShouldReturn3WhenCalledOnTreeWith2Children() {
        // prepare
        State state = new State();
        RightArmPickUp op = new RightArmPickUp(new Block("A", 2), 1);
        StateOperatorTree tree = new StateOperatorTree(state);
        StateOperatorTree.Node root = tree.getRoot();
        root.addChild(new StateOperatorTree.Node(state,op, StateOperatorTree.Node.NodeStatus.VALID));
        root.addChild(new StateOperatorTree.Node(state,op, StateOperatorTree.Node.NodeStatus.VALID));

        // execute & validate
        assertEquals(3, tree.getNodesCount());
    }

}

