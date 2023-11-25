public class AlgorithmCompare {
    public static void main(String[] args) {
        AStar A = new AStar();
        double numSuccessA;
        int numNodes;
        Results r1, r2;
        Graph graph = new Graph("more_graphs/realistic_small/adj.txt", "more_graphs/realistic_small/wadj.txt", "more_graphs/realistic_small/coords.txt");
        double[] epsilonCandidates = {0, 0.25, 0.5, 0.75, 1};
        for (double e : epsilonCandidates) {
            // Replace this with the algorithm you are comparing
             AEpsilonStar Ae = new AEpsilonStar(e);
            numSuccessA = 0;
            numNodes = graph.adjacency_matrix.length;
            int numRuns = 0;
            double avgNodesVisitedPercent = 0;
            double avgPathLengthPercent = 0;
//            r1 = A.findPath(graph, 0, 3);
//            r2 = Ae.findPath(graph, 0, 3);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == j) continue;
                    numRuns++;
                    r1 = A.findPath(graph, i, j);
                    r2 = Ae.findPath(graph, i, j);
                    double nodesVisitedPercent = ((double) r2.getNumNodesExplored() / r1.getNumNodesExplored());
                    avgNodesVisitedPercent += nodesVisitedPercent;
                    double pathLengthPercent = (r2.getPathLength() / r1.getPathLength());
                    avgPathLengthPercent += pathLengthPercent;
                }
            }
            avgNodesVisitedPercent /= (numRuns);
            avgPathLengthPercent /= (numRuns);
            System.out.println("Average nodes visited compared to A*: " + (avgNodesVisitedPercent * 100) + "%");
            System.out.println("Average path length compared to A*: " + (avgPathLengthPercent * 100) + "%");
            System.out.println();
        }
    }
}
