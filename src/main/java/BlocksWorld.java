import java.util.LinkedList;
import java.util.List;

public class BlocksWorld {
    static ObjState currentState;
    static List<ObjState> pastStates;

    public static void main(String[] args) {
        // TODO parse configuration

        List<Block> blocks = new LinkedList<Block>();

        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        Block c = new Block("C", 3);


        blocks.add(a);
        blocks.add(b);
        blocks.add(c);



        ObjState initialState = new ObjState();
        initialState.addPredicate(new OnTable(a));
        initialState.addPredicate(new Clear(a));
        initialState.addPredicate(new LightBlock(a));
        initialState.addPredicate(new EmptyArm(Arm.leftArm));



        //initialState.addPredicate(new On(b,a));

        LeftArmPickUp op1 = new LeftArmPickUp(initialState, a);

        System.out.println(op1.isExecutable());

        ObjState state1 = op1.execute();

        System.out.println(initialState);
        System.out.println(state1);

    }
}
