import java.util.HashMap;
import java.util.ArrayList;

public class Test {
    public static void main(String args[]) {
        int[][] adjacency_matrix = {
            { 0, 20,  0, 70,  0},
            {20,  0, 25,  0,  0},
            { 0, 25,  0, 30,  0},
            {70,  0, 30,  0, 15},
            { 0,  0,  0, 15,  0}
        };
        HashMap<Integer, PathNode> nodes = new HashMap<>();
        PathNode n0 = new PathNode(0);
        PathNode n1 = new PathNode(1);
        PathNode n2 = new PathNode(2);
        PathNode n3 = new PathNode(3);
        PathNode n4 = new PathNode(4);
        nodes.put(0, n0);
        nodes.put(1, n1);
        nodes.put(2, n2);
        nodes.put(3, n3);
        nodes.put(4, n4);

        PathFinder pathFinder = new PathFinder(adjacency_matrix, nodes);

        long startTime = System.nanoTime();
        ArrayList<Integer> foundPath = pathFinder.findPath(0, 4);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        // This method of calculating elapsed time is not accurate. We should use some benchmarking library instead.
        System.out.println("Elapsed time: " + elapsedTime + " ns"); 

        // We could use Dijkstra's to find a guarenteed shortest path and compare it with the path A* found.
        for (int index : foundPath) {
            System.out.print(index + " ");
        }
        System.out.println();
    }
}