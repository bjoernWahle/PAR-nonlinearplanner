import java.util.LinkedList;
import java.util.List;

public class ObjState {
    List<Predicate> predicateList;

    ObjState() {
        predicateList = new LinkedList<Predicate>();
    }

    public boolean meetsPrecondition(Predicate precondition) {
        if(predicateList.contains(precondition)) {
            return true;
        } else {
            return false;
        }
        /*for(Predicate predicate:predicateList) {
            if(predicate.equals(precondition)){
                return true;
            };
        }
        return false;*/
    }

    public boolean meetsPreconditions(List<Predicate> preconditions) {
        for(Predicate precondition: preconditions) {
            if(!meetsPrecondition(precondition)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        str += "State: ";
        str += "(Predicates: ";
        for(Predicate predicate : predicateList) {
            str += predicate + ",";
        }
        str = str.substring(0, str.length()-1);
        str += ")";
        return str;
    }

    public void addPredicate(Predicate predicate) {
        predicateList.add(predicate);
    }

    @Override
    public ObjState clone() {
        ObjState newState = new ObjState();
        newState.predicateList = predicateList;
        return newState;
    }
}
