package Game;

import Models.Player;
import World.Coord;
import World.Level;
import javafx.scene.canvas.GraphicsContext;

/**
 * This object will contain all instances required for a complete game to be initialized, updated and displayed:
 * JavaFX scene
 * A display interface (GraphicsContext of a Canvas node)
 * A control schema and handlers
 * The level with all of it's constituents
 * Other (yet to be implemented)
 *
 * All other packages call to this object for reference and interfaces (for instance, rendering methods get their
 * context from here)
 */
public class Game {
    private GraphicsContext gc;
    private Level level;

    public Game(GraphicsContext gc) {
        this.gc = gc;
        makeLevel();
    }

    public void makeLevel() {
        // TODO: initialize the level object and all of it's constituents
        // Temp contents
        level = new Level(new Player(0, 0, 0, new Coord(Main.horizontalRes / 2, Main.verticalRes / 2), true));
    }
    // TODO: this object can be streamed to a file for a complete game state save and load function

    public GraphicsContext getGc() {
        return gc;
    }

    public Level getLevel() {
        return level;
    }

    // TODO: update functions
    // TODO: event handlers
}
