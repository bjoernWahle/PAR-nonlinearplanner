public class State {

    enum Blocks {
        A(0),B(0),C(0);

        int value;

        Blocks(int value) {
            this.value = value;
        }
    }

    public boolean[] onTable;
    boolean[][] on;

    public State() {
        onTable[Blocks.A.value] = true;
        on[Blocks.A.value][Blocks.B.value] =true;

        if(this.onTable[Blocks.A.value]) {

        }
    }

    public boolean isValidState() {
        if(onTable[Blocks.A.value] && on[Blocks.A.value][Blocks.B.value]) {
            return false;
        } else{
            return true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof State) {
            State otherState = (State) obj;
            if(this.onTable == otherState.onTable) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}
