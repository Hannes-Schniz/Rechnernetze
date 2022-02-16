package src.structure;
import src.parser.*;

import java.util.ArrayList;
import java.util.List;

public class Network {

    private List<Node> trees;
    public Network(final IP root, final List<IP> children) {
        this.trees = new ArrayList<>();
        this.trees.add(new Node(root, children.size(), true,new ArrayList<>(),null));
        this.trees.get(0).initNewConnections(children);
        this.trees.get(0).setAllUpperNodes(this.trees.get(0));
    }

    public Network(final String bracketNotation) throws ParseException {
    }

    public boolean add(final Network subnet) {
        return false;
    }

    public List<IP> list() {
        List<Node> foundList = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            foundList.addAll(dfs(this.trees.get(i)));
        }
        List<IP> returnValues = new ArrayList<>();
        for (Node found: foundList) {
            returnValues.add(found.getTag());
        }
        return returnValues; //still needs to be sorted
    }

    public boolean connect(final IP ip1, final IP ip2) {
        return false;
    }

    public boolean disconnect(final IP ip1, final IP ip2) {
        return false;
    }

    public boolean contains(final IP ip) {
        return this.list().contains(ip);
    }

    public int getHeight(final IP root) {
        int currMaxHeight = 0;
        if (contains(root)) {
            List<Node> foundTree = getTree(root);
            assert foundTree != null;
            for (Node node: foundTree) {
                if (currMaxHeight < node.getLayer()) {
                    currMaxHeight = node.getLayer();
                }
            }
        }
        return currMaxHeight;
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
        int returnValue = 0;
        if (!found.containsAll(pointer)) {
            for (Node node:pointer) {
                if (found.contains(node)) {
                    returnValue++;
                }
            }
        }
        return returnValue;
    }

    private List<Node> dfs(Node tree) {
        List<Node> foundList = new ArrayList<>();
        Node pointer = tree;
        foundList.add(tree);
        while (!pointer.isRoot() && !foundList.containsAll(pointer.getConnections())) {
            if (firstNode(pointer.getConnections(), foundList) == 0) {
                pointer = pointer.getUpperNode();
            }
            pointer = pointer.getConnections().get(firstNode(pointer.getConnections(), foundList));
            foundList.add(pointer);
        }
        return foundList;
    }

    private List<Node> getTree(IP tag) {
        for (Node tree : trees) {
            List<Node> curr = dfs(tree);
            for (Node node : curr) {
                if (node.getTag().equals(tag)) {
                    return curr;
                }
            }
        }
        return null;
    }

}
