package TaskA.directions;

public class DirectionUp implements IDirection{
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
    public DirectionUp() {
        this.name = "up";
        this.x = 0;
        this.y = 1;
    }



    @Override
    public void move() {

    }
}
