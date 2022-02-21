package ui;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import model.Game;
import model.item.Breaker;
import model.item.Item;
import model.utility.Coordinate;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // ConsoleApp game = new ConsoleApp();
        // game.start();

        Game g = new Game(20, 20);

        HashMap<Coordinate, Item> test = new HashMap<>();
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(11, 20);
        Coordinate c = new Coordinate(12, 20);
        Item item = new Breaker(100);

        test.put(a, item);
        test.put(b, item);
        test.put(c, item);
        JSONObject json = g.toJson();

        System.out.println(json.toString(4));
    }
}
