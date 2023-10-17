import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Test {
    public static void main(String args[]) {
        // Paths:
        String adjacencyMatrixSimplePath = "./../graphs/simple/adj.txt";
        String adjacencyMatrixMediumPath = "./../graphs/medium/adj.txt";
        String adjacencyMatrixDifficultPath = "./../graphs/difficult/adj.txt";
        String coordinatesSimplePath = "./../graphs/simple/coords.txt";
        String coordinatesMediumPath = "./../graphs/medium/coords.txt";
        String coordinatesDifficultPath = "./../graphs/difficult/coords.txt";

        int[][] adjacencyMatrixSimple;
        int[][] adjacencyMatrixMedium;
        int[][] adjacencyMatrixDifficult;
        int[][] coordinatesSimple;
        int[][] coordinatesMedium;
        int[][] coordinatesDifficult;

        try {
            adjacencyMatrixSimple = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixSimplePath));
            adjacencyMatrixMedium = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixMediumPath));
            adjacencyMatrixDifficult = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixDifficultPath));
            coordinatesSimple = GraphUtility.VertexCoordsFromfile(new File(coordinatesSimplePath));
            coordinatesMedium = GraphUtility.VertexCoordsFromfile(new File(coordinatesMediumPath));
            coordinatesDifficult = GraphUtility.VertexCoordsFromfile(new File(coordinatesDifficultPath));

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        }

        // Testing astar with h(n) = 0:
        
        // Testing astar with h(n) = distance(node, goal_node):
    }
}