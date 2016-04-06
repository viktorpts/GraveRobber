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

    // TODO: Inventory management
}
