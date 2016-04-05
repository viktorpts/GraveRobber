package Models;

import World.Coord;

public class Player extends Creature {
    private Inventory inventory;

    // Load existing player
    public Player(Player oldPlayer) {
        super(oldPlayer.getHealthPoints(),
                oldPlayer.getAttackPower(),
                oldPlayer.getArmorValue(),
                oldPlayer.getPos(),
                oldPlayer.isAlive());
    }
    // Create new player
    public Player(int startHealthPoints,
                  int startAttackPower,
                  int startArmorValue,
                  Coord position,
                  boolean isAlive) {
        super(startHealthPoints, startAttackPower, startArmorValue, position, isAlive);
    }


    // TODO: Movement
    // This is temporary, to test rendering and mouse acquisition; /!\ Inverted coordinate system!
    //public void moveUp() {
    //    setY(getY() - 10);
    //}
    //public void moveDown() {
    //    setY(getY() + 10);
    //}
    //public void moveLeft() {
    //    setX(getX() - 10);
    //}
    //public void moveRight() {
    //    setX(getX() + 10);
    //}

    // TODO: Inventory management
}
