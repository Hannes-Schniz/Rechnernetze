package src.helper;

import src.parser.Parser;
import src.structure.IP;
import src.structure.Node;

import java.util.ArrayList;
import java.util.List;

public class tools {

    private static final int ZERO = 0;
    private static final int ONE = 1;

    public static boolean containsIP(List<Node> tree, IP ip) {
        for (Node node: tree) {
            if (node.getTag().compareTo(ip) == ZERO) {
                return true;
            }
        }
        return false;
    }

    public static List<Node> dfs(Node tree, List<Node> input) {
        input.add(tree);
        for (Node node: tree.getConnections()) {
            dfs(node, input);
        }
        return input;
    }

    public static List<Node> sortNodes(List<Node> input) {
        List<IP> sortedIPs = new ArrayList<>();
        List<Node> output = new ArrayList<>();
        for (Node node: input) {
            sortedIPs.add(node.getTag());
        }
        for (IP ip: sortList(sortedIPs)) {
            for (Node node:input) {
                if (node.getTag().compareTo(ip) == ZERO) {
                    output.add(node);
                }
            }
        }
        return output;
    }

    public static List<IP> sortList(List<IP> input) {
        int i = ONE;
        while (i < input.size()) {
            if (i > ZERO) {
                if (input.get(i).compareTo(input.get(i - ONE)) < ZERO) {
                    IP curr = input.get(i);
                    input.set(i, input.get(i - ONE));
                    input.set(i - ONE, curr);
                    i--;
                }
            }
            i++;
        }
        return input;
    }

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

    public static Node findNode(Node tree, IP tag) {
        List<Node> dfsTree = dfs(tree, new ArrayList<>());
        for (Node node: dfsTree) {
            if (node.getTag().compareTo(tag) == ZERO) {
                return node;
            }
        }
        return null;
    }

    public static Node shiftTop(Node input) {
        Node root = input;
        while (!root.isRoot()) {
            root.correctLayers();
            root = root.getUpperNode();
        }
        return root;
    }

}
