

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.HashSet;

public class DynamicWeightingAStar implements IPathFinder {
    private double epsilon; // Base ε factor for heuristic weighting
    private Graph graph;

    public DynamicWeightingAStar(double epsilon, Graph graph) {
        this.epsilon = epsilon;
        this.graph = graph;
    }

    public Results findPath(Graph graph, int startNode, int goalNode) {
        HashMap<Integer, PathNode> nodes = graph.buildNodes();
        PriorityQueue<PathNode> openSet = new PriorityQueue<>(
                (node1, node2) -> Double.compare(node1.getCost() + dynamicWeight(node1) * heuristic(node1, goalNode, epsilon),
                        node2.getCost() + dynamicWeight(node2) * heuristic(node2, goalNode, epsilon)));

        HashSet<Integer> closedSet = new HashSet<>();
        PathNode start = nodes.get(startNode);
        start.gn = 0;
        start.hn = heuristic(start, goalNode, epsilon);
        start.depth = 0; // Initialize depth of start node to 0
        openSet.add(start);

        int nodesVisited = 0;

        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();
            if (current.index == goalNode) {
                return reconstructPath(nodes, goalNode, nodesVisited);
            }
            closedSet.add(current.index);
            nodesVisited++;

            for (Integer neighborId : getNeighbors(current.index)) {
                if (closedSet.contains(neighborId))
                    continue;
                PathNode neighbor = nodes.get(neighborId);
                double tentativeGScore = current.gn + graph.adjacency_matrix[current.index][neighborId];

                if (tentativeGScore < neighbor.gn) {
                    neighbor.gn = tentativeGScore;
                    neighbor.hn = heuristic(neighbor, goalNode, epsilon);
                    neighbor.parent = current;
                    neighbor.depth = current.depth + 1;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return new Results(); 
    }

    private double dynamicWeight(PathNode node) {
        int totalNodes = graph.getNodeCount();
        return 1 + Math.exp(-Math.E * node.depth / totalNodes);
    }

    private double heuristic(PathNode node, int goalNode, double epsilon) {
        double[] nodeCoords = this.graph.coordinates[node.getId()];
        double[] goalCoords = this.graph.coordinates[goalNode];
        return Math.sqrt(Math.pow(nodeCoords[0] - goalCoords[0], 2) + Math.pow(nodeCoords[1] - goalCoords[1], 2)) * epsilon;
    }    

    private Results reconstructPath(HashMap<Integer, PathNode> nodes, int goalNode, int nodesVisited) {
        ArrayList<Integer> path = new ArrayList<>();
        PathNode current = nodes.get(goalNode);
        double distance = 0.0;

        while (current != null && current.parent != null) {
            path.add(0, current.index);
            distance += this.graph.w_adjacency_matrix[current.index][current.parent.index];
            current = current.parent;
        }
        if (current != null) {
            path.add(0, current.index);
        }

        return new Results(path, nodesVisited, distance);
    }

    private ArrayList<Integer> getNeighbors(int nodeIndex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < graph.adjacency_matrix[nodeIndex].length; i++) {
            if (this.graph.adjacency_matrix[nodeIndex][i] != 0) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }
}
