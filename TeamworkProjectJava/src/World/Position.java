package World;

/**
 * Fixed coordinate on the map, must include methods to work with most numeric variable types (overloads for int, long,
 * double, etc.). May include some logic to verify map boundaries, to prevent placement outside the level.
 */
public class Position {
    double x;
    double y;

    public Position () {
        _Position(0.0, 0.0);
    }
    public Position (int inX, int inY) {
        _Position((double)inX, (double)inY);
    }
    public Position (long inX, long inY) {
        _Position((double)inX, (double)inY);
    }
    public Position (float inX, float inY) {
        _Position((double)inX, (double)inY);
    }
    public Position (double inX, double inY) {
        _Position(inX, inY);
    }
    private void _Position (double inX, double inY) {
        // This is the actual initializer, other constructor overloads only perform casting
        x = 0;
        y = 0;
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

    // As parameters: coordinates, map to check against
    public boolean VerifyBounds (/* Overloads for x, y; Position */) {
        // Implement map boundary check
        return true;
    }
}
