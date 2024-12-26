package TaskA;

import TaskA.directions.*;

import java.util.*;

public class MainSolutionA {
    // GRID-LIKE Graph

    private static final char ROVER_SYMBOL = 'R';
    private static final char TARGET_SYMBOL = 'x';
    private static final Set<Character> OBSTACLES_SET = Set.of('/', '\\', '|', '_', '*');

    private static final DirectionUp DIRECTION_UP = new DirectionUp();
    private static final DirectionDown DIRECTION_DOWN = new DirectionDown();
    private static final DirectionLeft DIRECTION_LEFT = new DirectionLeft();
    private static final DirectionRight DIRECTION_RIGHT = new DirectionRight();

    private static Node[][] matrix;
    private static Node rover;
    private static Node target;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            String userInput = input.nextLine();

            if (userInput.equals("quit")) {
                break;
            }

            String[] splitInput = userInput.split(" ");
            String command = splitInput[0];

            if (command.equalsIgnoreCase("new")) {
                int cols = Integer.parseInt(splitInput[1]);
                int rows = Integer.parseInt(splitInput[2]);

                matrix = readMatrix(rows, cols);
                printMatrix(rows, cols);
                setEachNodeAdjacencyList(rows, cols);
            }
            else if (command.equalsIgnoreCase("up")) {
                System.out.println("up");
            } else if (command.equalsIgnoreCase("down")) {
                System.out.println("down");
            } else if (command.equalsIgnoreCase("left")) {
                System.out.println("left");
            } else if (command.equalsIgnoreCase("right")) {
                System.out.println("right");
            } else if (command.equalsIgnoreCase("debug")) {
                System.out.println("debug");
            } else if (command.equalsIgnoreCase("path")) {
                BFS();
            } else if (command.equalsIgnoreCase("debug-path")) {
                System.out.println("debug-path");
            }
        }
    }

    public static Node[][] readMatrix(int rows, int cols) {
        Scanner input = new Scanner(System.in);
        Node[][] matrix = new Node[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] currentRow = input.nextLine().toCharArray();

            for (int j = 0; j < cols; j++) {
                char symbol = currentRow[j];
                Node currentNode = new Node(symbol);

                if (symbol == ROVER_SYMBOL) {
                    rover = currentNode;
                } else if (symbol == TARGET_SYMBOL) {
                    target = currentNode;
                }

                matrix[i][j] = currentNode;
            }
        }

        return matrix;
    }

    public static void printMatrix(int rows, int cols) {
        System.out.println("Matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
    }

    public static void setEachNodeAdjacencyList(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            Node[] currentRow = matrix[i];

            for (int j = 0; j < cols; j++) {
                Node currentNode = currentRow[j];
                Node upNode, downNode, leftNode, rightNode;

                // Up Node to the current Node
                if (i == 0) {
                    upNode = null;
                } else {
                    upNode = matrix[i - 1][j];
                }

                // Down Node to the current Node
                if (i == rows - 1) {
                    downNode = null;
                } else {
                    downNode = matrix[i + 1][j];
                }

                // Right Node to the current Node
                if (j == cols - 1) {
                    rightNode = null;
                } else {
                    rightNode = matrix[i][j + 1];
                }

                // Left Node to the current Node
                if (j == 0) {
                    leftNode = null;
                } else {
                    leftNode = matrix[i][j - 1];
                }

                // Create the links to current node
                Edge upEdge = new Edge(currentNode, upNode, DIRECTION_UP);
                Edge downEdge = new Edge(currentNode, downNode, DIRECTION_DOWN);
                Edge rightEdge = new Edge(currentNode, rightNode, DIRECTION_RIGHT);
                Edge leftEdge = new Edge(currentNode, leftNode, DIRECTION_LEFT);

                Edge[] currentNodeAdjacencyList = new Edge[]{ upEdge, downEdge, rightEdge, leftEdge };
                currentNode.setAdjacencyList(currentNodeAdjacencyList);
            }
        }
    }

    public static void BFS() {
        // The algorithm explores each node at degree 1, then 2, then 3, ...
        // It doesn't add the nodes that have symbols in the obstacles set

        Queue<List<Node>> queue = new LinkedList<>();
        List<Node> initialPath = new ArrayList<>();
        initialPath.add(rover);
        queue.add(initialPath);
        rover.setVisited(true);

        List<List<Node>> allPaths = new ArrayList<>();

        while (!queue.isEmpty()) {
            List<Node> currentPath = queue.poll();
            Node currentNode = currentPath.get(currentPath.size() - 1); // Get the last node of the path

            // If we reach the target, add the current path to the list of all paths
            if (currentNode == target) {
                allPaths.add(new ArrayList<>(currentPath)); // Store the found path
                System.out.println("Found path to target: " + currentPath);
            }

            // Explore all adjacent nodes
            for (Edge incidentEdge : currentNode.getAdjacencyList()) {
                Node nextNode = incidentEdge.getEndNode();

                // Check if the next node is not visited and not an obstacle
                if (nextNode != null && !nextNode.getVisited() && !OBSTACLES_SET.contains(nextNode.getSymbol())) {
                    nextNode.setVisited(true); // Mark node as visited

                    // Create a new path by adding the next node to the current path
                    List<Node> newPath = new ArrayList<>(currentPath);
                    newPath.add(nextNode);

                    queue.add(newPath);
                }
            }
        }

        // After BFS, print all found distinct paths
        if (allPaths.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("All distinct paths found:");
            for (List<Node> path : allPaths) {
                System.out.println(path);
            }
        }
    }
}
