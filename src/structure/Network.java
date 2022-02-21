package src.structure;

import src.parser.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Network.
 * @author Hannes Schniz
 * @version 1.0
 */
public class Network {

    private final int first = 0;
    private List<Node> trees;

    /**
     * Instantiates a new Network.
     *
     * @param root     the root
     * @param children the children
     */
    public Network(final IP root, final List<IP> children) {
        this.trees = new ArrayList<>();
        this.trees.add(new Node(root, children.size(), true, new ArrayList<>(), null));
        this.trees.get(first).initNewConnections(children);
        this.trees.get(first).setAllUpperNodes(this.trees.get(first));
    }

    /**
     * Instantiates a new Network.
     *
     * @param bracketNotation the bracket notation
     * @throws ParseException the parse exception
     */
    public Network(final String bracketNotation) throws ParseException {
        String[] layers = Parser.parseToTree(bracketNotation);
        this.trees = new ArrayList<>();
        String[] root = Parser.pointNotation(layers[0]);
        Node prevNode = new Node(new IP(root[0]), root.length - 1, true, new ArrayList<>(), null);
        for (int i = 1; i < root.length; i++) {
            prevNode.addConnection(new Node(new IP(root[i]), 1, false, new ArrayList<>(), prevNode));
        }
        for (int i = 1; i < layers.length; i++) {
            String[] currLayer = Parser.pointNotation(layers[i]);
            IP currTag = new IP(currLayer[0]);
            int pos = prevNode.hasConnection(currTag);
            while (pos < 0) {
                prevNode = prevNode.getUpperNode();
                pos = prevNode.hasConnection(currTag);
            }
            List<Node> prevConnections = prevNode.getConnections();
            List<Node> toChangeList = prevConnections.get(i).getConnections();
            if (toChangeList == null) {
                toChangeList = new ArrayList<>();
            }
            for (int j = 1; j < currLayer.length; j++) {
                toChangeList.add(new Node(new IP(currLayer[j]), 1,  false, new ArrayList<>(), null));
            }
            prevNode = prevConnections.get(pos);
        }
        while (!prevNode.isRoot()) {
            prevNode = prevNode.getUpperNode();
        }
        this.trees.add(prevNode);
    }

    /**
     * Add a subnet.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        return false;
    }

    /**
     * List all nodes.
     *
     * @return the list
     */
    public List<IP> list() {
        List<Node> foundList = new ArrayList<>();
        for (Node tree : trees) {
            foundList.addAll(dfs(tree));
        }
        List<IP> returnValues = new ArrayList<>();
        for (Node found: foundList) {
            returnValues.add(found.getTag());
        }
        return sortList(returnValues);
    }

    /**
     * Connect boolean.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean connect(final IP ip1, final IP ip2) {
        Node treeOne = this.trees.get(getTree(ip1));
        Node treeTwo = this.trees.get(getTree(ip2));
        if (treeOne.isRoot()) {
            treeOne.setAllUpperNodes(treeTwo);
            treeTwo.addConnection(treeOne);
            return true;
        }
        else if (treeTwo.isRoot()) {
            treeTwo.setAllUpperNodes(treeOne);
            treeOne.addConnection(treeTwo);
            return true;
        }
        return false;
    }

    /**
     * Disconnect boolean.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean disconnect(final IP ip1, final IP ip2) {
        return false;
    }

    /**
     * Contains the node.
     *
     * @param ip the ip
     * @return the boolean
     */
    public boolean contains(final IP ip) {
        return this.list().contains(ip);
    }

    /**
     * Gets height of the tree.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final IP root) {
        int currMaxHeight = first;
        if (contains(root)) {
            List<Node> foundTree = dfs(this.trees.get(getTree(root)));
            for (Node node: foundTree) {
                if (currMaxHeight < node.getLayer()) {
                    currMaxHeight = node.getLayer();
                }
            }
        }
        return currMaxHeight;
    }

    /**
     * Gets levels.
     *
     * @param root the root
     * @return the levels
     */
    public List<List<IP>> getLevels(final IP root) {
        return null;
    }

    /**
     * Gets route.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<IP> getRoute(final IP start, final IP end) {
        return null;
    }

    /**
     * To string string.
     *
     * @param root the root
     * @return the string
     */
    public String toString(IP root) {
        return null;
    }

    private int firstNode(List<Node> pointer, List<Node> found) {
        int returnValue = first;
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
            if (firstNode(pointer.getConnections(), foundList) == first) {
                pointer = pointer.getUpperNode();
            }
            pointer = pointer.getConnections().get(firstNode(pointer.getConnections(), foundList));
            foundList.add(pointer);
        }
        return foundList;
    }

    private int getTree(IP tag) {
        for (int i = 0; i < trees.size(); i++) {
            List<Node> curr = dfs(trees.get(i));
            for (Node node : curr) {
                if (node.getTag().compareTo(tag) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<IP> sortList(List<IP> input) {
        int i = 1;
        while (i < input.size()) {
            if (i > 0) {
                if (input.get(i).compareTo(input.get(i - 1)) < 0) {
                    IP curr = input.get(i);
                    input.set(i, input.get(i - 1));
                    input.set(i - 1, curr);
                    i--;
                }
                else {
                    i++;
                }
            }
            else {
                i++;
            }
        }
        return input;
    }

}
