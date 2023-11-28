import java.nio.file.Path;
import java.util.*;

public class AEpsilonStar implements IPathFinder{
    double e;
    double multiplier; // TODO: Delete this after distance multiplier is selected
    public AEpsilonStar(double epsilon, int m) {
        this.e = epsilon;
        this.multiplier = m;
    }
    public AEpsilonStar() {
        this.e = 0;
    }

    // Estimates the computational effort of computing the solution from node n to the goal
    // Currently does so by returning the number of unvisited neighbors n has (bad method)
    private double FOCALHeuristicNeighbors(double[] adjRow, HashSet<Integer> closed) {
        int count = 0;
        for (int i = 0; i < adjRow.length; i++) {
            if(adjRow[i] != 0 && !closed.contains(i))
                count++;
        }
        return count;
    }

    private double FOCALHeuristicEuclidean(double[] nCoords, double[] tCoords) {
        double dx = nCoords[0] - tCoords[0];
        double dy = nCoords[1] - tCoords[1];
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private double FOCALHeuristicManhattan(double[] nCoords, double[] tCoords) {
        double dx = nCoords[0] - tCoords[0];
        double dy = nCoords[1] - tCoords[1];
        return Math.abs(dx) + Math.abs(dy);
    }

    private double FOCALHeuristicAngle(double[] cCoords, double[] nCoords, double[] tCoords) {
        double[] vCN = {
                nCoords[0] - cCoords[0],
                nCoords[1] - cCoords[1]
        };
        double[] vCT = {
                tCoords[0] - cCoords[0],
                tCoords[1] - cCoords[1]
        };
        double dot = (vCN[0] * vCT[0]) + (vCN[1] * vCT[1]);
        double magCN = Math.sqrt((vCN[0] * vCN[0]) + (vCN[1] * vCN[1]));
        double magCT = Math.sqrt((vCT[0] * vCT[0]) + (vCT[1] * vCT[1]));
        double cosAngle = dot/(magCN * magCT);
        return Math.acos(cosAngle);
    }

    private double FOCALHeuristicIndex(int nIndex, int tIndex) {
        return Math.abs(nIndex - tIndex);
    }
    @Override
    public Results findPath(Graph g, int start, int goal) {
        HashMap<Integer, PathNode> nodes = g.buildNodes();
        HashSet<Integer> closed = new HashSet<>(); // Keeps track of visited nodes
        PriorityQueue<PathNode> open = new PriorityQueue<>((o1, o2) -> Double.compare(o1.gn + o1.hn, o2.gn + o2.hn));
//        PriorityQueue<PathNode> focal = new PriorityQueue<>((o1, o2) -> Double.compare(
//                FOCALHeuristicNeighbors(g.adjacency_matrix[o1.index], closed), FOCALHeuristicNeighbors(g.adjacency_matrix[o2.index], closed)
//                FOCALHeuristicEuclidean(g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicEuclidean(g.coordinates[o2.index], g.coordinates[goal])
//                FOCALHeuristicManhattan(g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicEuclidean(g.coordinates[o2.index], g.coordinates[goal])
//                FOCALHeuristicAngle(g.coordinates[o1.parent.index], g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicAngle(g.coordinates[o2.parent.index], g.coordinates[o2.index], g.coordinates[goal])
//                FOCALHeuristicIndex(o1.index, goal), FOCALHeuristicIndex(o2.index, goal)
//        ));
        PriorityQueue<PathNode> focal = new PriorityQueue<>(new Comparator<PathNode>() { // Should handle ties better by ranking tied nodes using Euclidean distance
            @Override
            public int compare(PathNode o1, PathNode o2) {
                int c = Double.compare(
//                    FOCALHeuristicNeighbors(g.adjacency_matrix[o1.index], closed), FOCALHeuristicNeighbors(g.adjacency_matrix[o2.index], closed)
//                    FOCALHeuristicEuclidean(g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicEuclidean(g.coordinates[o2.index], g.coordinates[goal])
//                    FOCALHeuristicManhattan(g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicEuclidean(g.coordinates[o2.index], g.coordinates[goal])
                    FOCALHeuristicAngle(g.coordinates[o1.parent.index], g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicAngle(g.coordinates[o2.parent.index], g.coordinates[o2.index], g.coordinates[goal])
//                    FOCALHeuristicIndex(o1.index, goal), FOCALHeuristicIndex(o2.index, goal)
                );
                if (c == 0) // In the case of a tie
                {
                    return Double.compare(FOCALHeuristicEuclidean(g.coordinates[o1.index], g.coordinates[goal]), FOCALHeuristicEuclidean(g.coordinates[o2.index], g.coordinates[goal]));
                } else return c;
            }
        });
        open.add(nodes.get(start));
        PathNode goalNode = nodes.get(goal);

        // The results
        ArrayList<Integer> path = new ArrayList<>(); // The generated path
        int numNodesExplored = 0;
        double pathLength = 0;

        while (!open.isEmpty()) {
            PathNode current;
            // Obtain the node with the lowest f(n) (or the lowest hf in the FOCAL list if there are any)
            if (focal.isEmpty()) {
                current = open.poll();
            } else {
                current = focal.poll();
                open.remove(current);
            }
            closed.add(current.index); // Mark the current node as explored

            if (current.index == goal) { // We have reached the goal node.
                PathNode child = goalNode;
                double _pathLength = 0;
                while (child.index != start) { // Construct path by moving through the chain of parents.
                    _pathLength += g.w_adjacency_matrix[child.index][child.parent.index];
                    path.add(child.index);
                    child = child.parent;
                }
                path.add(start);
                numNodesExplored = closed.size();
                pathLength = _pathLength;
                return new Results(path, numNodesExplored, pathLength); // Stop searching
            }

            // Get the neighbors of the current node
            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int i = 0; i < g.adjacency_matrix[0].length; i++) {
                if (g.adjacency_matrix[current.index][i] > 0)
                    neighbors.add(i); // True if there exists an edge between current node and node with index i
            }

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
                    double dist = getDistance(x1, y1, x2, y2);
                    neighborNode.hn = getDistance(x1, y1, x2, y2);

                    // Update neighbors parent (we reach neighbor through current).
                    neighborNode.parent = current;

                    // This pattern of removing then adding will force the PriorityQueue class to update the position of the node in the queue, or just add the node if it isn't already in the queue
                    open.remove(neighborNode);
                    open.add(neighborNode);

                }
            }
            if(!open.isEmpty()) {
                // For all the newly opened nodes, check if they can be added to FOCAL. This is done in a separate loop so that all the neighbors can be generated and added to OPEN first
                for (int neighbor : neighbors) {
                    if (closed.contains(neighbor))
                        continue;
                    PathNode neighborNode = nodes.get(neighbor);
                    PathNode smallestfnNode = open.peek();
                    double neighborfn = neighborNode.gn + neighborNode.hn;
                    double smallestfn = smallestfnNode.gn + smallestfnNode.hn;
                    // If this node's f(n) deviates from the node with the lowest f(n) by a factor less than (1 + e), then it is "similar" to the node with the lowest f(n) and is added to FOCAL
                    if (neighborfn <= (1 + e) * smallestfn) {
                        // This pattern of removing then adding will force the PriorityQueue class to update the position of the node in the queue, or just add the node if it isn't already in the queue
                        focal.remove(neighborNode);
                        focal.add(neighborNode);

                    }
                }
            }
        }
        return null;
    }

    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1))) * Math.pow(10, multiplier);
    }
}
