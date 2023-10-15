public class PathNode {
    int index;
    double x;
    double y;
    double gn;
    double hn;
    PathNode parent;

    PathNode(int index, double x, double y) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.gn = Double.MAX_VALUE;
        this.hn = Double.MAX_VALUE;
        PathNode parent = null;
    }
}