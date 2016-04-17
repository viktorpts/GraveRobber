package Abilities;

import Enumerations.*;
import Game.Main;
import Models.Creature;
import Models.Enemy;
import World.Coord;
import World.Physics;

import java.util.EnumSet;

/**
 * Same as MeleeAttack, but has a short lunge just after INIT state
 */
public class ChargeAttack extends Ability {

    double damage;
    double range;
    double elapsedTime;

    // TODO: set cool down from constructor
    public ChargeAttack(Creature owner, double damage, double range) {
        super(owner, 1.0);
        this.damage = damage;
        this.range = range;
        elapsedTime = 0;
    }

    @Override
    public void use() {
        spend();
        elapsedTime = 0;
        state = AbilityState.INIT;
        owner.stop();
        owner.changeAnimation(Abilities.ATTACKPRIMARY.getName());
        owner.setState(EnumSet.of(EntityState.CASTUP));
    }

    @Override
    public void update(double time) {
        if (isReady()) return; // do nothing if ability is ready (not cooling, not in use)
        if (isCooling()) {
            cool(time);
            return;
        }
        if ((state == AbilityState.INIT || state == AbilityState.RESOLVE) &&
                !vrfyState()) {
            // entity state has changed from external source, cancel attack
            state = AbilityState.COOLING;
            return;
        }
        elapsedTime += time;
        switch (state) {
            case INIT: // attack wind up, can't be cancelled, can be interrupted by staggering effects
                if (owner.getAnimationState() == AnimationState.ATTACKING) {
                    // animation state has advanced, resolve damage
                    state = AbilityState.RESOLVE; // move to next state
                    // cycle states of owner
                    owner.getState().remove(EntityState.CASTUP);
                    owner.getState().add(EntityState.CASTING);
                    // Charge forward
                    Coord vector = new Coord(10, 0.0);
                    vector.setDirection(owner.getDirection());
                    owner.accelerate(vector, 1.0);
                }
                break;
            case RESOLVE: // apply effect continuously
                // damage instances have a cool down, so no danger of resolving more than once
                if (owner.getAnimationState() == AnimationState.ATTACKDOWN) {
                    // animation state has advanced, wind down
                    state = AbilityState.RECOVER; // move to next state
                    owner.getState().remove(EntityState.CASTING);
                    owner.getState().add(EntityState.CASTDOWN);
                } else {
                    // apply to everyone within range 1, for testing
                    // TODO: best to replace this with an event for all entities to register and decide what to do
                    // TODO: events are a great idea! we can stream everything just once and register the whole queue
                    Main.game.getLevel().getEntities().stream()
                            .filter(entity -> {
                                boolean result = true;
                                if (!(entity instanceof Creature) || entity == owner)
                                    return false; // only damage creatures and not self
                                if (owner instanceof Enemy && entity instanceof Enemy)
                                    return false; // don't damage allies
                                if (entity.hasState(EntityState.DEAD))
                                    return false; // don't hit dead creatures
                                if (Coord.subtract(entity.getPos(), owner.getPos()).getMagnitude() > range)
                                    return false;
                                if (Math.abs(Math.abs(Coord.angleBetween(owner.getPos(), entity.getPos())) - Math.abs(owner.getDirection())) > Math.PI / 6)
                                    return false;
                                return true; // if everything's been fine, process target
                            })
                            .forEach(entity -> {
                                Creature current = (Creature) entity;
                                current.takeDamage(damage, DamageType.WEAPONMELEE, owner.getPos());
                            });
                }
                break;
            case RECOVER: // attack wind down, can be cancelled by other abilities (chaining attacks, dodging, etc.)
                if (owner.getAnimationState() != AnimationState.ATTACKDOWN) {
                    // animation state has advanced, go back to ready
                    reset(); // if attack was successful, reset cooldown
                    owner.setState(EnumSet.of(EntityState.IDLE));
                }
                break;
        }
    }
}
