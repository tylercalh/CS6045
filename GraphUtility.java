import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphUtility {

    // Get adjacency matrix from file
    public static int[][] AdjMatrixFromFile(File f) throws FileNotFoundException {
        Scanner rowReader;
        rowReader = new Scanner(f);
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
            // Put values from
            for (int i = 0; i < vals.length; i++) {
                adjacencyMatrix[rowIndex][i] = Integer.parseInt(vals[i]);
                System.out.print(vals[i] + " ");
            }
            // Increment counter to check next row
            rowIndex++;
            System.out.println();

        }
        rowReader.close();
        return adjacencyMatrix;

    }
}
