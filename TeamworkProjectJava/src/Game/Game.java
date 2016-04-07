package Game;

import Abilities.Attack;
import Enumerations.Abilities;
import Models.Creature;
import Models.Entity;
import Models.Player;
import Renderer.QuickView;
import World.Coord;
import World.Level;
import World.Physics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.Arrays;

/**
 * This object will contain all instances required for a complete game to be initialized, updated and displayed:
 * JavaFX scene
 * A display interface (GraphicsContext of a Canvas node)
 * A control schema and handlers
 * The level with all of it's constituents
 * Other (yet to be implemented)
 * <p>
 * All other packages call to this object for reference and interfaces (for instance, rendering methods get their
 * context from here)
 */
public class Game {
    private GraphicsContext gc;
    private Level level;
    private ControlState controlState;
    private long timeLast;      // System time in nanoseconds when last update occured
    private double elapsed;     // Time elapsed since last update in seconds

    public Game(GraphicsContext gc, long timeStart) {
        this.gc = gc;
        controlState = new ControlState();
        timeLast = timeStart;
        elapsed = 0;

        makeLevel();
    }

    public void makeLevel() {
        // TODO: initialize the level object and all of it's constituents
        // Temp contents
        level = new Level(new Player(0, 0, 0, new Coord(10, 10), true));
    }
    // TODO: this object can be streamed to a file for a complete game state save and load function

    // Shorthands for the most commonly used objects
    public GraphicsContext getGc() {
        return gc;
    }

    public Level getLevel() {
        return level;
    }

    // This will bypass some things that migth turn out to be important later on
    public Player getPlayer() {
        return level.getPlayer();
    }

    // Timekeeping
    public long getTime() {
        return timeLast;
    }

    public double getElapsed() {
        return elapsed;
    }

    public void update(long timeNew) {
        elapsed = (timeNew - timeLast) / 1000000000.0;
        if (elapsed > 1.0) elapsed = 1.0;
        timeLast = timeNew;

        level.getEntities().stream().filter(entity -> entity instanceof Creature).forEach(entity -> {
            Creature current = (Creature)entity;
            current.update(elapsed);
        });
    }

    public void render() {
        // TODO: filter out entities outside visibility scope
        level.getEntities().stream().filter(entity -> entity.isAlive()).forEach(Entity::render);
    }

    // TODO: event handlers
    public ControlState getControlState() {
        return controlState;
    }

    public void handleInput() {
        // User can't control the character if it's velocity is greater than Physics.maxMoveSpeed
        // This has the positive side effect of disabling player controls during knockback
        if (getPlayer().getVelocity().getMagnitude() > Physics.maxMoveSpeed) return;

        // Mouse
        if (controlState.mouseLeft) {
            // Attack
            getPlayer().useAbility(Abilities.ATTACKPRIMARY);
        }

        // Keyboard
        // Sloppy dodge roll; this is dependent on framerate, find a better implementation!
        double modifier = Physics.playerAcceleration;
        if (controlState.pressed(KeyCode.SPACE)) {
            modifier *= 10;
        }
        if (controlState.pressed(KeyCode.W) && !controlState.pressed(KeyCode.S)) {
            getPlayer().accelerate(new Coord(0.0, -modifier), elapsed);
        } else if (controlState.pressed(KeyCode.S) && !controlState.pressed(KeyCode.W)) {
            getPlayer().accelerate(new Coord(0.0, modifier), elapsed);
        }
        if (controlState.pressed(KeyCode.A) && !controlState.pressed(KeyCode.D)) {
            getPlayer().accelerate(new Coord(-modifier, 0.0), elapsed);
        } else if (controlState.pressed(KeyCode.D) && !controlState.pressed(KeyCode.A)) {
            getPlayer().accelerate(new Coord(modifier, 0.0), elapsed);
        }
    }

}
