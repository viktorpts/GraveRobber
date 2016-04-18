package Models;

import Enumerations.Abilities;
import Abilities.Ability;
import Abilities.Dash;
import Abilities.MeleeAttack;
import Enumerations.EntityState;
import Game.ControlState;
import Game.Main;
import Renderer.Animation;
import Renderer.QuickView;
import World.Coord;
import World.Physics;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

/**
 * Dedicated class to differentiate the Player character from his inanimate parent class and his sibling Enemy
 * creatures. We keep user input information here - keys/button pressed and mouse position. Note Player framerate is
 * considerably higher than everything else, to make controls feel responsive.
 */
public class Player extends Creature {

    // User input record
    private double mouseX;
    private double mouseY;
    private ControlState current;
    private ControlState dispatched;
    private ControlState released;

    private Inventory inventory;

    // Create new player
    public Player(int startHealthPoints,
                  int startAttackPower,
                  int startArmorValue,
                  double x, double y) {
        super(new Animation(15, "Player"),
                x, y, 0.0,
                startHealthPoints, startAttackPower, startArmorValue,
                new HashMap<Abilities, Ability>(),
                0.25, Physics.maxMoveSpeed, Physics.maxAcceleration);
        addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(this, startAttackPower, 1.0));
        addAbility(Abilities.DASH, new Dash(this, 5, 12));

        current = new ControlState();
        dispatched = new ControlState();
        released = new ControlState();
    }

    // TODO: Inventory management

    public ControlState getControlState() {
        return current;
    }

    //region Keyboard Properties
    public void addKey(KeyCode kc) {
        current.addKey(kc);
    }
    public void removeKey(KeyCode kc) {
        current.removeKey(kc);
    }

    public void setMouseLeft(boolean mouseLeft) {
        current.setMouseLeft(mouseLeft);
    }

    public void setMouseRight(boolean mouseRight) {
        current.setMouseRight(mouseRight);
    }
    //endregion ==============================

    //region Mouse Properties
    public double getMouseX() {
        return mouseX;
    }
    public double getMouseY() {
        return mouseY;
    }
    public double[] getMouse() {
        double[] result = { mouseX, mouseY };
        return result;
    }
    public void updateMouse(double x, double y) {
        mouseX = x;
        mouseY = y;
    }
    //endregion ==============================

    public void handleInput(double elapsed) {
        // Don't let the player move if he's stunned
        if (hasState(EntityState.STAGGERED)) return;

        // Face mouse cursor
        double offsetX = QuickView.toWorldX(mouseX) - getX();
        double offsetY = QuickView.toWorldY(mouseY) - getY();
        double dir = Math.atan2(offsetY, offsetX);
        // Don't let the player look around if he's committed to an animation
        if (isReady()) setDirection(dir);

        // User can't control the character if it's velocity is greater than Physics.maxMoveSpeed
        // This has the positive side effect of disabling player controls during knockback
        if (getVelocity().getMagnitude() > Physics.maxMoveSpeed) return;

        // Mouse
        if (current.isMouseLeft()) {
            // Attack
            // TODO: attack chaining (if Attack ability is in the correct state, add the next ability to the queue)
            useAbility(Abilities.ATTACKPRIMARY);
        }

        // Keyboard
        double modifier = Physics.playerAcceleration + Physics.friction; // we add friction so we can have a net positive
        if (current.pressed(KeyCode.SPACE)) {
            // Dash
            useAbility(Abilities.DASH);
        }
        // if player is busy, don't let him move
        if (hasState(EntityState.CASTUP) ||
                hasState(EntityState.CASTING) ||
                hasState(EntityState.CASTDOWN)) return;
        if (current.pressed(KeyCode.W) && !current.pressed(KeyCode.S)) { // go up
            accelerate(new Coord(0.0, -modifier), elapsed);
            setState(EnumSet.of(EntityState.MOVING));
        } else if (current.pressed(KeyCode.S) && !current.pressed(KeyCode.W)) { // go down
            accelerate(new Coord(0.0, modifier), elapsed);
            setState(EnumSet.of(EntityState.MOVING));
        }
        if (current.pressed(KeyCode.A) && !current.pressed(KeyCode.D)) { // go left
            accelerate(new Coord(-modifier, 0.0), elapsed);
            setState(EnumSet.of(EntityState.MOVING));
        } else if (current.pressed(KeyCode.D) && !current.pressed(KeyCode.A)) { // go right
            accelerate(new Coord(modifier, 0.0), elapsed);
            setState(EnumSet.of(EntityState.MOVING));
        }
    }

}
