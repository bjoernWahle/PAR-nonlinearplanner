package fib.par.nonlinearplanner.util;

import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.Operator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StateOperatorTree {
    private final Node root;

    public int getNodesCount() {
        return _getNodesCount(root);
    }

    private int _getNodesCount(Node node) {
        int count = 1;
        if(node.children.size()>0) {
            for(Node cNode : node.children) {
                count += _getNodesCount(cNode);
            }
        }
        return count;
    }

    public Node getRoot() {
        return root;
    }

    public StateOperatorTree(State state) {
        root = new Node(state);
    }

    public List<Node> getNodesInLevel(int i) {
        List<Node> nodeList = new LinkedList<Node>();
        if(i == 0) {
            nodeList.add(root);
        } else {
            List<Node> parentNodeList = getNodesInLevel(i -1);
            for(Node node : parentNodeList) {
                nodeList.addAll(node.children);
            }
        }
        return nodeList;
    }

    public static class Node {
        private final State state;
        private Operator operator;
        private final List<Node> children;
        private Node parent;

        public boolean isRoot() {
            return parent == null;
        }

        public Node(State state, Operator operator) {
            this.state = state;
            this.operator = operator;
            this.children = new ArrayList<Node>();
        }

        Node(State state) {
            this.state = state;
            this.children = new ArrayList<Node>();
        }

        public void addChild(Node child) {
            child.parent = this;
            this.children.add(child);
        }

        @Override
        public String toString() {
            return "Node(S," + operator+")";
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

        public Node getParent() {
            return parent;
        }
    }
}
