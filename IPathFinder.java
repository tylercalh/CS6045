// All pathfinder algorithm classes must implement this
public interface IPathFinder {
    // The only method from the algorithm's class that needs to be called from an external class
    // start and goal are the indices of the start node of the path and the end node of the path
    public Results findPath(Graph g, int start, int goal);
}
