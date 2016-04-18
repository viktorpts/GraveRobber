package Game;

import Enumerations.Abilities;
import Enumerations.EntityState;
import Models.Creature;
import Models.Entity;
import Models.Player;
import Renderer.QuickView;
import World.Coord;
import World.Level;
import World.Physics;
import World.Tile;
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

    /**
     * Initialize level; the coordinates for the player here don't matter, since a random starting location is chosen
     * by the Level itself, after the maze is generated
     */
    public void makeLevel() {
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

    // This will bypass some things that might turn out to be important later on
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

    /**
     * Calculate length of previous frame in seconds and store it internally for all other time dependent functions.
     * @param timeNew Current processor time in nanoseconds. We convert that to seconds and forward to all entities for
     *                convenience
     */
    public void passTime(long timeNew) {
        elapsed = (timeNew - timeLast) / 1000000000.0;
        if (elapsed > 1.0) elapsed = 1.0;
        timeLast = timeNew;
    }

    /**
     * Update everything, called every frame. We only apply physics to the living ones and ignore the destroyed ones
     * altogether. Filters are in place to only update entities a certain distance from where the camera is looking.
     */
    public void update() {
        ArrayList<Entity> markedForDeletion = new ArrayList<>(); // prevent ConcurrentModificationException
        // Limit to Creature instances that are within a predetermined range (one screen width in each direction from the camera)
        level.getEntities().stream().filter(entity -> entity instanceof Creature)
                .filter(entity -> entity.getX() > getPlayer().getX() - Physics.activeRange &&
                        entity.getX() < getPlayer().getX() + Physics.activeRange &&
                        entity.getY() > getPlayer().getY() - Physics.activeRange &&
                        entity.getY() < getPlayer().getY() + Physics.activeRange)
                .forEach(entity -> {
                    if (entity.getState().contains(EntityState.DESTROYED)) { // release dead entries
                        markedForDeletion.add(entity);
                        return;
                    }
                    entity.animate(elapsed); // process animation first, since we need to know if state has changed (note this wont output, only update!)
                    if (entity.hasState(EntityState.DEAD)) return; // don't update if it's dead (not destroyed yet!)
                    Creature current = (Creature) entity;
                    current.update(elapsed);
                });
        // Release all marked entities
        for (Entity entity : markedForDeletion) {
            level.getEntities().remove(entity);
        }
    }

    /**
     * Output everything that's within range of the camera. Each entity takes care of it's own rendering, we just
     * propagate the call to everyone.
     */
    public void render() {
        // Level tiles first, so they appear bellow everything else
        level.getGeometry().stream()
                .filter(tile -> tile.getX() > getPlayer().getX() - Physics.activeRange &&
                        tile.getX() < getPlayer().getX() + Physics.activeRange &&
                        tile.getY() > getPlayer().getY() - Physics.activeRange &&
                        tile.getY() < getPlayer().getY() + Physics.activeRange)
                .forEach(Tile::render);
        // Now entities. Note dead creatures still render according to their creation index, so there might be some
        // unwanted overlap
        level.getEntities().stream()
                .filter(entity -> entity.getX() > getPlayer().getX() - Physics.activeRange &&
                        entity.getX() < getPlayer().getX() + Physics.activeRange &&
                        entity.getY() > getPlayer().getY() - Physics.activeRange &&
                        entity.getY() < getPlayer().getY() + Physics.activeRange)
                .forEach(Entity::render);
    }

    public ControlState getControlState() {
        return controlState;
    }

    /**
     * Control handler. We call this every frame to see what the user is doing and command the player character
     * accordingly.
     */
    // TODO: best to move this to the player class and leave minimal processing here
    public void handleInput() {
        // Player states need to go somewhere else, but are here for now
        // Don't let the player move if he's stunned
        if (getPlayer().hasState(EntityState.STAGGERED)) return;

        // Face mouse cursor
        double[] mousePos = Main.game.getControlState().getMouse();
        double offsetX = QuickView.toWorldX(mousePos[0]) - getPlayer().getX();
        double offsetY = QuickView.toWorldY(mousePos[1]) - getPlayer().getY();
        double dir = Math.atan2(offsetY, offsetX);
        // Don't let the player look around if he's committed to an animation
        if (getPlayer().isReady()) getPlayer().setDirection(dir);

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
        double modifier = Physics.playerAcceleration + Physics.friction; // we add friction so we can have a net positive
        // Sloppy dodge roll; this is dependent on framerate, find a better implementation!
        if (controlState.pressed(KeyCode.SPACE)) {
            modifier *= 10;
        }
        // if player is busy, don't let him move
        if (getPlayer().hasState(EntityState.CASTUP) ||
                getPlayer().hasState(EntityState.CASTING) ||
                getPlayer().hasState(EntityState.CASTDOWN)) return;
        if (controlState.pressed(KeyCode.W) && !controlState.pressed(KeyCode.S)) { // go up
            getPlayer().accelerate(new Coord(0.0, -modifier), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        } else if (controlState.pressed(KeyCode.S) && !controlState.pressed(KeyCode.W)) { // go down
            getPlayer().accelerate(new Coord(0.0, modifier), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        }
        if (controlState.pressed(KeyCode.A) && !controlState.pressed(KeyCode.D)) { // go left
            getPlayer().accelerate(new Coord(-modifier, 0.0), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        } else if (controlState.pressed(KeyCode.D) && !controlState.pressed(KeyCode.A)) { // go right
            getPlayer().accelerate(new Coord(modifier, 0.0), elapsed);
            getPlayer().setState(EnumSet.of(EntityState.MOVING));
        }
    }

}
