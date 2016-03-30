package Models;

import World.Position;

abstract public class Entity {
    private Boolean isAlive;
    private Position position;

    public Entity(double x, double y, boolean isAlive) {
        position = new Position(x, y);
    }

    // Position modifiers
    public Position getPos() {
        return position;
    }
    public void setPos(double x, double y) {
        position.setPos(x, y);
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
        isAlive = alive;
    }

    public Boolean getAlive() {
        return isAlive;
    }
}

