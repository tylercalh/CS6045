import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.PriorityQueue;

// An implmentation of A* that uses euclidean distance as its heuristic.
public class AStar implements IPathFinder {
    public double weight;

    public AStar() {
        this.weight = 1.0;
    }

    // Constructor with specified weight.
    // When weight is 0.0 A* works similarly to Dijkstra's.
    // When weight is 1.0 A* takes h(n) into account.
    // When weight is greater than 1.0 h(n) begins to dominate g(n).
    public AStar(double weight) {
        this.weight = weight;
    }

    public Results findPath(Graph g, int start, int goal) {
        // First we have to build some path nodes to maintain node states as the algorithm runs.
        HashMap<Integer, PathNode> nodes = g.buildNodes();
        // Set up our data structures:
        PriorityQueue<PathNode> open = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
        HashSet<Integer> closed = new HashSet<>();
        ArrayList<Integer> path = new ArrayList<>();
        PathNode goalNode = nodes.get(goal);
        // Create default results.
        Results res = new Results();
        
        // Add the start node to the open list. Evalute nodes while there are nodes in open.
        open.add(nodes.get(start));
        while (open.size() > 0) {
            // Get the node with the lowest f(n) = g(n) + h(n).
            PathNode current = open.poll();
            closed.add(current.index);

            // If our current node is the goal node we have found a path.
            if (current.index == goal) {
                // Sum the distance between each child and its parent.
                double distance = 0.0;

                // Move through the child parent chain from the goal node back to the start node.
                PathNode child = goalNode;
                while (child.index != start) {
                    path.add(child.index);
                    distance += g.w_adjacency_matrix[child.index][child.parent.index];
                    child = child.parent;
                }
                path.add(start);

                // Update and return our results.
                res.setPath(path);
                res.setNumNodesExplored(closed.size());
                res.setPathLength(distance);
                return res;
            }

            // Build a list of neighbor nodes that can be accessed from our current node.
            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < g.adjacency_matrix[0].length; i++) {
                if (g.adjacency_matrix[current.index][i] > 0)
                    neighbors.add(i);
            }

            // Iterate over neighbors, updating values.
            for (int neighbor : neighbors) {
                // Skip neighbors that have already been examined.
                if (closed.contains(neighbor)) {
                    continue;
                }
                PathNode neighborNode = nodes.get(neighbor);

                double currentgn = (current.gn == Double.MAX_VALUE) ? 0 : current.gn;
                double neighborgn = (neighborNode.gn == Double.MAX_VALUE) ? 0 : neighborNode.gn;
                
                // Calculate distance from our current node to the neighboer we are looking at.
                double x1, y1, x2, y2;
                x1 = g.coordinates[current.index][0];
                y1 = g.coordinates[current.index][1];
                x2 = g.coordinates[neighborNode.index][0];
                y2 = g.coordinates[neighborNode.index][1];
                // The movenet cost to the neighbor is our current g(n) + distance(current, neighbor).
                double movementCost = currentgn + g.w_adjacency_matrix[current.index][neighbor];

                // We need to update the neighbor nodes values if any of the following conditions are met:
                // 1. movementCost < neighborgn: We have previously looked at a path to the neighbor, but now we have found a shorter one.
                // 2. neighborNode.gn == MAX: We havent previously looked at a path to the neighbor.
                // 3. open does not contain the neighbor.
                if (movementCost < neighborgn || neighborNode.gn == Double.MAX_VALUE || !open.contains(neighborNode)) {
                    neighborNode.gn = movementCost;

                    // Calculate h(n) with euclidean distance between neighbor node and goal node.
                    x1 = g.coordinates[neighborNode.index][0];
                    y1 = g.coordinates[neighborNode.index][1];
                    x2 = g.coordinates[goal][0];
                    y2 = g.coordinates[goal][1];
                    // Multiply h(n) by weight.
                    neighborNode.hn = this.weight * getDistance(x1, y1, x2, y2);

                    // Update neighbors parent (we reach neighbor through current).
                    neighborNode.parent = current;

                    // Add the neighbor node to open if it isn't in it already.
                    if (!open.contains(neighborNode)) {
                        open.add(neighborNode);
                    }
                }
            }
        }

        // If this return is reached then something went wrong. Default reseult values will return.
        return res;
    }

    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }
}
