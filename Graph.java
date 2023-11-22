import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Graph {
    // Adjacency matrix representing vertices and edges.
    double[][] adjacency_matrix;

    // If only one file path is supplied then the resulting Graph will be unweighted.
    // adj_filepath -- a String representing the file path of an adj.txt. For example "./graphs/medium/adj.txt"
    public Graph(String adj_filepath) {
        File adj_file = new File(adj_filepath);

        try {
            double[][] adjacency_matrix = GraphUtility.AdjMatrixFromFile(adj_file);
            this.adjacency_matrix = adjacency_matrix;
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
    }

    // If two file paths are supplied the resulting Graph will be weighted.
    // adj_filepath -- a String representing the file path of an adj.txt. For example "./graphs/medium/adj.txt"
    // coord_filepath -- a String representing the file path of a coord.txt. For example "./graphs/medium/coord.txt"
    public Graph(String adj_filepath, String coord_filepath) {
        File adj_file = new File(adj_filepath);
        File coord_file = new File(coord_filepath);

        try {
            int[][] vertexCoords = GraphUtility.VertexCoordsFromfile(coord_file);
            double[][] adjacency_matrix = GraphUtility.AdjMatrixFromFile(adj_file);

            this.adjacency_matrix = GraphUtility.WeightedAdjMatrixFromCoords(adjacency_matrix, vertexCoords);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
    }

    // Build a map of pathnodes to maintain state values necessary throughout the path finding computation.
    // find_path() consumes nodes, it is no longer useful for subsequent runs.
    public HashMap<Integer, PathNode> buildNodes() {
        HashMap<Integer, PathNode> nodes = new HashMap<>();
        for (int i = 0; i < this.adjacency_matrix[0].length; i++) {
            nodes.put(i, new PathNode(i));
        }
        return nodes;
    }
}
