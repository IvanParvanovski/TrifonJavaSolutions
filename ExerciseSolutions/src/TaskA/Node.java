package TaskA;

import java.util.Arrays;

public class Node {
    // This class represents each cell in the map

    char symbol;
    boolean visited;
    Edge[] adjacencyList = new Edge[4];

    public char getSymbol() {
        return this.symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Edge[] getAdjacencyList() {
        return this.adjacencyList;
    }

    public void setAdjacencyList(Edge[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Node(char symbol) {
        this.symbol = symbol;
        this.visited = false;
    }

    @Override
    public String toString() {
        return String.format("Node{symbol=%s, visited=%b}", this.symbol, this.visited);
    }

    public Node findNodeByDirectionName(String directionName) {
        for (Edge e : this.adjacencyList) {
            if (e.getDirection().getName().equalsIgnoreCase(directionName)) {
                return e.getEndNode();
            }
        }

        System.out.println("Direction Not Found!");
        return null;
    }

    public Edge[] deepCopyAdjacencyList() {
        Edge[] deepCopyEdges = new Edge[this.adjacencyList.length];
        for (int i = 0; i < this.adjacencyList.length; i++) {
            Edge edge = this.adjacencyList[i];
            deepCopyEdges[i] = new Edge(edge.startNode, edge.endNode, edge.direction);
        }
        return deepCopyEdges;
    }
}
