package Abilities;

import Models.Creature;
import World.Coord;

public class Attack extends Ability {

    double damage;
    double radius;
    Coord target;

    public Attack(Creature owner, double damage, double radius) {
        super(owner, 1.0);
        this.damage = damage;
        this.radius = radius;
        this.target = new Coord(0.0, 0.0);
    }

    public void setTarget(Coord target) {
        this.target.setPos(target.getPos());
    }

    public void use() {
        if (!ready) return;
        spend();
        // change owner animation to attack
        owner.stop();

        // TODO: cycle all entities in affected area and propagate damage resolution
    }

}
