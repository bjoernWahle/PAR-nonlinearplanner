package fib.par.nonlinearplanner.util;

import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.operators.Operator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StateOperatorTree {
    private Node root;

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

    public StateOperatorTree(State state, Operator operator) {
        root = new Node(state, operator);
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
        private State state;
        private Operator operator;
        private List<Node> children;
        private Node parent;

        public boolean isRoot() {
            return parent == null;
        }

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

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public Node getParent() {
            return parent;
        }
    }
}
