package fib.par.nonlinearplanner.util;

import fib.par.nonlinearplanner.State;
import fib.par.nonlinearplanner.operators.Operator;

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

    public List<Node> getInvalidNodes() {
        return _getInvalidNodes(root);
    }

    private List<Node> _getInvalidNodes(Node node) {
        List<Node> nodeList = new LinkedList<Node>();
        if(!node.isValid()) {
            nodeList.add(node);
        } else {
            for(Node cNode: node.children) {
                nodeList.addAll(_getInvalidNodes(cNode));
            }
        }
        return nodeList;
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
        private final NodeStatus status;

        public boolean isValid() {
            return status == NodeStatus.VALID;
        }

        public NodeStatus getStatus() {
            return status;
        }

        public enum NodeStatus {
            VALID("Node is valid"),
            REPEATED_STATE("Repeated state"),
            INVALID_STATE("State is not valid"),
            OP_PREC_NOT_MET("Operator preconditions are not fulfilled.");

            final String reason;

            NodeStatus(String reason) {
                this.reason = reason;
            }

            public String getReason() {
                return reason;
            }
        }

        public boolean isRoot() {
            return parent == null;
        }

        public Node(State state, Operator operator, NodeStatus status) {
            this.state = state;
            this.status = status;
            this.operator = operator;
            this.children = new ArrayList<Node>();
        }

        Node(State state) {
            this.state = state;
            this.status = NodeStatus.VALID;
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
