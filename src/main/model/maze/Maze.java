package model.maze;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import model.utility.Coordinate;
import model.utility.Direction;

// Maze describes a maze that the player has to solve.
// both start and exit point will be randomly generated
// a point within the region of ([width/2, width-1], [height/2, height-1])
// will be the exit point
// a point within the region of ([0, width/2), [0, height/2))
// will be the start point
// assuming the top left corner is (0, 0)
// true stands for a road and false stands for a wall
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
    // and an end point will be within the region of ([width/2, width-1], [height/2,
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
    public Coordinate getRoad(int index) {
        return roads.get(index);
    }

    // REQUIRES: isInRange(coord) is true
    // MODIFIES: this
    // EFFECTS: set the value to the given position in the maze
    public void setBlock(Coordinate coord, boolean value) {
        maze[coord.getY()][coord.getX()] = value;
    }

    // EFFECTS: convert the maze to json object
    public JSONObject toJson() {
        JSONObject maze = new JSONObject();
        maze.put("exit", exit.toJson());
        maze.put("static", start.toJson());
        maze.put("maze", this.maze);
        return maze;
    }

    // MODIFIES: this
    // EFFECTS: generate a maze with a random start point within the region of
    // ([0, width/2), [0, height/2))
    // and a random exit point within the region of
    // ([width/2, width-1], [height/2, height-1])
    // by the Randomized Prim's algorithm
    // source link:
    // https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Prim's_algorithm
    private void generateMaze() {
        ArrayList<Coordinate> queue = new ArrayList<>();

        setBlock(start, true);
        explore(start, queue);

        while (queue.size() != 0) {
            int index = random.nextInt(queue.size());
            Coordinate next = queue.get(index);

            if (countSurroundingRoad(next) == 1) {
                roads.add(next);
                setBlock(next, true);
                explore(next, queue);
            }
            queue.remove(index);
        }

        generateExit();
    }

    // MODIFIES: this
    // EFFECTS: set a random exit point within the region of
    // ([width/2, width-1], [height/2, height-1])
    private void generateExit() {
        do {
            int xcoord = random.nextInt(width / 2) + width / 2;
            int ycoord = random.nextInt(height / 2) + height / 2;
            exit = new Coordinate(xcoord, ycoord);
        } while (isWall(exit));
    }

    // REQUIRES: isInRange(coord)
    // EFFECTS: count the number of road around the coord
    // only check 4 directions:
    // up, down, left, right
    private int countSurroundingRoad(Coordinate coord) {
        int roadCounter = 0;
        int saveX = coord.getX();
        int saveY = coord.getY();
        for (Direction direction : Direction.values()) {
            coord.go(direction, 1);
            if (this.isInRange(coord) && !this.isWall(coord)) {
                roadCounter++;
            }
            coord.setXY(saveX, saveY);
        }
        return roadCounter;
    }

    // MODIFIES: walls
    // EFFECTS: check 4 directions: up, down, right, left around the coord
    // if any of these 4 points satisfies (isInRange(point) && isWall(point))
    // this point will be added to the walls
    private void explore(Coordinate coord, ArrayList<Coordinate> walls) {
        int saveX = coord.getX();
        int saveY = coord.getY();
        for (Direction direction : Direction.values()) {
            coord.go(direction, 1);
            if (isInRange(coord) && isWall(coord)) {
                Coordinate wall = new Coordinate(coord.getX(), coord.getY());
                walls.add(wall);
            }
            coord.setXY(saveX, saveY);
        }
    }
}
