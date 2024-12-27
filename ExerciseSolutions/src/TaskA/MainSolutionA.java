package TaskA;

import TaskA.directions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainSolutionA {
    // GRID-LIKE Graph

    private static final char ROVER_SYMBOL = 'R';
    private static final char TARGET_SYMBOL = 'x';
    private static final Set<Character> OBSTACLES_SET = Set.of('/', '\\', '|', '_', '*');
    private static final Set<Character> ALLOWED_SYMBOLS = Stream.concat(
            OBSTACLES_SET.stream(),
            Stream.of(ROVER_SYMBOL, TARGET_SYMBOL, ' ')
    ).collect(Collectors.toSet());

    private static final DirectionUp DIRECTION_UP = new DirectionUp();
    private static final DirectionDown DIRECTION_DOWN = new DirectionDown();
    private static final DirectionLeft DIRECTION_LEFT = new DirectionLeft();
    private static final DirectionRight DIRECTION_RIGHT = new DirectionRight();

    private static Node[][] matrix;
    private static Node target;
    private static int matrixRows;
    private static int matrixCols;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Node rover = null;

        while (true) {
            String userInput = input.nextLine();

            if (userInput.equals("quit")) {
                break;
            }

            String[] splitInput = userInput.split(" ");
            String command = splitInput[0];

            if (command.equalsIgnoreCase("new")) {
                matrixCols = Integer.parseInt(splitInput[1]);
                matrixRows = Integer.parseInt(splitInput[2]);

                Object[] result = readMatrix(matrixRows, matrixCols);

                if (result[0] == null || result[1] == null) {
                    continue;
                }

                matrix = (Node[][]) result[0];
                rover = (Node) result[1];

                printMatrix(matrixRows, matrixCols);
                setEachNodeAdjacencyList(matrixRows, matrixCols);
            }
            else if (command.equalsIgnoreCase("up")
                    || command.equalsIgnoreCase("down")
                    || command.equalsIgnoreCase("left")
                    || command.equalsIgnoreCase("right")) {

                if (rover == null) {
                    System.out.println("Rover not initialised!");
                    return;
                }

                int count = 1;
                if (splitInput.length > 1) {
                    count = Integer.parseInt(splitInput[1]);
                }

                for (int i = 0; i < count; i++) {
                    Node neighbourNode = null;

                    if (command.equalsIgnoreCase("up")) {
                        neighbourNode = rover.findNodeByDirectionName("up");
                    } else if (command.equalsIgnoreCase("down")) {
                        neighbourNode = rover.findNodeByDirectionName("down");
                    } else if (command.equalsIgnoreCase("left")) {
                        neighbourNode = rover.findNodeByDirectionName("left");
                    } else if (command.equalsIgnoreCase("right")) {
                        neighbourNode = rover.findNodeByDirectionName("right");
                    }

                    if (neighbourNode == null) {
                        System.out.println("Out of map or obstacle!");
                        break;
                    }

                    // Swap the adjacent nodes
                    char neighbourNodeSymbol = neighbourNode.getSymbol();
                    if (OBSTACLES_SET.contains(neighbourNodeSymbol)) {
                        break;
                    }
                    neighbourNode.setSymbol(rover.getSymbol());
                    rover.setSymbol(neighbourNodeSymbol);
                    rover = neighbourNode;
                }
            } else if (command.equalsIgnoreCase("debug")) {
                printMatrix(matrixRows, matrixCols);
            } else if (command.equalsIgnoreCase("path")) {
                List<Node> path = BFS(rover);
                resetVisitedNodesAfterPathCalculation(matrixRows, matrixCols);

                // No path message
                if (path == null) {
                    System.out.println("FAIL");
                    continue;
                }

                // Print Path
                System.out.printf("PATH %d\n", path.size() - 1);

                // Get the directions of the path up, down, left, right
                printPathDirections(path);
            } else if (command.equalsIgnoreCase("debug-path")) {
                List<Node> path = BFS(rover);
                resetVisitedNodesAfterPathCalculation(matrixRows, matrixCols);

                if (path == null) {
                    System.out.println("FAIL");
                    continue;
                }

                printDebugPathMatrix(matrixRows, matrixCols, path);
                printPathDirections(path);
            }
        }
    }

    public static Object[] readMatrix(int rows, int cols) {
        // It returns the matrix and the rover node
        Scanner input = new Scanner(System.in);
        Node[][] matrix = new Node[rows][cols];
        Node rover = null;

        for (int i = 0; i < rows; i++) {
            char[] currentRow = input.nextLine().toCharArray();

            if (currentRow.length == 0) {
                System.out.println("ERROR");
                return new Object[]{null, null};
            }

            for (int j = 0; j < cols; j++) {
                char symbol = currentRow[j];

                if (!ALLOWED_SYMBOLS.contains(symbol)) {
                    System.out.println("ERROR");
                    return new Object[]{null, null};
                }

                Node currentNode = new Node(symbol);

                if (symbol == ROVER_SYMBOL) {
                    rover = currentNode;
                } else if (symbol == TARGET_SYMBOL) {
                    target = currentNode;
                }

                matrix[i][j] = currentNode;
            }
        }

        return new Object[]{matrix, rover};
    }

    public static List<Node> BFS(Node rover) {
        // The algorithm explores each node at degree 1, then 2, then 3, ...
        // It doesn't add the nodes that have symbols in the obstacles set

        Queue<List<Node>> queue = new LinkedList<>();
        List<Node> initialPath = new ArrayList<>();
        initialPath.add(rover);
        queue.add(initialPath);
        rover.setVisited(true);

        while (!queue.isEmpty()) {
            List<Node> currentPath = queue.poll();
            // Get the last node of the path
            Node currentNode = currentPath.get(currentPath.size() - 1);

            // If we reach the target, add the current path to the list of all paths
            if (currentNode == target) {
                return currentPath;
            }

            // Explore all adjacent nodes
            for (Edge incidentEdge : currentNode.getAdjacencyList()) {
                Node nextNode = incidentEdge.getEndNode();

                // Check if the next node is not visited and not an obstacle
                if (nextNode != null && !nextNode.getVisited() && !OBSTACLES_SET.contains(nextNode.getSymbol())) {
                    nextNode.setVisited(true);

                    // Create a new path by adding the next node to the current path
                    List<Node> newPath = new ArrayList<>(currentPath);
                    newPath.add(nextNode);

                    queue.add(newPath);
                }
            }
        }

        return null;
    }

    public static void resetVisitedNodesAfterPathCalculation(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node currentNode = matrix[i][j];

                currentNode.setVisited(false);
            }
        }
    }

    public static void printMatrix(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j].getSymbol() + "");
            }
            System.out.println();
        }
    }

    public static void printDebugPathMatrix(int rows, int cols, List<Node> path) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node currentNode = matrix[i][j];

                if (path.contains(currentNode) &&
                        currentNode.getSymbol() != TARGET_SYMBOL &&
                        currentNode.getSymbol() != ROVER_SYMBOL) {
                    System.out.print('.');
                } else {
                    System.out.print(currentNode.getSymbol());
                }
            }
            System.out.println();
        }
    }

    public static void printPathDirections(List<Node> path) {
        String currentDirection = "";
        int currentDirectionCount = 0;
        for (int i = 0; i < path.size() - 1; i++) {

            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);

            String direction = currentNode.getDirectionNameByNode(nextNode);

            if (!direction.equalsIgnoreCase(currentDirection)) {
                if (i != 0) {
                    System.out.printf("%s%s%n",
                            currentDirection,
                            (currentDirectionCount != 1) ? " " + currentDirectionCount : "");
                }


                currentDirection = direction;
                currentDirectionCount = 0;
            }
            currentDirectionCount += 1;
        }
        System.out.printf("%s%s%n",
                currentDirection,
                (currentDirectionCount != 1) ? " " + currentDirectionCount : "");
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
}
