package TaskA;

import TaskA.directions.*;

import java.util.Scanner;
import java.util.Set;

public class MainSolutionA {
    // GRID-LIKE Graph

    private static final Node ROVER_SYMBOL = new Node('R');
    private static final Node TARGET_SYMBOL = new Node('x');
    private static final Set<Character> OBSTACLES_SET = Set.of('/', '\\', '|', '_', '*');
    private static Node[][] matrix;

    private static DirectionUp directionUp = new DirectionUp();
    private static DirectionDown directionDown = new DirectionDown();
    private static DirectionLeft directionLeft = new DirectionLeft();
    private static DirectionRight directionRight = new DirectionRight();

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
                System.out.println(matrix);
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
                System.out.println("path");
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
                matrix[i][j] = new Node(symbol);
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
                Edge upEdge = new Edge(currentNode, upNode, directionUp);
                Edge downEdge = new Edge(currentNode, downNode, directionDown);
                Edge rightEdge = new Edge(currentNode, rightNode, directionRight);
                Edge leftEdge = new Edge(currentNode, leftNode, directionLeft);

                Edge[] currentNodeAdjacencyList = new Edge[]{ upEdge, downEdge, rightEdge, leftEdge };
                currentNode.setAdjacencyList(currentNodeAdjacencyList);
            }
        }
    }
}
