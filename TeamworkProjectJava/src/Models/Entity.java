package Models;

import Enumerations.EntityState;
import Renderer.Animation;
import World.Coord;

import java.util.EnumSet;

abstract public class Entity {

    private static int lastID = 0; // all entities know the last assigned ID
    private int ID; // ID
    private EnumSet<EntityState> state;
    private Coord position;
    private double direction;
    private double radius; // used for collision detection
    private Animation animation;   // TODO: don't forget to initialize this in a constructor instead
    // TODO: Add methods to initialize a sprite and output it to a display interface /!\ depends on direction

    public Entity(Animation animation, double x, double y, double direction) {
        ID = ++lastID; // increment then assign

        this.animation = animation;
        position = new Coord(x, y);
        this.direction =  direction;
        state = EnumSet.of(EntityState.IDLE); // all entities idle at creation, modify using set method

        // TODO: add a way for this to be initialized to a different value
        radius = 0.25;
    }

    public int getID() {
        return ID;
    }

    // Position and orientation properties
    public Coord getPos() {
        return position;
    }
    public void setPos(double x, double y) {
        position.setPos(x, y);
    }
    public void setPos(Coord position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    public double getX() {
        return position.getX();
    }
    public void setX(double x) {
        position.setX(x);
    }

    public double getY() {
        return position.getY();
    }
    public void setY(double y) {
        position.setY(y);
    }

    public double getDirection() {
        return direction;
    }
    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getRadius() {
        return radius;
    }

    // State sets
    public void setState(EnumSet state) {
        this.state = state;
    }
    public EnumSet<EntityState> getState() {
        return state;
    }

    public boolean hasState(EntityState state) {
        return this.state.contains(state);
    }
    // Shorthands
    public boolean isAlive() {
        return (!state.contains(EntityState.DESTROYED));
    }

    public boolean isReady() {
        // Check against a list of all unchangeable states
        return state.contains(EntityState.READY);
    }

    // TODO: Output sprite to display interface /!\ depends on direction
    public void render()
    {
        if (animation == null || !isAlive()) return;

        int selector = 0; // class type based color
        if (this instanceof Player) selector = 1; // change color of player
        animation.output(selector, position.getX(), position.getY(), direction);
    }

    public void animate(double time) {
        animation.advance(time);
    }

    public void changeAnimation(String animation) {
        // TODO: information should be passed between ability and animation
    }

}

