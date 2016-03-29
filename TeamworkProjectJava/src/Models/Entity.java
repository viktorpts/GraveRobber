package Models;

public class Entity {
    private Boolean isAlive;
    private double x = 0;
    private double y = 0;

    public Entity(double x, double y,boolean isAlive) {
        this.x = x;
        this.y = y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Boolean getAlive() {

        return isAlive;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
  }

