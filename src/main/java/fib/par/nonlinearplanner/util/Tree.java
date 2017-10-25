package fib.par.nonlinearplanner.util;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Node<T> root;

    public Node<T> getRoot() {
        return root;
    }

    public Tree(T rootData) {
        root = new Node<T>(rootData);
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;

        public Node(T data) {
            this.data = data;
            this.children = new ArrayList<Node<T>>();
        }

        public void addChild(Node<T> child) {
            this.children.add(child);
        }

        public T getData() {
            return data;
        }
    }
}
