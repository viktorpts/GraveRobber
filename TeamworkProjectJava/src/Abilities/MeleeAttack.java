package Abilities;

import Enumerations.*;
import Game.Main;
import Models.Creature;
import World.Coord;

import java.util.EnumSet;

/**
 * Note that attacks don't actually have a cooldown, since another attack cannot be initiated before the first one has
 * resolved, but we use the cooldown to disable attacking if attack was cancelled (by staggering or other effects)
 */
public class MeleeAttack extends Ability {

    double damage;
    double radius;
    Coord target;
    double elapsedTime;

    // TODO: set cool down from constructor
    public MeleeAttack(Creature owner, double damage, double radius) {
        super(owner, 1.0);
        this.damage = damage;
        this.radius = radius;
        this.target = new Coord(0.0, 0.0);
        elapsedTime = 0;
    }

    /**
     * TODO:
     * find a way to set target directly with usage; the problem is we want the target some times to be a location
     * and other times to be an Entity, or even no target
     */
    public void setTarget(Coord target) {
        this.target.setPos(target.getPos());
    }
    // TODO: Since this is a melee attack, we likely don't need this, just project in front of caster

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
        if ((state == AbilityState.INIT ||state == AbilityState.RESOLVE) &&
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
                    // TODO: change this to a sweep, filter with Coord.angleBetween() (currently AoE centered on source)
                    Main.game.getLevel().getEntities().stream()
                            .filter(entity -> entity instanceof Creature)
                            .filter(entity -> Coord.subtract(entity.getPos(), owner.getPos()).getMagnitude() <= 1.0)
                            .forEach(entity -> {
                                if (entity == owner) return; // don't knock ourselves back, duh
                                Creature current = (Creature) entity;
                                current.takeDamage(damage, DamageType.WEAPONMELEE, owner.getPos());
                            });
                }
                break;
            case RECOVER: // attack wind down, can be cancelled by other abilities (chaining attacks, dodging, etc.)
                if (owner.getAnimationState() != AnimationState.ATTACKDOWN) {
                    // animation state has advanced, go back to ready
                    reset();
                    owner.setState(EnumSet.of(EntityState.IDLE));
                }
                break;
        }
    }
}
