package TaskA;

import TaskA.directions.*;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainSolutionA {
    // GRID-LIKE Graph

    private static final char ROVER_SYMBOL = 'R';
    private static final char TARGET_SYMBOL = 'x';
    private static final char OVERLAP_SYMBOL = 'X';
    private static final char SPACE_SYMBOL = '␣';

    private static final Set<Character> OBSTACLES_SET = Set.of('/', '\\', '|', '_', '*');
    private static final Set<Character> ALLOWED_SYMBOLS = Stream.concat(
            OBSTACLES_SET.stream(),
            Stream.of(ROVER_SYMBOL, TARGET_SYMBOL, OVERLAP_SYMBOL, SPACE_SYMBOL)
    ).collect(Collectors.toSet());

    private static final DirectionUp DIRECTION_UP = new DirectionUp();
    private static final DirectionDown DIRECTION_DOWN = new DirectionDown();
    private static final DirectionLeft DIRECTION_LEFT = new DirectionLeft();
    private static final DirectionRight DIRECTION_RIGHT = new DirectionRight();

    private static Node[][] matrix;
    private static int matrixRows;
    private static int matrixCols;

    private static Node target = null;
    private static Node overlap = null;
    private static Node rover = null;

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
                matrixCols = Integer.parseInt(splitInput[1]);
                matrixRows = Integer.parseInt(splitInput[2]);

                Node[][] userInputMatrix = readMatrix(matrixRows, matrixCols);

                // The matrix is not set because the input matrix was invalid
                if (userInputMatrix == null) {
                    continue;
                }

                matrix = userInputMatrix;

                printMatrix(matrixRows, matrixCols);
                setEachNodeAdjacencyList(matrixRows, matrixCols);
            }
            else if (command.equalsIgnoreCase("up")
                    || command.equalsIgnoreCase("down")
                    || command.equalsIgnoreCase("left")
                    || command.equalsIgnoreCase("right")) {

                if (rover == null) {
                    System.out.println("Rover not initialised!");
                    continue;
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
                // Display the map with the current positions of the target and the
                printMatrix(matrixRows, matrixCols);
            } else if (command.equalsIgnoreCase("path")) {
                // Special case where the rover and the target overlap
                if (overlap != null && rover == null && target == null) {
                    System.out.println("PATH 0");
                    continue;
                }

                // Calculate path and reset the nodes if you want to repeat the command
                List<Node> path = BFS(rover);
                resetVisitedNodesAfterPathCalculation(matrixRows, matrixCols);

                // No path message
                if (path == null) {
                    System.out.println("FAIL");
                    continue;
                }

                // Print the path if it exists
                printPathDirections(path);
            } else if (command.equalsIgnoreCase("debug-path")) {
                List<Node> path = BFS(rover);
                resetVisitedNodesAfterPathCalculation(matrixRows, matrixCols);

                if (path == null) {
                    System.out.println("FAIL");
                    continue;
                }

                printDebugPathMatrix(matrixRows, matrixCols, path);
            }
        }
    }

    public static Node[][] readMatrix(int rows, int cols) {
        // It returns the matrix and sets the target, rover and overlap if they exist

        Scanner input = new Scanner(System.in);
        Node[][] matrix = new Node[rows][cols];
        Node rover = null;

        for (int i = 0; i < rows; i++) {
            char[] currentRow = input.nextLine().toCharArray();

            if (currentRow.length == 0) {
                System.out.println("ERROR");
                return null;
            }

            for (int j = 0; j < cols; j++) {
                char symbol = currentRow[j];

                if (!ALLOWED_SYMBOLS.contains(symbol)) {
                    System.out.println("ERROR");
                    return null;
                }

                Node currentNode = new Node(symbol);

                // If it finds the special symbols like rover it sets them
                if (symbol == ROVER_SYMBOL) {
                    rover = currentNode;
                } else if (symbol == TARGET_SYMBOL) {
                    target = currentNode;
                } else if (symbol == OVERLAP_SYMBOL) {
                    overlap = currentNode;
                }

                matrix[i][j] = currentNode;
            }
        }

        if (overlap == null && rover == null && target == null) {
            System.out.println("ERROR");
            return null;
        }

        return matrix;
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
        List<String> resultMessage = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            // Get the current and the neighbour node in the following direction
            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);

            String direction = currentNode.getDirectionNameByNode(nextNode);

            // Change direction
            if (!direction.equalsIgnoreCase(currentDirection)) {
                // When it is the first direction it should not print anything, so not add
                if (i != 0) {
                    resultMessage.add(String.format("%s%s",
                            currentDirection,
                            (currentDirectionCount != 1) ? " " + currentDirectionCount : ""
                    ));
                }

                // Change direction and reset count
                currentDirection = direction;
                currentDirectionCount = 0;
            }
            currentDirectionCount += 1;
        }

        // We want to print if it does not overlap
        resultMessage.add(String.format("%s%s",
                currentDirection,
                (currentDirectionCount != 1) ? " " + currentDirectionCount : ""
        ));

        // Print result
        System.out.println("PATH " + resultMessage.size());
        System.out.println(String.join("\n", resultMessage));
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
