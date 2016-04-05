package World;

public class Coord {
    double x;
    double y;

    public Coord() {
        x = 0.0;
        y = 0.0;
    }
    public Coord(double inX, double inY) {
        x = inX;
        y = inY;
    }

    public double getX() {
        return x;
    }
    public void setX(double inX) {
        x = inX;
    }

    public double getY() {
        return y;
    }
    public void setY(double inY) {
        y = inY;
    }

    public void setPos(double inX, double inY) {
        x = inX;
        y = inY;
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }
    public void setMagnitude(double magnitude) {
        double scalar = magnitude / getMagnitude();
        x *= scalar;
        y *= scalar;
    }

    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public double getDirection() {
        double dir = Math.atan2(y, x);
        return dir;
    }

    public void subtract(Coord vector) {
        x -= vector.getX();
        y -= vector.getY();
    }

    public void add(Coord vector) {
        x += vector.getX();
        y += vector.getY();
    }

    // As parameters: coordinates, map to check against
    public boolean verifyBounds (/* Overloads for x, y; Coord */) {
        // Implement map boundary check
        return true;
    }
}
