package TaskA;

public class Node {
    // This class represents each cell in the map

    char symbol;
    boolean visited;

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

    public Node(char symbol) {
        this.symbol = symbol;
        this.visited = false;
    }


}
