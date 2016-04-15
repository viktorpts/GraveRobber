package Game;

import Enumerations.Abilities;
import Enumerations.EntityState;
import Models.Creature;
import Models.Entity;
import Models.Player;
import World.Coord;
import World.Level;
import World.Physics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.EnumSet;

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
        level = new Level(new Player(100, 10, 2, 10, 10));
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

    // TODO: place time calculations in separate method, so we can change the order in which input, state, rendering occurs
    public void update(long timeNew) {
        elapsed = (timeNew - timeLast) / 1000000000.0;
        if (elapsed > 1.0) elapsed = 1.0;
        timeLast = timeNew;

        ArrayList<Entity> markedForDeletion = new ArrayList<>(); // prevent ConcurrentModificationException
        level.getEntities().stream().filter(entity -> entity instanceof Creature).forEach(entity -> {
            if (entity.getState().contains(EntityState.DESTROYED)) { // release dead entries
                markedForDeletion.add(entity);
                return;
            }
            entity.animate(elapsed);
            if (entity.hasState(EntityState.DEAD)) return; // don't update if it's dead (not destroyed yet!)
            Creature current = (Creature)entity;
            current.update(elapsed);
        });
        // Release all marked entities
        for (Entity entity : markedForDeletion) {
            level.getEntities().remove(entity);
        }
    }

    public void render() {
        // TODO: filter out entities outside visibility scope
        // TODO: terrain rendering
        // TODO: vertical ordering, to handle overlap
        // todo since we already cleared the list, this check is likely redundant
        level.getEntities().stream().filter(entity -> entity.isAlive()).forEach(Entity::render);
    }

    // TODO: event handlers
    public ControlState getControlState() {
        return controlState;
    }

    // TODO: best to move this to the player class and leave minimal processing here
    public void handleInput() {
        // Player states need to go somewhere else, but are here for now
        // Don't let the player move if he's stunned
        if (getPlayer().hasState(EntityState.STAGGERED)) return;

        // User can't control the character if it's velocity is greater than Physics.maxMoveSpeed
        // This has the positive side effect of disabling player controls during knockback
        if (getPlayer().getVelocity().getMagnitude() > Physics.maxMoveSpeed) return;

        // Mouse
        if (controlState.isMouseLeft()) {
            // Attack
            // TODO: attack chaining (if Attack ability is in the correct state, add the next ability to the queue)
            getPlayer().useAbility(Abilities.ATTACKPRIMARY);
        }
        // TODO: add some sort of order queue, so the attack combo and animation cancelling window is more generous

        // Keyboard
        // Sloppy dodge roll; this is dependent on framerate, find a better implementation!
        double modifier = Physics.playerAcceleration + Physics.friction; // we add friction so we can have a net positive
        if (controlState.pressed(KeyCode.SPACE)) {
            modifier *= 10;
        }
        // if player is busy, don't let him move
        if (getPlayer().hasState(EntityState.CASTUP) ||
                getPlayer().hasState(EntityState.CASTING) ||
                getPlayer().hasState(EntityState.CASTDOWN)) return;
        if (controlState.pressed(KeyCode.W) && !controlState.pressed(KeyCode.S)) {
            getPlayer().accelerate(new Coord(0.0, -modifier), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        } else if (controlState.pressed(KeyCode.S) && !controlState.pressed(KeyCode.W)) {
            getPlayer().accelerate(new Coord(0.0, modifier), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        }
        if (controlState.pressed(KeyCode.A) && !controlState.pressed(KeyCode.D)) {
            getPlayer().accelerate(new Coord(-modifier, 0.0), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        } else if (controlState.pressed(KeyCode.D) && !controlState.pressed(KeyCode.A)) {
            getPlayer().accelerate(new Coord(modifier, 0.0), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        }
    }

}
