
public class PathNode {
    int index;
    double gn;
    double hn;
    PathNode parent;
    int depth;

    PathNode(int index) {
        this.index = index;
        this.gn = Double.MAX_VALUE;
        this.hn = Double.MAX_VALUE;
    }
    
    public double getCost() {
        return this.gn + this.hn; 
    }
    
    public void setCost(double g, double h) {
        this.gn = g;
        this.hn = h;
    }
    
    public int getId() {
        return this.index;
    }
    
    public void setPrevious(PathNode p) {
        this.parent = p;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }
}