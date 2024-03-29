package src.structure;

import src.parser.*;
import java.util.ArrayList;
import java.util.List;
import src.helper.*;

/**
 * The type Network.
 *
 * @author Hannes Schniz
 * @version 1.0
 */
public class Network {

    private static final int ZERO = 0;
    private static final int ONE = 1;
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
        this.trees.get(ZERO).initNewConnections(children);
        this.trees.get(ZERO).setAllUpperNodes(this.trees.get(ZERO));
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
        for (int i = ZERO; i < layers.length; i++) {
            String[] currLayer = Parser.pointNotation(layers[i]);
            Node pointer = new Node(new IP(currLayer[ZERO]), currLayer.length - ONE, false, new ArrayList<>(), null);
            for (int j = ONE; j < currLayer.length; j++) {
                pointer.addConnection(new Node(new IP(currLayer[j]), ONE, false, new ArrayList<>(), pointer));
                pointer.correctLayers();
            }
            if (i == ZERO) {
                this.trees.add(pointer);
                this.trees.get(i).setRoot(true);
            }
            else {
                Node toEdit = tools.findNode(this.trees.get(ZERO), pointer.getTag());
                for (Node node: pointer.getConnections()) {
                    editNode(this.trees, toEdit, node);
                }
            }
        }
    }

    /**
     * Add a subnet.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        boolean returnBool = false;
        List<Node> newTrees = new ArrayList<>(tools.copyTrees(getTrees()));
        for (Node tree: getTrees()) {
            boolean edited = false;
            List<Node> dfsTree = tools.dfs(tree, new ArrayList<>());
            List<Node> dfs = new ArrayList<>(dfsTree);
            int i = ZERO;
            int pos = tools.getTree(subnet.getTrees(), dfs.get(i).getTag());
            if (pos >= ZERO) {
                newTrees.add(subnet.getTrees().get(pos));
                dfsTree = tools.dfs(subnet.getTrees().get(pos), new ArrayList<>());
                while (!tools.containsAll(dfsTree, dfs)) {
                    if (!tools.containsIP(dfsTree, dfs.get(i).getTag())) {
                        if (tools.containsIP(dfsTree, dfs.get(i).getUpperNode().getTag())) {
                            newTrees = editNode(newTrees, tools.findNode(subnet.getTrees().get(pos),
                                    dfs.get(i).getUpperNode().getTag()), dfs.get(i));
                        }
                    }
                    i++;
                    dfsTree = tools.dfs(subnet.getTrees().get(pos), new ArrayList<>());
                    returnBool = true;
                    edited = true;
                    if (i == dfs.size()) {
                        break;
                    }
                }
            }
            if (edited) {
                newTrees.remove(newTrees.get(tools.getTree(newTrees, tree.getTag())));
                if (tools.hasCircle(newTrees)) {
                    return false;
                }
            }
        }
        for (Node tree: subnet.getTrees()) {
            List<Node> inputDFS = tools.dfs(tree, new ArrayList<>());
            int i = ZERO;
            int pos = tools.getTree(newTrees, inputDFS.get(i).getTag());
            if (pos >= ZERO) {
                List<Node> dfsTree = tools.dfs(newTrees.get(pos), new ArrayList<>());
                while (!tools.containsAll(dfsTree, inputDFS)) {
                    if (!tools.containsIP(dfsTree, inputDFS.get(i).getTag())) {
                        if (tools.containsIP(dfsTree, inputDFS.get(i).getUpperNode().getTag())) {
                            newTrees = editNode(newTrees, tools.findNode(newTrees.get(pos),
                                    inputDFS.get(i).getUpperNode().getTag()), inputDFS.get(i));
                            returnBool = true;
                        }
                    }
                    i++;
                    dfsTree = tools.dfs(newTrees.get(pos), new ArrayList<>());
                    if (i == inputDFS.size()) {
                        break;
                    }
                }
            }
            else {
                newTrees.add(tree);
            }
        }
        setTrees(newTrees);
        return returnBool;
    }

    /**
     * Sets trees.
     *
     * @param trees the trees
     */
    public void setTrees(List<Node> trees) {
        this.trees = trees;
    }

    /**
     * Gets trees.
     *
     * @return the trees
     */
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
            foundList.addAll(tools.dfs(tree, new ArrayList<>()));
        }
        List<IP> returnValues = new ArrayList<>();
        for (Node found: foundList) {
            returnValues.add(found.getTag());
        }
        return tools.sortList(returnValues, ONE);
    }

    /**
     * Connect boolean.
     *
     * @param ipONE the ip ONE
     * @param ip2   the ip 2
     * @return the boolean
     */
    public boolean connect(final IP ipONE, final IP ip2) {
        Node treeOne = tools.findNode(this.trees.get(tools.getTree(this.trees, ipONE)), ipONE);
        Node treeTwo = tools.findNode(this.trees.get(tools.getTree(this.trees, ip2)), ip2);
        List<Node> dfsTree = tools.dfs(treeTwo, new ArrayList<>());
        if (!tools.containsIP(dfsTree, ipONE)) {
            editNode(getTrees(), treeOne, treeTwo);
            return true;
        }
        return false;
    }

    private List<Node> editNode(List<Node> tree, Node target, Node toAdd) {
        toAdd.setUpperNode(target);
        toAdd.setLayer(target.getLayer() + ONE);
        target.addConnection(toAdd);
        target.setAllUpperNodes(target);
        Node root = tools.shiftTop(target);
        tree.set(tools.getTree(tree, root.getTag()), root);
        return tree;
    }

    /**
     * Disconnect boolean.
     *
     * @param ipONE the ip ONE
     * @param ip2   the ip 2
     * @return the boolean
     */
    public boolean disconnect(final IP ipONE, final IP ip2) {
        if (contains(ipONE) && contains(ip2)) {
            Node nodeOne = tools.findNode(this.trees.get(tools.getTree(this.trees, ipONE)), ipONE);
            Node nodeTwo = tools.findNode(this.trees.get(tools.getTree(this.trees, ip2)), ip2);
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
        this.trees.set(tools.getTree(this.trees, source.getTag()), tools.shiftTop(source));
        this.trees.add(toDisconnect);
    }

    /**
     * Contains the node.
     *
     * @param ip the ip
     * @return the boolean
     */
    public boolean contains(final IP ip) {
        for (IP inTrees: list()) {
            if (inTrees.compareTo(ip) == ZERO) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets height of the tree.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final IP root) {
        return getLevels(root).size() - ONE;
    }

    /**
     * Gets levels.
     *
     * @param root the root
     * @return the levels
     */
    public List<List<IP>> getLevels(final IP root) {
        List<Node> newTrees = new ArrayList<>(moveRoot(getTrees().get(tools.getTree(getTrees(), root)), root));
        int pos = tools.getTree(newTrees, root);
        List<List<IP>> output = new ArrayList<>();
        if (pos >= ZERO) {
            Node tree = tools.findNode(newTrees.get(pos), root);
            List<IP> insert = new ArrayList<>();
            List<Node> dfs = tools.dfs(tree, new ArrayList<>());
            int curr = ZERO;
            for (Node node: tools.sortByLayer(dfs)) {
                if (curr != node.getLayer()) {
                    curr++;
                    output.add(tools.sortList(insert, ONE));
                    insert = new ArrayList<>();
                }
                insert.add(node.getTag());
            }
            output.add(tools.sortList(insert, ONE));

        }
        return output;
    }

    private List<Node> moveRoot(Node tree, IP root) {
        List<Node> newRootTree = tools.copyTrees(getTrees());
        if (tree.getTag().compareTo(root) != ZERO) {
            List<IP> path = getRoute(tree.getTag(), root);
            List<Node> backwards = new ArrayList<>();
            for (int i = path.size() - ONE; i >= ZERO; i--) {
                backwards.add(tools.findNode(newRootTree.get(tools.getTree(newRootTree, path.get(i))), path.get(i)));
            }
            Node prev = null;
            for (int i = ZERO; i < backwards.size(); i++) {
                if (i != backwards.size() - ONE) {
                    backwards.get(i).addConnection(backwards.get(i).getUpperNode());
                }
                backwards.get(i).setUpperNode(prev);
                if (i > ZERO) {
                    backwards.get(i).setRoot(false);
                    backwards.get(i).removeConnection(prev.getTag());
                }
                else {
                    backwards.get(i).setRoot(true);
                }
                backwards.get(i).correctLayers();
                backwards.get(i).correctGradiant();
                prev = backwards.get(i);
            }
            newRootTree.set(tools.getTree(newRootTree, tree.getTag()), tools.shiftTop(prev));
        }
        return tools.correctAllLayers(newRootTree);
    }

    /**
     * Gets route.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<IP> getRoute(final IP start, final IP end) {
        if (contains(start) && contains(end) && tools.getTree(this.trees, start) == tools.getTree(this.trees, end)) {
            Node curr = tools.findNode(this.trees.get(tools.getTree(this.trees, end)), end);
            List<IP> pathFromEnd = new ArrayList<>();
            List<IP> pathFromStart = new ArrayList<>();
            while (!curr.isRoot()) {
                pathFromEnd.add(curr.getTag());
                curr = curr.getUpperNode();
            }
            pathFromEnd.add(curr.getTag());
            if (curr.getTag().compareTo(start) != ZERO) {
                curr = tools.findNode(this.trees.get(tools.getTree(this.trees, start)), start);
                while (!tools.containsAny(pathFromEnd, pathFromStart)) {
                    pathFromStart.add(curr.getTag());
                    curr = curr.getUpperNode();
                }
            }
            for (int i = pathFromEnd.size() - ONE; i >= ZERO; i--) {
                if (!pathFromStart.contains(pathFromEnd.get(i))) {
                    pathFromStart.add(pathFromEnd.get(i));
                }
            }
            return pathFromStart;
        }
        return new ArrayList<>();
    }

    /**
     * To string string.
     *
     * @param root the root
     * @return the string
     */
    public String toString(IP root) {
        List<Node> newTrees = new ArrayList<>(moveRoot(getTrees().get(tools.getTree(getTrees(), root)), root));
        Node rootNode = newTrees.get(tools.getTree(newTrees, root));
        String out = tools.recPrint("", rootNode);
        return out;
    }
}
