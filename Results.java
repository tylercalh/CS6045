import java.util.ArrayList;

// Class for storing the results of a pathfinding method
public class Results {
    private ArrayList<Integer> path; // An ArrayList of integers holding the indices of the path
    private int numNodesExplored;
    private double pathLength;

    public Results() {
        this.path = null;
        this.numNodesExplored = 0;
        this.pathLength = 0;
    }
    public Results(ArrayList<Integer> path, int numNodesExplored, double pathLength){
        this.path = path;
        this.numNodesExplored = numNodesExplored;
        this.pathLength = pathLength;
    }

    // Setters
    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
    public void setNumNodesExplored(int numNodesExplored) {
        this.numNodesExplored = numNodesExplored;
    }

    public void setPathLength(double pathLength) {
        this.pathLength = pathLength;
    }

    // Getters

    public ArrayList<Integer> getPath() {
        return path;
    }

    public int getNumNodesExplored() {
        return numNodesExplored;
    }

    public double getPathLength() {
        return pathLength;
    }

    @Override
    public String toString() {
        String str = "";
        str += "Path: ";
        for (Integer i : path) {
            str = str.concat(i + ", ");
        }
        str += "\nPath length: " + pathLength + "\nNodes visited: " + numNodesExplored;

        return str;
    }
}
