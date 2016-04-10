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
    public Coord(double[] pos) {
        x = pos[0];
        y = pos[1];
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
    public void setPos(double[] pos) {
        x = pos[0];
        y = pos[1];
    }
    public double[] getPos() {
        double[] result = {x, y};
        return result;
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

    public void setDirection(double dir) {
        double magnitude = getMagnitude();
        x = magnitude * Math.cos(dir);
        y = magnitude * Math.sin(dir);
    }

    public void doSubtract(Coord vector) {
        x -= vector.getX();
        y -= vector.getY();
    }

    public void doAdd(Coord vector) {
        x += vector.getX();
        y += vector.getY();
    }

    // As parameters: coordinates, map to check against
    public boolean verifyBounds (/* Overloads for x, y; Coord */) {
        // Implement map boundary check
        return true;
    }

    // Static methods for vector math
    public static Coord add(Coord vector1, Coord vector2) {
        double x = vector1.getX() + vector2.getX();
        double y = vector1.getY() + vector2.getY();
        return new Coord(x, y);
    }

    public static Coord subtract(Coord vector1, Coord vector2) {
        double x = vector1.getX() - vector2.getX();
        double y = vector1.getY() - vector2.getY();
        return new Coord(x, y);
    }

    public static double angleBetween(Coord vector1, Coord vector2) {
        return subtract(vector2, vector1).getDirection();
    }
}
