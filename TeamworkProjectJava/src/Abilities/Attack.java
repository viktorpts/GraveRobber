package Abilities;

import Enumerations.Abilities;
import Enumerations.EntityState;
import Models.Creature;
import World.Coord;

import java.util.EnumSet;

public class Attack extends Ability {

    double damage;
    double radius;
    Coord target;
    // TODO: add timing

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
        // TODO: change owner animation to attack
        owner.stop();
        owner.changeAnimation(Abilities.ATTACKPRIMARY.getName());
        owner.getState().add(EntityState.CASTINGINIT);

        // TODO: cycle all entities in affected area and propagate damage resolution
    }

}
