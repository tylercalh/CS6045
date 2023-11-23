import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AEpsilonStar implements IPathFinder{
    int e;
    public AEpsilonStar(int epsilon) {
        this.e = epsilon;
    }
    public AEpsilonStar() {
        this.e = 0;
    }
    @Override
    public Results findPath(Graph g, int start, int end) {
        HashMap<Integer, PathNode> nodes = g.buildNodes();
        PriorityQueue<PathNode> open = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
        PriorityQueue<PathNode> focal = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
        HashSet<Integer> closed = new HashSet<>(); // Keeps track of visited nodes
        open.add(nodes.get(start));
        PathNode goalNode = nodes.get(end);

        // The results
        ArrayList<Integer> path = new ArrayList<>(); // The generated path
        int numNodesExplored = 0;
        double pathLength = 0;

        while (open.size() > 0) {
            PathNode current = focal.isEmpty() ? open.poll() : focal.poll(); // Obtain the node with the lowest f(n) (in the FOCAL list if there are any)
            closed.add(current.index); // Mark the current node as explored

            if (current.index == end) { // We have reached the goal node.
                PathNode child = goalNode;
                while (child.index != start) { // Construct path by moving through the chain of parents.
                    path.add(child.index);
                    child = child.parent;
                }
                path.add(start);
                numNodesExplored = closed.size();
                pathLength = path.size();
                break; // Stop searching
            }

            // Get the neighbors of the current node
            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < g.adjacency_matrix[0].length; i++) {
                if (g.adjacency_matrix[current.index][i] > 0)
                    neighbors.add(i); // True if there exists an edge between current node and node with index i
            }

            for (int neighbor: neighbors) {
                if(closed.contains(neighbor)) // True if this neighbor has already been visited
                    continue;
                PathNode neighborNode = nodes.get(neighbor);

                double currentgn = (current.gn == Double.MAX_VALUE) ? 0 : current.gn;
                double neighborgn = (neighborNode.gn == Double.MAX_VALUE) ? 0 : neighborNode.gn;

                // Calculate g(n), cost of moving to the neighbor node
                double movementCost = currentgn + g.adjacency_matrix[current.index][neighbor];

                // We have to update the neighbor node if a path to the neighbor node has not been set or we have calculated a shorter path (smaller g(n)).
                if (movementCost < neighborgn || neighborNode.gn == Double.MAX_VALUE || !open.contains(neighborNode)) {
                    neighborNode.gn = movementCost;
                    // Heuristic goes here
                    neighborNode.hn = 0;
                    neighborNode.parent = current;

                    if (!open.contains(neighborNode)) {
                        open.add(neighborNode);
                    }
                }
            }
            if(!open.isEmpty()) {
                // For all the newly opened nodes, check if they can be added to FOCAL. This is done in a separate loop so that all the neighbors can be generated and added to OPEN first
                for (int neighbor : neighbors) {
                    PathNode neighborNode = nodes.get(neighbor);
                    PathNode smallestfnNode = open.peek();
                    double neighborfn = neighborNode.gn + neighborNode.hn;
                    double smallestfn = smallestfnNode.gn + smallestfnNode.hn;
                    // If this node's f(n) deviates from the node with the lowest f(n) by a factor less than (1 + e), then it is "similar" to the node with the lowest f(n) and is added to FOCAL
                    if (neighborfn <= (1 + e) * smallestfn && !focal.contains(neighborNode)) {
                        focal.add(neighborNode);
                    }
                }
            }
        }
        return new Results(path, numNodesExplored, pathLength);
    }
}
