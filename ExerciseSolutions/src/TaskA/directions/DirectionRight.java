package TaskA.directions;

public class DirectionRight implements IDirection{
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

    public DirectionRight() {
        this.name = "right";
        this.x = 1;
        this.y = 0;
    }


}
