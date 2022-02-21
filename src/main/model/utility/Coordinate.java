package model.utility;

// Coordinate is a (x, y) coordinate
public class Coordinate {

    private int xcoord;
    private int ycoord;

    // EFFECTS: constructs a coordinate with the given x and y
    public Coordinate(int xcoord, int ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    // MODIFIES: this
    // EFFECTS: set x to be the given x
    public void setX(int xcoord) {
        this.xcoord = xcoord;
    }

    // MODIFIES: this
    // EFFECTS: set y to be the given y
    public void setY(int ycoord) {
        this.ycoord = ycoord;
    }

    // MODIFIES: this
    // EFFECTS: getX() -= dx;
    public void goLeft(int dx) {
        this.xcoord -= dx;
    }

    // MODIFIES: this
    // EFFECTS: getX() += dx
    public void goRight(int dx) {
        this.xcoord += dx;
    }

    // MODIFIES: this
    // EFFECTS: getY() -= dy
    public void goUp(int dy) {
        this.ycoord -= dy;
    }

    // MODIFIES: this
    // EFFECTS: getY() += dy
    public void goDown(int dy) {
        this.ycoord += dy;
    }

    // MODIFIES: this
    // EFFECTS: go with the given direction
    // for example go(Direction.up, 1) is equivalent to goUp(1)
    public void go(Direction direction, int distance) {
        if (direction == Direction.UP) {
            goUp(distance);
        } else if (direction == Direction.DOWN) {
            goDown(distance);
        } else if (direction == Direction.LEFT) {
            goLeft(distance);
        } else {
            goRight(distance);
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
    // EFFECTS: increase y by amount
    public void increaseY(int amount) {
        this.ycoord += amount;
    }

    // MODIFIES: this
    // EFFECTS: decrease y by amount
    public void decreaseY(int amount) {
        this.ycoord -= amount;
    }

    // MODIFIES: this
    // EFFECTS: increase x by amount
    public void increaseX(int amount) {
        this.xcoord += amount;
    }

    // MODIFIES: this
    // EFFECTS: decrease x by amount
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
    // EFFECTS: decrease x and y by dx and dy
    public void decreaseXY(int dx, int dy) {
        this.xcoord -= dx;
        this.ycoord -= dy;
    }

    @Override
    public int hashCode() {
        return (xcoord + ycoord) * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj.getClass() != this.getClass()) {
            return false;
        }

        Coordinate coord = (Coordinate) obj;
        return isSame(coord);
    }

    // REQUIRES: coordinate != null
    // EFFECTS: true if getX() == coordinate.getX() && getY() == coordinate.getY();
    public boolean isSame(Coordinate coordinate) {
        return isSame(coordinate.xcoord, coordinate.ycoord);
    }

    // EFFECTS: true if getX() == x && getY() == y;
    public boolean isSame(int x, int y) {
        return this.xcoord == x && this.ycoord == y;
    }

    public int getX() {
        return xcoord;
    }

    public int getY() {
        return ycoord;
    }

    @Override
    public String toString() {
        return "(" + xcoord + " , " + ycoord + ")";
    }
}
