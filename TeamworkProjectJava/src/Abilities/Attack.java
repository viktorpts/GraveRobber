package Abilities;

import World.Coord;

public class Attack extends Ability {

    double damage;
    double radius;
    Coord target;

    public Attack(double damage, double radius) {
        this.damage = damage;
        this.radius = radius;
        this.target = new Coord(0.0, 0.0);
    }

    public void setTarget(Coord target) {
        this.target.setPos(target.getPos());
    }

    public void use() {
        spend();

        // TODO: cycle all entities in affected area and propagate damage resolution
    }

}
