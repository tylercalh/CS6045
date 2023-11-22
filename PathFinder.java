import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;

public class PathFinder {
    double[][] adjacency_matrix;
    HashMap<Integer, PathNode> nodes;
    int nodesVisited;

    PathFinder(double[][] adjacency_matrix) {
        this.adjacency_matrix = adjacency_matrix;
        this.nodes = new HashMap<>();
        for (int i = 0; i < adjacency_matrix[0].length; i++) {
            this.nodes.put(i, new PathNode(i));
        }
        this.nodesVisited = -1;
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

        open.add(nodes.get(start)); // First the start node is added to the open list so that we have a node to
                                    // evaluate.
        while (open.size() > 0) {
            PathNode current = open.poll(); // Obtain the node with the lowest f(n) = g(n) + h(n)
            closed.add(current.index); // Mark the current node as explored.

            if (current.index == goal) { // We have reached the goal node.
                PathNode child = goalNode;
                while (child.index != start) { // Construct path by moving through the chain of parents.
                    path.add(child.index);
                    child = child.parent;
                }
                path.add(start);

                nodesVisited = closed.size(); // Collect this metric.
                return path;
            }

            // Obtain the current node's neighbor nodes.
            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < adjacency_matrix[0].length; i++) {
                if (adjacency_matrix[current.index][i] > 0)
                    neighbors.add(i);
            }

            for (int neighbor : neighbors) {
                if (closed.contains(neighbor)) { // This node has already been evaluated.
                    continue;
                }
                PathNode neighborNode = nodes.get(neighbor);

                // If the gn is Double.MAX value then gn has not been set.
                double currentgn = (current.gn == Double.MAX_VALUE) ? 0 : current.gn;
                double neighborgn = (neighborNode.gn == Double.MAX_VALUE) ? 0 : neighborNode.gn;

                // Calculate the cost of moving to the neighbor node, g(n).
                double movementCost = currentgn + adjacency_matrix[current.index][neighborNode.index];

                // We have to update the neighbor node if a path to the neighbor node has not
                // been set or we have calculated a shorter path (smaller g(n)).
                if (movementCost < neighborgn || neighborNode.gn == Double.MAX_VALUE || !open.contains(neighborNode)) {
                    neighborNode.gn = movementCost;
                    // There is no heuristic therefor h(n) = 0. This is where a heuristic would be
                    // calculated.
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

    public ArrayList<Integer> findPath(int start, int goal, int[][] coordinates) {
        PriorityQueue<PathNode> open = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
        HashSet<Integer> closed = new HashSet<>();
        ArrayList<Integer> path = new ArrayList<>();
        PathNode goalNode = nodes.get(goal);

        open.add(nodes.get(start));
        while (open.size() > 0) {
            PathNode current = open.poll();
            closed.add(current.index);

            if (current.index == goal) {
                PathNode child = goalNode;
                while (child.index != start) {
                    path.add(child.index);
                    child = child.parent;
                }
                path.add(start);

                nodesVisited = closed.size();
                return path;
            }

            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < adjacency_matrix[0].length; i++) {
                if (adjacency_matrix[current.index][i] > 0)
                    neighbors.add(i);
            }

            for (int neighbor : neighbors) {
                if (closed.contains(neighbor)) {
                    continue;
                }
                PathNode neighborNode = nodes.get(neighbor);

                double currentgn = (current.gn == Double.MAX_VALUE) ? 0 : current.gn;
                double neighborgn = (neighborNode.gn == Double.MAX_VALUE) ? 0 : neighborNode.gn;
                double movementCost = currentgn
                        + getDistance(coordinates[current.index][0], coordinates[current.index][1],
                                coordinates[neighborNode.index][0], coordinates[neighborNode.index][1]);

                if (movementCost < neighborgn || neighborNode.gn == Double.MAX_VALUE || !open.contains(neighborNode)) {
                    neighborNode.gn = movementCost;
                    neighborNode.hn = getDistance(coordinates[neighborNode.index][0],
                            coordinates[neighborNode.index][1], coordinates[goal][0], coordinates[goal][1]);
                    neighborNode.parent = current;

                    if (!open.contains(neighborNode)) {
                        open.add(neighborNode);
                    }
                }
            }
        }
        return path;
    }

    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }
}