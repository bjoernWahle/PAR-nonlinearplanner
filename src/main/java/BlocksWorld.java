import java.util.List;

public class BlocksWorld {
    static ObjState currentState;
    static List<ObjState> pastStates;

    public static void main(String[] args) {
        // TODO parse configuration

        Block a = new Block("A", 1);
        Block b = new Block("B", 2);
        Block c = new Block("C", 3);

        ObjState initialState = new ObjState();
        initialState.addPredicate(new OnTable(a));
        initialState.addPredicate(new Clear(a));
        //initialState.addPredicate(new On(b,a));

        PickUp op1 = new PickUp(initialState, a);

        System.out.println(op1.isExecutable());

        System.out.println(initialState);

    }
}
