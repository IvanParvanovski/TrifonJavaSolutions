package TaskA.directions;

public class DirectionDown implements IDirection {
    String name;
    int x;
    int y;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void move() {

    }

    public DirectionDown() {
        this.name = "down";
        this.x = 0;
        this.y = -1;
    }

}
