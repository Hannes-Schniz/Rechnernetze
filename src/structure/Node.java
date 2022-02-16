package src.structure;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final IP tag;
    private int gradiant;
    private boolean isRoot;
    private List<Node> connections;
    private Node upperNode;
    private int layer;

    public Node(IP tag, int gradiant, boolean isRoot, List<Node> connections, Node upperNode) {
        this.tag = tag;
        this.gradiant = gradiant;
        this.isRoot = isRoot;
        this.connections = connections;
        this.upperNode = upperNode;
        if (isRoot) {
            this.layer = 0;
        }
        else {
            this.layer = upperNode.getLayer() + 1;
        }
    }

    public Node(IP tag) {
        this.tag = tag;
        this.gradiant = 0;
        this.isRoot = false;
        this.connections = new ArrayList<>();
        this.upperNode = null;
        this.layer = 0;
    }

    public IP getTag() {
        return tag;
    }

    public int getGradiant() {
        return gradiant;
    }

    public void setGradiant(int gradiant) {
        this.gradiant = gradiant;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public List<Node> getConnections() {
        return connections;
    }

    public void setConnections(List<Node> connections) {
        this.connections = connections;
    }

    public void initNewConnections(List<IP> connections) {
        for (IP connection : connections) {
            this.connections.add(new Node(connection));
        }
    }

    public Node getUpperNode() {
        return upperNode;
    }

    public void setUpperNode(Node upperNode) {
        this.upperNode = upperNode;
    }

    public void setAllUpperNodes(Node upperNode) {
        for (Node connection: connections) {
            connection.setUpperNode(upperNode);
        }
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
