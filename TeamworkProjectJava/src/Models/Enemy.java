package Models;

import World.Coord;

public class Enemy extends Creature{
    LootTable lootTable;
    // TODO: add behaviour fields

    public Enemy(int startHealthPoints, int startAttackPower, int startArmorValue, double x, double y, boolean isAlive) {
        super(startHealthPoints, startAttackPower, startArmorValue, new Coord(x, y), isAlive);
        setBehaviour(1);
    }

    // TODO: add methods to initialize loot table
    // TODO: define behaviour, maybe in it's own class
}
