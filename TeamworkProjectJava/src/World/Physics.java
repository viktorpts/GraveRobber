package World;

public class Physics {
    final public static double maxVelocity = 20.0;          // Tiles per second
    final public static double maxMoveSpeed = 6.0;          // Tiles per second
    final public static double maxAcceleration = 40.0;      // Tiles per second ^ 2
    final public static double friction = 20.0;             // Tiles per second ^ 2
    final public static double playerAcceleration = 30.0;    // Tiles per second ^ 2
    // Effective max acceleration is (maxAcceleration - friction)

    // Object deceleration due to friction
    public static void decelerate(Coord vector, double time) {
        double direction = vector.getDirection();
        Coord brake = new Coord(time * friction * Math.cos(direction), time * friction * Math.sin(direction));
        if (brake.getMagnitude() > vector.getMagnitude()) {
            // Prevent reverse acceleration
            vector.setPos(0.0, 0.0);
        } else {
            vector.subtract(brake);
        }
    }
}
