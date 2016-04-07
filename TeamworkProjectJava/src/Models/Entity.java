package Models;

import Renderer.QuickView;
import World.Coord;

abstract public class Entity {
    private Boolean alive;
    private Coord position;
    private double direction;
    private Sprite sprite;   // TODO: don't forget to initialize this in a constructor instead
    // TODO: Add methods to initialize a sprite and output it to a display interface /!\ depends on direction

    public Entity() {
        alive = true;
    }
    public Entity(double x, double y, boolean isAlive) {
        this.alive = isAlive;
        position = new Coord(x, y);
        sprite = new Sprite(10);
    }
    public Entity(Coord position, boolean isAlive) {
        this.alive = isAlive;
        this.position = position;
        sprite = new Sprite(10);
    }

    // Coord modifiers
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

    // Alive modifiers
    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public Boolean isAlive() {
        return alive;
    }

    public double getDirection() {
        return direction;
    }
    public void setDirection(double direction) {
        this.direction = direction;
    }

    // TODO: Output sprite to display interface /!\ depends on direction
    public void render()

    {
        if (sprite == null || !alive) return;

        // A rather hasty implementation for debugging purposes
        QuickView.renderSprite(position.getX(), position.getY(), direction);
    }

    public void animate(double time) {
        sprite.advance(time);
    }
}

