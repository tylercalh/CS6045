public class PathNode {
    int index;
    double gn;
    double hn;
    PathNode parent;

    PathNode(int index) {
        this.index = index;
        this.gn = Double.MAX_VALUE;
        this.hn = Double.MAX_VALUE;
    }
}