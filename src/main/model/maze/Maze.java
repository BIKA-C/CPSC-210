package model.maze;

import java.util.ArrayList;
import java.util.Random;

import model.utility.Coordinate;
import model.utility.Direction;

// Maze describes a maze that the player has to solve.
// both start and exit point will be randomly generated
// a point within the region of ([width/2, width-1], [height/2, height-1])
// will be the exit point
// a point within the region of ([0, width/2), [0, height/2))
// will be the start point
// assuming the top left corner is (0, 0)
public class Maze {

    private boolean[][] maze;
    private int width;
    private int height;

    private Coordinate exit;
    private Coordinate start;
    ArrayList<Coordinate> roads;

    private Random random;

    // REQUIRES: (width % 2 == 0) and (height % 2 == 0) and (width and height) >= 8
    // EFFECTS: constructs a random maze by the given width and
    // height. A start point will be within the region of ([0, width/2), [0,
    // height/2))
    // and a end point will be within the region of ([width/2, width-1], [height/2,
    // height-1])
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.roads = new ArrayList<>();
        this.random = new Random();

        maze = new boolean[height][width];

        start = new Coordinate(random.nextInt(width / 2), random.nextInt(height / 2));

        generateMaze();
    }

    // REQUIRES: isInRange(coordinate)
    // EFFECTS: returns true if the given coordinate in the map
    // is a wall
    public boolean isWall(Coordinate coordinate) {
        return !maze[coordinate.getY()][coordinate.getX()];
    }

    // EFFECTS: returns true if
    // coordinate.getX() >= 0 &&
    // coordinate.getX() <= getWidth() -1 &&
    // coordinate.getY() >= 0 &&
    // coordinate.getY() <= getHeight() -1 &&
    // false otherwise
    public boolean isInRange(Coordinate coordinate) {
        int xcoord = coordinate.getX();
        int ycoord = coordinate.getY();
        return xcoord >= 0
                && xcoord <= width - 1
                && ycoord >= 0
                && ycoord <= height - 1;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getExit() {
        return exit;
    }

    // EFFECTS: return the number of the roads
    public int getNumOfRoad() {
        return roads.size();
    }

    // REQUIRES: index >= 0 && index < getNumOfRoad()
    // EFFECTS: returns a road coord by the given index
    // the order is random
    public Coordinate getaRoad(int index) {
        return roads.get(index);
    }

    private void setBloack(Coordinate coord, boolean value) {
        maze[coord.getY()][coord.getX()] = value;
    }

    private void generateMaze() {
        ArrayList<Coordinate> queue = new ArrayList<>();

        setBloack(start, true);
        explore(start, queue);

        while (queue.size() != 0) {
            int index = random.nextInt(queue.size());
            Coordinate next = queue.get(index);

            if (countSurroundingRoad(next) == 1) {
                roads.add(next);
                setBloack(next, true);
                explore(next, queue);
            }
            queue.remove(index);
        }

        generateExit();
    }

    private void generateExit() {
        do {
            int xcoord = random.nextInt(width / 2) + width / 2;
            int ycoord = random.nextInt(height / 2) + height / 2;
            exit = new Coordinate(xcoord, ycoord);
        } while (isWall(exit));
    }

    private int countSurroundingRoad(Coordinate coord) {
        int roadCounter = 0;
        int saveX = coord.getX();
        int saveY = coord.getY();
        for (Direction direction : Direction.values()) {
            coord.go(direction);
            if (this.isInRange(coord) && !this.isWall(coord)) {
                roadCounter++;
            }
            coord.setXY(saveX, saveY);
        }
        return roadCounter;
    }

    private void explore(Coordinate coord, ArrayList<Coordinate> walls) {
        int saveX = coord.getX();
        int saveY = coord.getY();
        for (Direction direction : Direction.values()) {
            coord.go(direction);
            if (isInRange(coord) && isWall(coord)) {
                Coordinate wall = new Coordinate(coord.getX(), coord.getY());
                walls.add(wall);
            }
            coord.setXY(saveX, saveY);
        }
    }
}
