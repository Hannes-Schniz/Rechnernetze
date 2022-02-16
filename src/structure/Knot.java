package src.structure;

import java.util.List;

public class Knot {

    private final IP tag;
    private int gradiant;
    private boolean isRoot;
    private List<Knot> connections;
    private Knot upperKnot;

    public Knot(IP tag, int gradiant, boolean isRoot, List<Knot> connections, Knot upperKnot) {
        this.tag = tag;
        this.gradiant = gradiant;
        this.isRoot = isRoot;
        this.connections = connections;
        this.upperKnot = upperKnot;
    }

    public Knot(IP tag) {
        this.tag = tag;
        this.gradiant = 0;
        this.isRoot = false;
        this.connections = null;
        this.upperKnot = null;
    }

    public IP getTag() {
        return tag;
    }

    public int getGradiant() {
        return gradiant;
    }

    public void setGradiant(int gradiant) {
        this.gradiant = gradiant;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public List<Knot> getConnections() {
        return connections;
    }

    public void setConnections(List<Knot> connections) {
        this.connections = connections;
    }

    public void initNewConnections(List<IP> connections) {
        for (IP connection : connections) {
            this.connections.add(new Knot(connection));
        }
    }

    public Knot getUpperKnot() {
        return upperKnot;
    }

    public void setUpperKnot(Knot upperKnot) {
        this.upperKnot = upperKnot;
    }

    public void setAllUpperKnots(Knot upperKnot) {
        for (Knot connection: connections) {
            connection.setUpperKnot(upperKnot);
        }
    }
}
