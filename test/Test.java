package test;
import  java.util.List;
import src.structure.*;
import src.parser.*;

public class Test {
    public static void main(String[] args) throws ParseException {
        // Construct initial network
        IP root = new IP("141.255.1.133");
        List<List<IP>> levels = List.of(List.of(root),
                List.of(new IP("0.146.197.108"), new IP("122.117.67.158")));
        final Network network = new Network(root, levels.get(1));
        // (141.255.1.133 0.146.197.108 122.117.67.158)
        System.out.println(network.toString(root));
        // 1 true
        System.out.println((levels.size() - 1) == network.getHeight(root));
        // 2 true
        System.out.println(List.of(List.of(root), levels.get(1)).equals(network.getLevels(root)));
        // "Change" root and call toString, getHeight and getLevels again
        root = new IP("122.117.67.158");
        levels = List.of(List.of(root), List.of(new IP("141.255.1.133")),
                List.of(new IP("0.146.197.108")));
        // 3 true
        System.out.println("(122.117.67.158 (141.255.1.133 0.146.197.108))".equals(network.toString(root)));
        // 4 true
        System.out.println((levels.size() - 1) == network.getHeight(root));
        // 5 true
        System.out.println(levels.equals(network.getLevels(root)));
        // Try to add circular dependency
        // 6 false
        System.out.println(network.add(new Network("(122.117.67.158 0.146.197.108)")));
        // Merge two subnets with initial network
        // 7 true
        System.out.println(network.add(new Network("(85.193.148.81 34.49.145.239 231.189.0.127 141.255.1.133)")));
        // 8 true
        System.out.println(network.add(new Network("(231.189.0.127 252.29.23.0"
                                + " 116.132.83.77 39.20.222.120 77.135.84.171)")));
        System.out.println(network.toString(root));
        // "Change" root and call toString, getHeight and getLevels again
        root = new IP("85.193.148.81");
        levels = List.of(List.of(root),
                List.of(new IP("34.49.145.239"), new IP("141.255.1.133"),
                        new IP("231.189.0.127")),
                List.of(new IP("0.146.197.108"), new IP("39.20.222.120"),
                        new IP("77.135.84.171"), new IP("116.132.83.77"),
                        new IP("122.117.67.158"), new IP("252.29.23.0")));
        // 9 true
        String compare = "(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108"
                + " 122.117.67.158) (231.189.0.127 39.20.222.120"
                + " 77.135.84.171 116.132.83.77 252.29.23.0))";
        System.out.println(compare.equals(network.toString(root)));
        // 10 true
        System.out.println(network.getHeight(root));
        System.out.println((levels.size() - 1) == network.getHeight(root));
        // 11 true
        System.out.println(levels.equals(network.getLevels(root)));
        // 12 true
        System.out.println(List
                .of(new IP("141.255.1.133"), new IP("85.193.148.81"),
                        new IP("231.189.0.127"))
                .equals(network.getRoute(new IP("141.255.1.133"),
                        new IP("231.189.0.127"))));
        // "Change" root and call getHeight again
        root = new IP("34.49.145.239");
        levels = List.of(List.of(root), List.of(new IP("85.193.148.81")),
                List.of(new IP("141.255.1.133"), new IP("231.189.0.127")),
                List.of(new IP("0.146.197.108"), new IP("39.20.222.120"),
                        new IP("77.135.84.171"), new IP("116.132.83.77"),
                        new IP("122.117.67.158"), new IP("252.29.23.0")));
        // 13 true
        System.out.println((levels.size() - 1) == network.getHeight(root));
    }
}
