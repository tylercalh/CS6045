import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Analysis {
    public static void main(String[] args) throws IOException {
        System.out.println("Overwrite existing result files? Y/N");
        String response = new Scanner(System.in).nextLine();
        boolean append = true;
        if (response.equalsIgnoreCase("Y")) {
            append = false;
        }
        // Graphs
        Graph graph_test = new Graph("more_graphs/test/adj.txt", "more_graphs/test/wadj.txt", "more_graphs/test/coords.txt");
        Graph graph_small = new Graph("more_graphs/realistic_small/adj.txt", "more_graphs/realistic_small/wadj.txt", "more_graphs/realistic_small/coords.txt");
        Graph graph_medium = new Graph("more_graphs/realistic_medium/adj.txt", "more_graphs/realistic_medium/wadj.txt", "more_graphs/realistic_medium/coord.txt");
        Graph graph_large = new Graph("more_graphs/realistic_large/adj.txt", "more_graphs/realistic_large/wadj.txt", "more_graphs/realistic_large/coord.txt");
        Graph[] graphs = {graph_small, graph_medium, graph_large};
//        Graph[] graphs = {graph_small};
        // Pathfinding Algorithms
        //TODO: Add dynamic weighted A*
        IPathFinder aStar, wAStar, aEStar;
        Results aStar_results, wAStar_results, aEStar_results;
        double[] epsilonCandidates = {1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75, 4};
//        double[] epsilonCandidates = {3, 3.25, 3.5, 3.75, 4};

        // Standard measurements
        final int maxLoops = 100;
        int numRuns = 0;
        double aStar_totalNodesVisitedPercent = 0, wAStar_totalNodesVisitedPercent = 0, aEStar_totalNodesVisitedPercent = 0;
        double aStar_totalPathLengthPercent = 0, wAStar_totalPathLengthPercent = 0, aEStar_totalPathLengthPercent = 0;

        String aStar_str, wAStar_str, aEStar_str;

        BufferedWriter aStar_writer = new BufferedWriter(new FileWriter("results/AStar.txt", append));
        BufferedWriter wAStar_writer = new BufferedWriter(new FileWriter("results/WeightedAStar.txt", append));
        BufferedWriter aEStar_writer = new BufferedWriter(new FileWriter("results/AEpsilonStar.txt", append));

        for (int k = 0; k < graphs.length; k++) {
            Graph g = graphs[k];
            System.out.println("Graph " + (k + 1));
            wAStar_str = "Graph " + (k + 1) + "\n";
            aEStar_str = "Graph " + (k + 1) + "\n";
            for (double e: epsilonCandidates) {
                System.out.println(e);
                aStar_totalNodesVisitedPercent = 0;
                wAStar_totalNodesVisitedPercent = 0;
                aEStar_totalNodesVisitedPercent = 0;
                aStar_totalPathLengthPercent = 0;
                wAStar_totalPathLengthPercent = 0;
                aEStar_totalPathLengthPercent = 0;
                numRuns = 0;
                for (int i = 0; i < Math.min(g.adjacency_matrix.length, maxLoops); i++) {
                    for (int j = 0; j < Math.min(g.adjacency_matrix.length, maxLoops); j++) {
                        // Select two random nodes
                        int start = 0, goal = 0;
                        while (start == goal) {
                            start = new Random().nextInt(g.adjacency_matrix.length);
                            goal = new Random().nextInt(g.adjacency_matrix.length);
                        }

                        aStar = new AStar(1, 4);
                        wAStar = new AStar(e, 5);
                        aEStar = new AEpsilonStar(e, 5);
                        numRuns++;

                        aStar_results = aStar.findPath(g, start, goal);
                        wAStar_results = wAStar.findPath(g, start, goal);
                        aEStar_results = aEStar.findPath(g, start, goal);

                        wAStar_totalNodesVisitedPercent += (double) wAStar_results.getNumNodesExplored() / aStar_results.getNumNodesExplored();
                        aEStar_totalNodesVisitedPercent += (double) aEStar_results.getNumNodesExplored() / aStar_results.getNumNodesExplored();

                        wAStar_totalPathLengthPercent += wAStar_results.getPathLength() / aStar_results.getPathLength();
                        aEStar_totalPathLengthPercent += aEStar_results.getPathLength() / aStar_results.getPathLength();
                    }
                }
                // TODO: Output results by appending to files
                wAStar_str += "---" + e + "---\n" +
                        "Average nodes visited compared to A*: " + (wAStar_totalNodesVisitedPercent / numRuns) + "\n" +
                        "Average path length compared to A*: " + (wAStar_totalPathLengthPercent / numRuns) + "\n";

                aEStar_str += "---" + e + "---\n" +
                        "Average nodes visited compared to A*: " + (aEStar_totalNodesVisitedPercent / numRuns) + "\n" +
                        "Average path length compared to A*: " + (aEStar_totalPathLengthPercent / numRuns) + "\n";

            }
            System.out.println("Weighted A*:");
            System.out.println(wAStar_str);
            wAStar_writer.write(wAStar_str);

            System.out.println("AEpsilon*:");
            System.out.println(aEStar_str);
            aEStar_writer.write(aEStar_str);
            System.out.println();
        }

        wAStar_writer.close();
        aEStar_writer.close();
    }
}
