package TaskA;

import TaskA.directions.IDirection;

public class Edge {
    // This class represents the link between two nodes
    // It uses the strategy design pattern for the directions

    Node startNode;
    Node endNode;
    IDirection direction;

    public Edge(Node startNode, Node endNode, IDirection direction) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.direction = direction;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public IDirection getDirection() {
        return direction;
    }

    public void setDirection(IDirection direction) {
        this.direction = direction;
    }
}
