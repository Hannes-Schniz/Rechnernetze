package src.structure;
import src.parser.*;

import java.util.ArrayList;
import java.util.List;

public class Network {

    private Node tree;
    public Network(final IP root, final List<IP> children) {
        this.tree = new Node(root, children.size(), true,new ArrayList<>(),null);
        this.tree.initNewConnections(children);
        this.tree.setAllUpperNodes(this.tree);
    }

    public Network(final String bracketNotation) throws ParseException {
    }

    public boolean add(final Network subnet) {
        return false;
    }

    public List<IP> list() {
        Node pointer = this.tree;
        List<Node> foundList = new ArrayList<>();
        foundList.add(tree);
        while (!pointer.isRoot() && !foundList.containsAll(pointer.getConnections())) {
            if (firstNode(pointer.getConnections(), foundList) == 0) {
                pointer = pointer.getUpperNode();
            }
            pointer = pointer.getConnections().get(firstNode(pointer.getConnections(), foundList));
            foundList.add(pointer);
        }
        List<IP> returnValues = new ArrayList<>();
        for (Node found: foundList) {
            returnValues.add(found.getTag());
        }
        return returnValues;
    }

    public boolean connect(final IP ip1, final IP ip2) {
        return false;
    }

    public boolean disconnect(final IP ip1, final IP ip2) {
        return false;
    }

    public boolean contains(final IP ip) {
        return false;
    }

    public int getHeight(final IP root) {
        return 0;
    }

    public List<List<IP>> getLevels(final IP root) {
        return null;
    }

    public List<IP> getRoute(final IP start, final IP end) {
        return null;
    }

    public String toString(IP root) {
        return null;
    }

    private int firstNode(List<Node> pointer, List<Node> found) {
        if (found.containsAll(pointer)) {
            return 0;
        }
        int retunValue = 0;
        for (Node node:pointer) {
            if (found.contains(pointer)) {
                retunValue++;
            }
        }
        return retunValue;
    }
}
