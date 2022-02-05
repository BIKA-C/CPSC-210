package model.utility;

// Coodinate is a (x, y) coordinate
public class Coordinate {

    private int xcoord;
    private int ycoord;
    private final int saveX;
    private final int saveY;

    // xxx may not necessary // REQUIRES: xcoord >= 0 and ycoord >= 0
    // EFFECTS: constructs a coordinate with the given x and y
    public Coordinate(int xcoord, int ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;

        this.saveX = xcoord;
        this.saveY = ycoord;
    }

    // xxx may not necessary // REQUIRES: xcoord >= 0
    // MODIFIES: this
    // EFFECTS: set x to be the given x
    public void setX(int xcoord) {
        this.xcoord = xcoord;
    }

    // xxx may not necessary // REQUIRES: ycoord >= 0
    // MODIFIES: this
    // EFFECTS: set y to be the given y
    public void setY(int ycoord) {
        this.ycoord = ycoord;
    }

    // MODIFIES: this
    // EFFECTS: xcoord - 1
    public void goLeft() {
        this.xcoord--;
    }

    // MODIFIES: this
    // EFFECTS: xcoord + 1
    public void goRight() {
        this.xcoord++;
    }

    // MODIFIES: this
    // EFFECTS: ycoord - 1
    public void goUp() {
        this.ycoord--;
    }

    // MODIFIES: this
    // EFFECTS: ycoord + 1
    public void goDown() {
        this.ycoord++;
    }

    // MODIFIES: this
    // EFFECTS: go with the given direction
    // for example go(Direction.up) is equivalent to goUp()
    public void go(Direction direction) {
        switch (direction) {
            case up:
                this.goUp();
                break;
            case down:
                this.goDown();
                break;
            case left:
                this.goLeft();
                break;
            case right:
                this.goRight();
                break;

            default:
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: set xcoord to be the newX
    // and ycoord to be the newY
    public void setXY(int newX, int newY) {
        this.xcoord = newX;
        this.ycoord = newY;
    }

    // MODIFIES: this
    // EFFECTS: increas y by amount
    public void increaseY(int amount) {
        this.ycoord += amount;
    }

    // MODIFIES: this
    // EFFECTS: decreas y by amount
    public void decreaseY(int amount) {
        this.ycoord -= amount;
    }

    // MODIFIES: this
    // EFFECTS: increas x by amount
    public void increaseX(int amount) {
        this.xcoord += amount;
    }

    // MODIFIES: this
    // EFFECTS: decreas x by amount
    public void decreaseX(int amount) {
        this.xcoord -= amount;
    }

    // MODIFIES: this
    // EFFECTS: increase x and y by dx and dy
    public void increaseXY(int dx, int dy) {
        this.xcoord += dx;
        this.ycoord += dy;
    }

    // MODIFIES: this
    // EFFECTS: decreas x and y by dx and dy
    public void decreaseXY(int dx, int dy) {
        this.xcoord -= dx;
        this.ycoord -= dy;
    }

    // MODIFIES: this
    // EFFECTS: reset the xcoord and ycoord to be
    // the value that the object is inititialized with
    public void reset() {
        this.xcoord = this.saveX;
        this.ycoord = this.saveY;
    }

    // EFFECTS: true if getX() == coordinate.getX() && getY() == coordinate.getY();
    public boolean isSame(Coordinate coordinate) {
        return this.xcoord == coordinate.xcoord && this.ycoord == coordinate.ycoord;
    }

    public int getX() {
        return xcoord;
    }

    public int getY() {
        return ycoord;
    }
}
