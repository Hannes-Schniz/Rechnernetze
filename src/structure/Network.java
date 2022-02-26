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
        this.trees = new ArrayList<>();
        String[] layers = Parser.parseToTree(bracketNotation);
        for (int i = 0; i < layers.length; i++) {
            String[] currLayer = Parser.pointNotation(layers[i]);
            Node pointer = new Node(new IP(currLayer[0]), currLayer.length - 1, false, new ArrayList<>(), null);
            for (int j = 1; j < currLayer.length; j++) {
                pointer.addConnection(new Node(new IP(currLayer[j]), 1, false, new ArrayList<>(), pointer));
                pointer.correctLayers();
            }
            if (i == 0) {
                this.trees.add(pointer);
                this.trees.get(i).setRoot(true);
            }
            else {
                Node toEdit = findNode(this.trees.get(0), pointer.getTag());
                for (Node node: pointer.getConnections()) {
                    editNode(toEdit, node);
                }
            }
        }
    }

    private Node shiftTop(Node input) {
        Node root = input;
        while (!root.isRoot()) {
            root.correctLayers();
            root = root.getUpperNode();
        }
        return root;
    }

    /**
     * Add a subnet.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        boolean returnBool = false;
        List<Node> oldTrees = new ArrayList<>(this.trees);
        for (Node tree: oldTrees) {
            boolean edited = false;
            List<Node> dfsTree = dfs(tree, new ArrayList<>());
            List<Node> dfs = new ArrayList<>(dfsTree);
            int i = 0;
            int pos = getTree(subnet.getTrees(), dfs.get(i).getTag());
            if (pos >= 0) {
                this.trees.add(subnet.getTrees().get(pos));
                dfsTree = dfs(subnet.getTrees().get(pos), new ArrayList<>());
                while (!dfsTree.containsAll(dfs)) {
                    if (!containsIP(dfsTree, dfs.get(i).getTag())) {
                        if (containsIP(dfsTree, dfs.get(i).getUpperNode().getTag())) {
                            editNode(findNode(subnet.getTrees().get(pos),
                                    dfs.get(i).getUpperNode().getTag()), dfs.get(i));
                        }
                    }
                    i++;
                    dfsTree = dfs(subnet.getTrees().get(pos), new ArrayList<>());
                    returnBool = true;
                    edited = true;
                    if (i == dfs.size()) {
                        break;
                    }
                }
            }
            if (edited) {
                this.trees.remove(tree);
            }
        }
        for (Node tree: subnet.getTrees()) {
            List<Node> inputDFS = dfs(tree, new ArrayList<>());
            int i = 0;
            int pos = getTree(this.trees, inputDFS.get(i).getTag());
            if (pos >= 0) {
                List<Node> dfsTree = dfs(this.trees.get(pos), new ArrayList<>());
                while (!dfsTree.containsAll(inputDFS)) {
                    if (!containsIP(dfsTree, inputDFS.get(i).getTag())) {
                        if (containsIP(dfsTree, inputDFS.get(i).getUpperNode().getTag())) {
                            editNode(findNode(this.trees.get(pos), inputDFS.get(i).getUpperNode().getTag()), inputDFS.get(i));
                        }
                    }
                    i++;
                    dfsTree = dfs(this.trees.get(pos), new ArrayList<>());
                    returnBool = true;
                    if (i == inputDFS.size()) {
                        break;
                    }
                }
            }
            else {
                this.trees.add(tree);
            }
        }
        return returnBool;
    }

    private Node findNode(Node tree, IP tag) {
        List<Node> dfsTree = dfs(tree, new ArrayList<>());
        for (Node node: dfsTree) {
            if (node.getTag().compareTo(tag) == 0) {
                return node;
            }
        }
        return null;
    }

    public List<Node> getTrees() {
        return trees;
    }

    /**
     * List all nodes.
     *
     * @return the list
     */
    public List<IP> list() {
        List<Node> foundList = new ArrayList<>();
        for (Node tree : trees) {
            foundList.addAll(dfs(tree, new ArrayList<>()));
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
        Node treeOne = findNode(this.trees.get(getTree(this.trees, ip1)), ip1);
        Node treeTwo = findNode(this.trees.get(getTree(this.trees, ip2)), ip2);
        List<Node> dfsTree = dfs(treeTwo, new ArrayList<>());
        if (!containsIP(dfsTree, ip1)) {
            editNode(treeOne, treeTwo);
            return true;
        }
        return false;
    }

    private void editNode(Node target, Node toAdd) {
        toAdd.setUpperNode(target);
        toAdd.setLayer(target.getLayer() + 1);
        target.addConnection(toAdd);
        Node root = shiftTop(target);
        this.trees.set(getTree(this.trees, root.getTag()), root);
    }

    private boolean hasCircle(Node tree, Node toAdd) {
        return false;
    }

    private boolean containsIP(List<Node> tree, IP ip) {
        for (Node node: tree) {
            if (node.getTag().compareTo(ip) == 0) {
                return true;
            }
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
        if (contains(ip1) && contains(ip2)) {
            Node nodeOne = findNode(this.trees.get(getTree(this.trees, ip1)), ip1);
            Node nodeTwo = findNode(this.trees.get(getTree(this.trees, ip2)), ip2);
            if (nodeOne.getConnections().contains(nodeTwo)) {
                disconnectNodes(nodeOne, nodeTwo);
                return true;
            }
            if (nodeTwo.getConnections().contains(nodeOne)) {
                disconnectNodes(nodeTwo, nodeOne);
                return true;
            }
        }
        return false;
    }

    private void disconnectNodes(Node source, Node toDisconnect) {
        List<Node> newConnections = new ArrayList<>(source.getConnections());
        newConnections.remove(toDisconnect);
        source.setConnections(newConnections);
        toDisconnect.setUpperNode(null);
        this.trees.set(getTree(this.trees, source.getTag()), shiftTop(source));
        this.trees.add(toDisconnect);
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
        return getLevels(root).size() - 1;
    }

    /**
     * Gets levels.
     *
     * @param root the root
     * @return the levels
     */
    public List<List<IP>> getLevels(final IP root) {
        int pos = getTree(this.trees, root);
        List<List<IP>> output = new ArrayList<>();
        if (pos >= 0) {
            Node tree = findNode(this.trees.get(pos), root);
            List<IP> insert = new ArrayList<>();
            List<Node> dfs = dfs(tree, new ArrayList<>());
            int curr = 0;
            for (Node node: sortByLayer(dfs)) {
                if (curr != node.getLayer()) {
                    curr++;
                    output.add(sortList(insert));
                    insert = new ArrayList<>();
                }
                insert.add(node.getTag());
            }
            output.add(sortList(insert));

        }
        return output;
    }

    private List<Node> sortByLayer(List<Node> input) {
        List<Node> output = new ArrayList<>();
        int i = 0;
        int layer = 0;
        while (!output.containsAll(input)) {
            if (i == input.size()) {
                i = 0;
                layer = layer + 1;
            }
            if (input.get(i).getLayer() == layer) {
                output.add(input.get(i));
            }
            i = i + 1;
        }
        return output;
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
        Node rootNode = this.trees.get(getTree(this.trees, root));
        return recPrint("", rootNode);
    }

    private String recPrint(String output, Node node) {
        List<String> toParse = new ArrayList<>();
        toParse.add(Parser.parseToString(node.getTag().getAddress()));
        if (node.getConnections().size() > 0) {
            List<Node> sortedConnections = sortNodes(node.getConnections());
            for (Node connection: sortedConnections) {
                toParse.add(recPrint(Parser.parseToBracket(node.getLayerNodes()), connection));
            }
            return Parser.parseToBracket(toParse);
        }
        else {
            return toParse.get(0);
        }
    }

    private List<Node> sortNodes(List<Node> input) {
        List<IP> sortedIPs = new ArrayList<>();
        List<Node> output = new ArrayList<>();
        for (Node node: input) {
            sortedIPs.add(node.getTag());
        }
        sortedIPs = sortList(sortedIPs);
        for (IP ip: sortedIPs) {
            for (Node node:input) {
                if (node.getTag().compareTo(ip) == 0) {
                    output.add(node);
                }
            }
        }
        return output;
    }

    private List<Node> dfs(Node tree, List<Node> input) {
        input.add(tree);
        for (Node node: tree.getConnections()) {
            dfs(node, input);
        }
        return input;
    }

    private int getTree(List<Node> trees, IP tag) {
        for (int i = 0; i < trees.size(); i++) {
            List<Node> dfsTree = dfs(trees.get(i), new ArrayList<>());
            for (Node node : dfsTree) {
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
