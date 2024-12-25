package TaskA.directions;

public class DirectionLeft implements IDirection {
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


    public DirectionLeft() {
        this.name = "left";
        this.x = -1;
        this.y = 0;
    }


}
