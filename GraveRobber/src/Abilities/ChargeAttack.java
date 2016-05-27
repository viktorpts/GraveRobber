package Abilities;

import Enumerations.*;
import Models.Creature;
import World.Coord;

/**
 * Same as MeleeAttack, but has a short lunge just after INIT state. Narrower sweep and range.
 */
public class ChargeAttack extends MeleeAttack {

    public ChargeAttack(Creature owner, double resolution, double damage, double range, double cooldown) {
        super(owner, resolution, damage, range, cooldown);
        this.damage = damage;
        this.range = range;
        this.width = Math.PI / 6; // this is half the sweep, to simplify calculations
    }

    /**
     * Place ability in initial stage, begin owner animation accordingly. We set the cooldown at first, so if the owner
     * gets staggered, there's longer recovery time as punishment. Successful attacks reset the cooldown.
     */
    @Override
    public void use() {
        super.use();
        // Charge forward
        Coord vector = new Coord(10, 0.0);
        vector.setDirection(owner.getDirection());
        owner.accelerate(vector, 1.0);
    }

    @Override
    public void resolve() {
        getValidTargets().forEach(entity -> entity.takeDamage(damage, DamageType.WEAPONMELEE, owner.getPos()));
        // Charge attacks do not reset, or else hilarity ensues
    }

}
