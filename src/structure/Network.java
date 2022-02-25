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
    private List<Node> dfsTree;

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
        this.dfsTree = new ArrayList<>();
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
        for (Node tree: this.trees) {
            doDFS(tree);
            int pos = getTree(subnet.getTrees(), tree.getTag());
            if (pos >= 0) {
                Node toEdit = findNode(subnet.getTrees().get(pos), tree.getTag());
                toEdit.addAllConnections(tree.getConnections());
                this.trees.set(getTree(this.trees, tree.getTag()), shiftTop(toEdit));
                this.trees.remove(tree);
                returnBool = true;
            }
        }
        for (Node tree: subnet.getTrees()) {
            doDFS(tree);
            List<Node> inputDFS = new ArrayList<>(this.dfsTree);
            int i = 0;
            int pos = getTree(this.trees, inputDFS.get(i).getTag());
            if (pos >= 0) {
                doDFS(this.trees.get(pos));
                while (!this.dfsTree.containsAll(inputDFS)) {
                    if (!containsIP(this.dfsTree, inputDFS.get(i).getTag())) {
                        if (containsIP(this.dfsTree, inputDFS.get(i).getUpperNode().getTag())) {
                            editNode(findNode(this.trees.get(pos), inputDFS.get(i).getUpperNode().getTag()), inputDFS.get(i));
                        }
                    }
                    i++;
                    doDFS(this.trees.get(pos));
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
        doDFS(tree);
        for (Node node: this.dfsTree) {
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
            doDFS(tree);
            foundList.addAll(dfsTree);
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
        doDFS(treeTwo);
        if (!containsIP(this.dfsTree, ip1)) {
            editNode(treeOne, treeTwo);
            return true;
        }
        return false;
    }

    private void editNode(Node target, Node toAdd) {
        toAdd.setUpperNode(target);
        target.addConnection(toAdd);
        Node root = shiftTop(target);
        this.trees.set(getTree(this.trees, root.getTag()), root);
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
            doDFS(this.trees.get(getTree(this.trees, root)));
            for (Node node: dfsTree) {
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
        List<List<IP>> layers = new ArrayList<>();
        Node rootNode = this.trees.get(getTree(this.trees, root));
        String hell = recPrint("", rootNode);
        //doDFS(rootNode);
        //for (Node node: this.dfsTree) {
        //    layers.add(node.getLayerIPs());
        //}
        //List<String> working = new ArrayList<>();
        //String output = "";
        //for (int i = layers.size() - 1; i > 1; i++) {
        //    if (layers.get(i).size() > 1) {
        //        layers.set(i, sortList(layers.get(i)));
        //        for (IP address: layers.get(i)) {
        //            working.add(Parser.parseToString(address.getAddress()));
        //        }
//
        //    }
        //}
        return hell;
    }

    private String recPrint(String output, Node node) {
        for (Node connection: node.getConnections()) {
            return recPrint(Parser.parseToBracket(node.getLayerIPs()), connection);
        }
        return output;
    }

    private void doDFS(Node tree) {
        this.dfsTree = new ArrayList<>();
        dfs(tree);
    }

    private void dfs(Node tree) {
        dfsTree.add(tree);
        for (Node node: tree.getConnections()) {
            dfs(node);
        }
    }

    private int getTree(List<Node> trees, IP tag) {
        for (int i = 0; i < trees.size(); i++) {
            doDFS(trees.get(i));
            List<Node> curr = dfsTree;
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
