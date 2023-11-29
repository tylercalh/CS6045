import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Test {
    public static void main(String args[]) {
        // Paths:
        String adjacencyMatrixSimplePath = "./graphs/simple/adj.txt";
        String adjacencyMatrixMediumPath = "./graphs/medium/adj.txt";
        String adjacencyMatrixDifficultPath = "./graphs/difficult/adj.txt";
        String coordinatesSimplePath = "./graphs/simple/coords.txt";
        String coordinatesMediumPath = "./graphs/medium/coords.txt";
        String coordinatesDifficultPath = "./graphs/difficult/coords.txt";

        String wAdjacencyMatrixSmallPathDynamic = "./more_graphs/realistic_small/wadj.txt";
        String adjacencyMatrixSmallPathDynamic = "./more_graphs/realistic_small/adj.txt";
        String coordinatesSmallPathDynamic = "./more_graphs/realistic_small/coords.txt";
        String wAdjacencyMatrixMediumPathDynamic = "./more_graphs/realistic_medium/wadj.txt";
        String adjacencyMatrixMediumPathDynamic = "./more_graphs/realistic_medium/adj.txt";
        String coordinatesMediumPathDynamic = "./more_graphs/realistic_medium/coord.txt";
        String wAdjacencyMatrixLargePathDynamic = "./more_graphs/realistic_large/wadj.txt";
        String adjacencyMatrixLargePathDynamic = "./more_graphs/realistic_large/adj.txt";
        String coordinatesLargePathDynamic = "./more_graphs/realistic_large/coord.txt";

        double[][] adjacencyMatrixSimple;
        double[][] adjacencyMatrixMedium;
        double[][] adjacencyMatrixDifficult;
        double[][] coordinatesSimple;
        double[][] coordinatesMedium;
        double[][] coordinatesDifficult;

        double[][] wAdjacencyMatrixSimple;
        double[][] wAdjacencyMatrixMedium;
        double[][] wAdjacencyMatrixDifficult;

        // Load data from files:
        try {
            adjacencyMatrixSimple = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixSimplePath));
            adjacencyMatrixMedium = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixMediumPath));
            adjacencyMatrixDifficult = GraphUtility.AdjMatrixFromFile(new File(adjacencyMatrixDifficultPath));
            coordinatesSimple = GraphUtility.VertexCoordsFromfile(new File(coordinatesSimplePath));
            coordinatesMedium = GraphUtility.VertexCoordsFromfile(new File(coordinatesMediumPath));
            coordinatesDifficult = GraphUtility.VertexCoordsFromfile(new File(coordinatesDifficultPath));

            wAdjacencyMatrixSimple = GraphUtility.WeightedAdjMatrixFromCoords(adjacencyMatrixSimple, coordinatesSimple);
            wAdjacencyMatrixMedium = GraphUtility.WeightedAdjMatrixFromCoords(adjacencyMatrixMedium, coordinatesMedium);
            wAdjacencyMatrixDifficult = GraphUtility.WeightedAdjMatrixFromCoords(adjacencyMatrixDifficult,
                    coordinatesDifficult);

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            return;
        }

        // Testing astar with h(n) = 0:
        // Create PathFinders:
        PathFinder pathFinderSimple = new PathFinder(adjacencyMatrixSimple);
        PathFinder pathFinderMedium = new PathFinder(adjacencyMatrixMedium);
        PathFinder pathFinderDifficult = new PathFinder(adjacencyMatrixDifficult);

        ArrayList<Integer> foundPath;
        long startTime;
        long endTime;
        long duration;
        System.out.println("Testing A* with no heuristic:\n");

        startTime = System.nanoTime();
        foundPath = pathFinderSimple.findPath(0, 5);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderSimple.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        startTime = System.nanoTime();
        foundPath = pathFinderMedium.findPath(0, 11);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderMedium.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        startTime = System.nanoTime();
        foundPath = pathFinderDifficult.findPath(0, 14);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderDifficult.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        // Testing astar with h(n) = distance(node, goal_node):
        // Create PathFinders:
        pathFinderSimple = new PathFinder(wAdjacencyMatrixSimple);
        pathFinderMedium = new PathFinder(wAdjacencyMatrixMedium);
        pathFinderDifficult = new PathFinder(wAdjacencyMatrixDifficult);

        System.out.println("Testing A* with euclidean distance heuristic:\n");

        startTime = System.nanoTime();
        foundPath = pathFinderSimple.findPath(0, 5, coordinatesSimple);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderSimple.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        startTime = System.nanoTime();
        foundPath = pathFinderMedium.findPath(0, 11, coordinatesMedium);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderMedium.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        startTime = System.nanoTime();
        foundPath = pathFinderDifficult.findPath(0, 15, coordinatesDifficult);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printPath(foundPath);
        System.out.println("Nodes visited: " + pathFinderDifficult.nodesVisited);
        System.out.println("Duration: " + duration + " ns\n");

        System.out.println("Testing DynamicWeightingAStar:\n");

        double epsilon = 1.0; 

        pathFinderSimple = new PathFinder(adjacencyMatrixSimple);
        pathFinderMedium = new PathFinder(adjacencyMatrixMedium);
        pathFinderDifficult = new PathFinder(adjacencyMatrixDifficult);
        Results pathFound;
        startTime = System.nanoTime();
        pathFound = pathFinderSimple.findPath(0, 5, coordinatesSimple, epsilon);
        endTime = System.nanoTime();
        printResults(pathFound, endTime-startTime);

        startTime = System.nanoTime();
        pathFound = pathFinderMedium.findPath(0, 11, coordinatesMedium, epsilon);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printResults(pathFound, duration);

        startTime = System.nanoTime();
        pathFound = pathFinderDifficult.findPath(0, 11, coordinatesDifficult, epsilon);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        printResults(pathFound, duration);
        
    }


    private static void printPath(ArrayList<Integer> path) {
        System.out.print("Path: ");
        for (int index : path) {
            System.out.print(index + " ");
        }
        System.out.println();
    }

    private static void printResults(Results results, long duration) {
        ArrayList<Integer> path = results.getPath();
        if (path != null) {
            System.out.print("Path: ");
            for (int index : path) {
                System.out.print(index + " ");
            }
            System.out.println();
        } else {
            System.out.println("No path found.");
        }
        System.out.println("Nodes visited: " + results.getNumNodesExplored());
        System.out.println("Path length: " + results.getPathLength());
        System.out.println("Duration: " + duration + " ns\n");
    }
    

}