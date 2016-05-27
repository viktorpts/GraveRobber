package Abilities;

import Enumerations.*;
import Game.Main;
import Models.Creature;
import Models.Enemy;
import World.Coord;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * Note that attacks don't actually have a cooldown, since another attack cannot be initiated before the first one has
 * resolved, but we use the cooldown to disable attacking if attack was cancelled (by staggering or other effects)
 */
public class MeleeAttack extends Ability {

    double damage;
    double range;
    double width;

    public MeleeAttack(Creature owner, double resolution, double damage, double range, double cooldown) {
        super(owner, cooldown, resolution);
        this.damage = damage;
        this.range = range;
        this.width = Math.PI / 4; // this is half the sweep, to simplify calculations
    }

    /**
     * Place ability in initial stage, begin owner animation accordingly. We set the cooldown at first, so if the owner
     * gets staggered, there's longer recovery time as punishment. Successful attacks reset the cooldown.
     */
    @Override
    public void use() {
        spend();
        elapsedTime = 0;
        state = AbilityState.INIT;
        owner.stop();
        owner.changeAnimation(Sequences.ATTACK, false);
        owner.setState(EnumSet.of(EntityState.CASTUP));
    }

    @Override
    public void resolve() {
        getValidTargets().forEach(entity -> entity.takeDamage(damage, DamageType.WEAPONMELEE, owner.getPos()));
        reset();
    }

    protected Stream<Creature> getValidTargets() {
        return Main.game.getLevel().getEntities().stream()
                .filter(entity -> {
                    boolean result = true;
                    if (!(entity instanceof Creature) || entity == owner)
                        return false; // only damage creatures and not self
                    if (owner instanceof Enemy && entity instanceof Enemy)
                        return false; // don't damage allies
                    if (entity.hasState(EntityState.DEAD) || entity.hasState(EntityState.DIE))
                        return false; // don't hit dead creatures
                    if (Coord.dist(entity, owner) > range)
                        return false; // only damage those in range
                    if (Coord.innerAngle(owner.getPos(), entity.getPos(), owner.getDirection()) > width)
                        return false; // only damage those within sweep angle
                    return true; // if everything's been fine, process target
                })
                .map(entity -> (Creature) entity);
    }

}
