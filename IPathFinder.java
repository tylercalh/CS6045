// All pathfinder algorithm classes must implement this
public interface IPathFinder {
    // The only method from the algorithm's class that needs to be called from an external class
    public Results findPath(Graph g); // Will throw an error until the Graph class is created by Tyler
}
