package TaskA.directions;

public interface IDirection {
    // Interface for the directions that the rover can move to

    String getName();
    int getX();
    int getY();
    void move();
}
