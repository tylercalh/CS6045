import java.util.HashMap;
import java.util.ArrayList;

public class Test {
    public static void main(String args[]) {
        int[][] adjacency_matrix = {
            { 0, 20,  0, 80,  0},
            {20,  0, 25,  0,  0},
            { 0, 25,  0, 30,  0},
            {80,  0, 30,  0, 15},
            { 0,  0,  0, 15,  0}
        };

        PathFinder pathFinder = new PathFinder(adjacency_matrix);

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