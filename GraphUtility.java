import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphUtility {

    // Use for testing
    // public static void main(String[] args) {
    // try {
    // int[][] coords = VertexCoordsFromfile(new
    // File("./graphs/simple/coords.txt"));
    // int[][] adjM = AdjMatrixFromFile(new File("./graphs/simple/adj.txt"));
    // double[][] wAdjM = WeightedAdjMatrixFromCoords(adjM, coords);
    // System.out.println();
    // } catch (FileNotFoundException e) {
    // System.out.println(e);
    // }
    // }

    // Get adjacency matrix from file
    public static int[][] AdjMatrixFromFile(File f) throws FileNotFoundException {
        Scanner rowReader = new Scanner(f);
        int rowIndex = 0;
        int[][] adjacencyMatrix = null;
        while (rowReader.hasNextLine()) {
            String row = rowReader.nextLine();
            // Sanitize the string
            row = row.replaceAll("[^\\d]", " ");
            // Get array of values in the row
            String[] vals = row.split(" +");
            // Use this to create the adjacency matrix
            if (adjacencyMatrix == null) {
                adjacencyMatrix = new int[vals.length][vals.length];
            }
            // Put values from file into matrix
            for (int i = 0; i < vals.length; i++) {
                adjacencyMatrix[rowIndex][i] = Integer.parseInt(vals[i]);
                // System.out.print(vals[i] + " ");
            }
            // Increment counter to check next row
            rowIndex++;
            // System.out.println();

        }
        rowReader.close();
        return adjacencyMatrix;
    }

    public static int[][] VertexCoordsFromfile(File f) throws FileNotFoundException {
        // Coordinate files will be in the following format:
        /*
         * N (where N = number of vertices)
         * X0 Y0
         * X1 Y1
         * ...
         * XN YN
         */
        Scanner rowReader = new Scanner(f);
        int numVertices = rowReader.nextInt();
        rowReader.nextLine();
        int[][] vertexCoords = new int[numVertices][2];
        // Read each row and pass the numbers into the coordinates array
        for (int i = 0; i < numVertices; i++) {
            String[] row = rowReader.nextLine().split(" ");
            vertexCoords[i][0] = Integer.parseInt(row[0]);
            vertexCoords[i][1] = Integer.parseInt(row[1]);
        }
        rowReader.close();
        return vertexCoords;
    }

    public static double[][] WeightedAdjMatrixFromCoords(int[][] adjacencyMatrix, int[][] coords) {
        double[][] wAdjMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    // Gets the distance between the points i and j
                    int dX = coords[i][0] - coords[j][0];
                    int dY = coords[i][1] - coords[j][1];
                    double distance = Math.sqrt((dX * dX) + (dY * dY));
                    wAdjMatrix[i][j] = distance;
                }
            }
        }
        return wAdjMatrix;
    }
}
