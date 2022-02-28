package src.helper;

import src.parser.Parser;
import src.structure.IP;
import src.structure.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tools.
 */
public class tools {

    private static final int ZERO = 0;
    private static final int ONE = 1;

    /**
     * Contains ip boolean.
     *
     * @param tree the tree
     * @param ip   the ip
     * @return the boolean
     */
    public static boolean containsIP(List<Node> tree, IP ip) {
        for (Node node: tree) {
            if (node.getTag().compareTo(ip) == ZERO) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dfs list.
     *
     * @param tree  the tree
     * @param input the input
     * @return the list
     */
    public static List<Node> dfs(Node tree, List<Node> input) {
        input.add(tree);
        for (Node node: tree.getConnections()) {
            dfs(node, input);
        }
        return input;
    }

    /**
     * Sort nodes list.
     *
     * @param input the input
     * @return the list
     */
    public static List<Node> sortNodes(List<Node> input) {
        List<IP> sortedIPs = new ArrayList<>();
        List<Node> output = new ArrayList<>();
        for (Node node: input) {
            sortedIPs.add(node.getTag());
        }
        sortedIPs = sortList(sortedIPs, 0);
        for (IP ip: sortedIPs) {
            for (Node node: input) {
                if (node.getTag().equals(ip)) {
                    output.add(node);
                    break;
                }
            }
        }
        return output;
    }

    /**
     * Sort list list.
     *
     * @param input   the input
     * @param exclude the exclude
     * @return the list
     */
    public static List<IP> sortList(List<IP> input, int exclude) {
        boolean sorted = false;
        IP temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < input.size() - 1; i++) {
                if (input.get(i).compareTo(input.get(i + 1)) > 0) {
                    temp = input.get(i);
                    input.set(i, input.get(i + 1));
                    input.set(i + 1, temp);
                    sorted = false;
                }
            }
        }
        return input;
    }

    /**
     * Gets tree.
     *
     * @param trees the trees
     * @param tag   the tag
     * @return the tree
     */
    public static int getTree(List<Node> trees, IP tag) {
        for (int i = 0; i < trees.size(); i++) {
            List<Node> dfsTree = dfs(trees.get(i), new ArrayList<>());
            for (Node node : dfsTree) {
                if (node.getTag().compareTo(tag) == ZERO) {
                    return i;
                }
            }
        }
        return -ONE;
    }

    /**
     * Rec print string.
     *
     * @param output the output
     * @param node   the node
     * @return the string
     */
    public static String recPrint(String output, Node node) {
        List<String> toParse = new ArrayList<>();
        toParse.add(Parser.parseToString(node.getTag().getAddress()));
        if (node.getConnections().size() > ZERO) {
            List<Node> sortedConnections = sortNodes(node.getConnections());
            for (Node connection: sortedConnections) {
                toParse.add(recPrint(Parser.parseToBracket(node.getLayerNodes()), connection));
            }
            return Parser.parseToBracket(toParse);
        }
        else {
            return toParse.get(ZERO);
        }
    }

    /**
     * Contains any boolean.
     *
     * @param source   the source
     * @param contains the contains
     * @return the boolean
     */
    public static boolean containsAny(List<IP> source, List<IP> contains) {
        for (IP ip: source) {
            for (IP ip2: contains) {
                if (ip.compareTo(ip2) == ZERO) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contains all boolean.
     *
     * @param source   the source
     * @param contains the contains
     * @return the boolean
     */
    public static boolean containsAll(List<Node> source, List<Node> contains) {
        if (source.size() > contains.size()) {
            return false;
        }
        for (Node node: source) {
            for (int i = 0; i < contains.size(); i++) {
                if (node.getTag().equals(contains.get(i).getTag())) {
                    break;
                }
                if (i == contains.size()) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Sort by layer list.
     *
     * @param input the input
     * @return the list
     */
    public static List<Node> sortByLayer(List<Node> input) {
        List<Node> output = new ArrayList<>();
        int i = ZERO;
        int layer = ZERO;
        while (!output.containsAll(input)) {
            if (i == input.size()) {
                i = ZERO;
                layer = layer + ONE;
            }
            if (input.get(i).getLayer() == layer) {
                output.add(input.get(i));
            }
            i = i + ONE;
        }
        return output;
    }

    /**
     * Find node node.
     *
     * @param tree the tree
     * @param tag  the tag
     * @return the node
     */
    public static Node findNode(Node tree, IP tag) {
        List<Node> dfsTree = dfs(tree, new ArrayList<>());
        for (Node node: dfsTree) {
            if (node.getTag().compareTo(tag) == ZERO) {
                return node;
            }
        }
        return null;
    }

    /**
     * Shift top node.
     *
     * @param input the input
     * @return the node
     */
    public static Node shiftTop(Node input) {
        Node root = input;
        while (!root.isRoot()) {
            root.correctLayers();
            root = root.getUpperNode();
        }
        return root;
    }

    /**
     * Copy trees list.
     *
     * @param source the source
     * @return the list
     */
    public static List<Node> copyTrees(List<Node> source) {
        List<Node> copyNetwork = new ArrayList<>();
        for (Node tree: source) {
            copyNetwork.add(new Node(tree, null));
        }
        return copyNetwork;
    }

    /**
     * Tree equals boolean.
     *
     * @param tree1 the tree 1
     * @param tree2 the tree 2
     * @return the boolean
     */
    public static boolean treeEquals(Node tree1, Node tree2) {
        List<Node> dfsList1 = sortByLayer(dfs(tree1, new ArrayList<>()));
        List<Node> dfsList2 = sortByLayer(dfs(tree2, new ArrayList<>()));
        if (dfsList1.size() != dfsList2.size()) {
            return false;
        }
        for (int i = ZERO; i < dfsList1.size(); i++) {
            if (!dfsList1.get(i).getTag().equals(dfsList2.get(i).getTag())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Correct all layers list.
     *
     * @param input the input
     * @return the list
     */
    public static List<Node> correctAllLayers(List<Node> input) {
        for (Node node: input) {
            List<Node> dfs = dfs(node, new ArrayList<>());
            for (Node dfsNode: dfs) {
                if (dfsNode.isRoot()) {
                    dfsNode.setLayer(ZERO);
                }
                else {
                    dfsNode.correctLayers();
                }
            }
        }
        return input;
    }

}
