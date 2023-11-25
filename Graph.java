import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Graph {
    // Adjacency matrix representing vertices and edges.
    double[][] adjacency_matrix;
    // Weighted adjacency matrix representing weighted vertices and edges.
    double[][] w_adjacency_matrix;
    // coordinates are stored in this form:
    // [[x1, y1],[x2,y2],[x3,y3],...[xn, yn]]
    double[][] coordinates;

    // adj_filepath -- a String representing the file path of an adj.txt. For example "./more_graphs/realistic_medium/adj.txt"
    // wadj_filepath -- a String representing the file path of a wadj.txt. For example "./more_graphs/realistic_medium/wadj.txt"
    // coord_filepath -- a String representing the file path of a coords.txt file. For example "./more_graphs/realistic_medium/coords.txt"
    public Graph(String adj_filepath, String wadj_filepath, String coord_filepath) {
        File adj_file = new File(adj_filepath);
        File wadj_file = new File(wadj_filepath);
        File coord_file = new File(coord_filepath);

        try {
            double[][] adjacency_matrix = GraphUtility.AdjMatrixFromFile(adj_file);
            this.adjacency_matrix = adjacency_matrix;

            double[][] w_adjacency_matrix = GraphUtility.AdjMatrixFromFile(wadj_file);
            this.w_adjacency_matrix = w_adjacency_matrix;

            double[][] coordinates = GraphUtility.VertexCoordsFromfile(coord_file);
            this.coordinates = coordinates;
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
    }

    // If two file paths are supplied the resulting Graph will be weighted.
    // adj_filepath -- a String representing the file path of an adj.txt. For example "./graphs/medium/adj.txt"
    // coord_filepath -- a String representing the file path of a coord.txt. For example "./graphs/medium/coord.txt"
    /* public Graph(String adj_filepath, String coord_filepath) {
        File adj_file = new File(adj_filepath);
        File coord_file = new File(coord_filepath);

        try {
            int[][] vertexCoords = GraphUtility.VertexCoordsFromfile(coord_file);
            double[][] adjacency_matrix = GraphUtility.AdjMatrixFromFile(adj_file);

            this.adjacency_matrix = GraphUtility.WeightedAdjMatrixFromCoords(adjacency_matrix, vertexCoords);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
    } */

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
