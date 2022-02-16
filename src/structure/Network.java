package src.structure;
import src.parser.*;

import java.util.List;

public class Network {

    private Knot tree;
    public Network(final IP root, final List<IP> children) {
        this.tree = new Knot(root, children.size(), true,null,null);
        this.tree.initNewConnections(children);
        this.tree.setAllUpperKnots(this.tree);
    }

    public Network(final String bracketNotation) throws ParseException {
    }

    public boolean add(final Network subnet) {
        return false;
    }

    public List<IP> list() {
        return null;
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
}
