import java.util.PriorityQueue;

public class AlgorithmCompare {
    public static void main(String[] args) {
        AStar A = new AStar();
        Results r1, r2;
        IPathFinder Ae;
//
//        Graph graph = new Graph("more_graphs/test/adj.txt", "more_graphs/test/wadj.txt", "more_graphs/test/coords.txt");
//        Graph graph = new Graph("more_graphs/realistic_small/adj.txt", "more_graphs/realistic_small/wadj.txt", "more_graphs/realistic_small/coords.txt");
        Graph graph = new Graph("more_graphs/realistic_medium/adj.txt", "more_graphs/realistic_medium/wadj.txt", "more_graphs/realistic_medium/coord.txt");
//        Graph graph = new Graph("more_graphs/realistic_large/adj.txt", "more_graphs/realistic_large/wadj.txt", "more_graphs/realistic_large/coord.txt");
        double[] epsilonCandidates = {0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3};
//        double[] epsilonCandidates = {10000};
        for (double e : epsilonCandidates) {
            // Replace this with the algorithm you are comparing
            int numRuns = 0;
            double avgNodesVisitedPercent = 0;
            double avgPathLengthPercent = 0;
            double weirdPercent = 0;
            for (int i = 0; i < graph.adjacency_matrix.length; i++) {
//            for (int i = 3; i < 4; i++) {
                for (int j = 0; j < graph.adjacency_matrix.length; j++) {
//                for (int j = 8; j < 9; j++) {
                    if (i == j) continue;
                    Ae = new AStar(1, 5);
//                    Ae = new AStar(e, 5);
                    Ae = new AEpsilonStar(e, 5);
                    numRuns++;
                    r1 = A.findPath(graph, i, j);
                    r2 = Ae.findPath(graph, i, j);
//                    if (r1.getNumNodesExplored() < r2.getNumNodesExplored()) System.out.println(i + ", " + j);
                    double nodesVisitedPercent = ((double) r2.getNumNodesExplored() / r1.getNumNodesExplored());
                    avgNodesVisitedPercent += nodesVisitedPercent;
                    double pathLengthPercent = (r2.getPathLength() / r1.getPathLength());
                    avgPathLengthPercent += pathLengthPercent;

                    if (r2.getPathLength() == r1.getPathLength()) weirdPercent++;
                }
            }
            System.out.println("-----" + e + "-----");
            avgNodesVisitedPercent /= (numRuns);
            avgPathLengthPercent /= (numRuns);
            weirdPercent /= (numRuns);
            System.out.println("Average nodes visited compared to A*: " + (avgNodesVisitedPercent * 100) + "%");
            System.out.println("Average path length compared to A*: " + (avgPathLengthPercent * 100) + "%");
            System.out.println(weirdPercent);
            System.out.println();
        }
    }

    public static boolean verifyQueue(PriorityQueue<PathNode> pq) {
        PriorityQueue<PathNode> q = new PriorityQueue<>(pq);
        // Construct array out of queue
        PathNode[] arr = new PathNode[q.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = q.poll();
        }

        // Verify that the f(n) of an element is <= the elements after it
        for (int i = 0; i < arr.length - 1; i++) {
            PathNode n1 = arr[i];
            PathNode n2 = arr[i+1];
            double s1 = n1.gn + n1.hn;
            double s2 = n2.gn + n2.hn;
            if( s1 > s2) return false;
        }
        return true;
    }
}
