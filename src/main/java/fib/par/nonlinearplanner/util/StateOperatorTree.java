package fib.par.nonlinearplanner.util;

import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.operators.Operator;

import java.util.ArrayList;
import java.util.List;

public class StateOperatorTree {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public StateOperatorTree(State state, Operator operator) {
        root = new Node(state, operator);
    }
    public StateOperatorTree(State state) {
        root = new Node(state);
    }

    public static class Node {
        private State state;
        private Operator operator;
        private List<Node> children;

        public Node(State state, Operator operator) {
            this.state = state;
            this.operator = operator;
            this.children = new ArrayList<Node>();
        }

        public Node(State state) {
            this.state = state;
            this.children = new ArrayList<Node>();
        }

        public void addChild(Node child) {
            this.children.add(child);
        }

        public State getState() {
            return state;
        }

        public Operator getOperator() {
            return operator;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }
    }
}
