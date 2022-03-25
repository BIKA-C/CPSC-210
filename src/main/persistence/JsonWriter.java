package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import model.Game;

// Represents a writer that writes JSON representation of game to file
// modified from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
public class JsonWriter {

    // MODIFIES: this
    // EFFECTS: writes json form of the game to the just opened dest file and closes the file
    public void saveToFile(String dest, Game game) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(dest));

        writer.print(game.toJson().toString(4));
        writer.close();
    }
}
