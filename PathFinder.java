import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;

public class PathFinder {
    int[][] adjacency_matrix;
    HashMap<Integer, PathNode> nodes;

    PathFinder(int[][] adjacency_matrix, HashMap<Integer, PathNode> nodes) {
        this.adjacency_matrix = adjacency_matrix;
        this.nodes = nodes;
    }

    public ArrayList<Integer> findPath(int start, int goal) {
        // Open represents nodes which are candidates for exploration.
        // The node with the lowest f(n) = g(n) + h(n) is at the head of open.
        PriorityQueue<PathNode> open = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
        // Closed contains nodes which have already been considered.
        HashSet<Integer> closed = new HashSet<>();
        // Path contains a squence of indices that represent the shortest path found.
        ArrayList<Integer> path = new ArrayList<>();
        PathNode goalNode = nodes.get(goal);

        open.add(nodes.get(start)); // First the start node is added to the open list so that we have a node to evaluate.
        while(open.size() > 0) {
            PathNode current = open.poll(); // Obtain the node with the lowest f(n) = g(n) + h(n)
            closed.add(current.index); // Mark the current node as explored.

            if(current.index == goal) { // We have reached the goal node.
                PathNode child = goalNode;
                while(child.index != start) { // Construct path by moving through the chain of parents.
                    path.add(child.index);
                    child = child.parent;
                }
                path.add(start);
                return path;
            }

            // Obtain the current node's neighbor nodes.
            ArrayList<Integer> neighbors = new ArrayList<>();
            for(int i = 0; i < adjacency_matrix[0].length; i++) {
                if(adjacency_matrix[current.index][i] > 0) neighbors.add(i);
            }

            for(int neighbor : neighbors) {
                if (closed.contains(neighbor)) { // This node has already been evaluated.
                    continue;
                }
                PathNode neighborNode = nodes.get(neighbor);
                
                // If the gn is Double.MAX value then gn has not been set.
                double currentgn = (current.gn == Double.MAX_VALUE) ? 0 : current.gn; 
                double neighborgn = (neighborNode.gn == Double.MAX_VALUE) ? 0 : neighborNode.gn;

                // Calculate the cost of moving to the neighbor node, g(n).
                double movementCost = currentgn + adjacency_matrix[current.index][neighborNode.index];

                // We have to update the neighbor node if a path to the neighbor node has not been set or we have calculated a shorter path (smaller g(n)).
                if(movementCost < neighborgn || neighborNode.gn == Double.MAX_VALUE || !open.contains(neighborNode)) {
                    neighborNode.gn = movementCost;
                    // There is no heuristic therefor h(n) = 0. This is where a heuristic would be calculated.
                    neighborNode.hn = 0;
                    neighborNode.parent = current;

                    if (!open.contains(neighborNode)) {
                        open.add(neighborNode);
                    }
                }
            }
        }
        return path;
    }
}